package com.example.hxh_project.presentation.ui.place_picker

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.example.hxh_project.R
import com.example.hxh_project.databinding.FragmentPlacePickerBinding
import com.example.hxh_project.utils.extensions.setWindowTransparency
import com.example.hxh_project.utils.extensions.updateMargin
import com.yandex.mapkit.Animation
import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.location.*
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.search.*
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlacePickerFragment : Fragment(), UserLocationObjectListener, CameraListener
{
    private lateinit var binding: FragmentPlacePickerBinding

    private val viewModel: PlacePickerViewModel by viewModels()

    private lateinit var userLocationLayer: UserLocationLayer
    private lateinit var mapObjects: MapObjectCollection
    private lateinit var locationManager: LocationManager
    private lateinit var searchManager: SearchManager

    private var lastKnownLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapKitFactory.initialize(requireContext())
        SearchFactory.initialize(requireContext())

        requestLocationPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlacePickerBinding.inflate(inflater, container, false)
        setWindowTransparency(binding.root) { statusBarSize, navigationBarSize ->
            val topMargin = resources.getDimension(R.dimen.normal_100).toInt()
            val bottomMargin = resources.getDimension(R.dimen.normal_200).toInt()

            binding.appBarLayout.updateMargin(top = statusBarSize + topMargin)
            binding.bottomBar.updateMargin(bottom = navigationBarSize + bottomMargin)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        elementInit()
        initOnClicks()
    }

    private fun elementInit() {
        binding.mapView.map.isRotateGesturesEnabled = false

        userLocationLayer = MapKitFactory.getInstance().createUserLocationLayer(binding.mapView.mapWindow)
        userLocationLayer.setObjectListener(this)
        userLocationLayer.isVisible = true

        mapObjects = binding.mapView.map.mapObjects.addCollection()

        locationManager = MapKitFactory.getInstance().createLocationManager()
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.ONLINE)

        moveCamera(ZERO_POINT, 4.716611f )

        binding.mapView.map.addCameraListener(this)

        lastKnownLocation = LocationManagerUtils.getLastKnownLocation()
        if (lastKnownLocation != null) {
            animateCamera(lastKnownLocation!!.position)
        }
    }

    private fun initOnClicks() {
        binding.ivMarker.setOnClickListener { selectPlace() }

        binding.topAppBar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.topAppBar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.btnChoose -> {
                    onResult()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun onResult() {
        if (viewModel.uiState.value != null) {
            Log.d(REQUEST_KEY, viewModel.uiState.value.toString())
            setFragmentResult(REQUEST_KEY, bundleOf(KEY_ADDRESS to viewModel.uiState.value))
            parentFragmentManager.popBackStack()
        }
    }

    private fun animateCamera(point: Point) {
        binding.mapView.map.move(
            CameraPosition(point, DEFAULT_SCALE, 0f, 0f),
            Animation(Animation.Type.SMOOTH, 1f),
            null
        )
    }

    private fun moveCamera(point: Point, scaleFactor: Float) {
        binding.mapView.map.move(
            CameraPosition(
                point,
                scaleFactor,
                0F,
                0F
            ))
    }

    private fun showAddress(place: GeoObject) {
        //ghetto way to get address :)
        val components = place
            .metadataContainer
            .getItem(ToponymObjectMetadata::class.java)
            .address
            .components

        val city = components
            .firstOrNull {it.kinds.contains(Address.Component.Kind.LOCALITY)}
            ?.name

        val street = components
            .firstOrNull {it.kinds.contains(Address.Component.Kind.STREET)}
            ?.name

        val house = components
            .firstOrNull {it.kinds.contains(Address.Component.Kind.HOUSE)}
            ?.name

        if (city != null && street != null && house != null) {
            binding.bottomBar.visibility = View.VISIBLE
            val address = "$city, $street, $house"
            viewModel.setAddress(address)
            binding.tvAddress.text = address
        }
    }

    private fun hideAddress() {
        binding.bottomBar.visibility = View.GONE
        viewModel.setAddress(null)
        binding.ivMarker.setOnClickListener { selectPlace() }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        locationManager.resume()
    }

    override fun onPause() {
        super.onPause()
        locationManager.suspend()
    }

    private val searchListener: Session.SearchListener = object : Session.SearchListener {
        override fun onSearchResponse(response: Response) {
            val toponym = response.metadata.toponym
            toponym?.let { showAddress(it) }
        }

        override fun onSearchError(error: Error) {
            var errorMessage = getString(R.string.unknown_error_message)
            if (error is RemoteError) {
                errorMessage = getString(R.string.remote_error_message)
            } else if (error is NetworkError) {
                errorMessage = getString(R.string.network_error_message)
            }
            Log.e(
                PlacePickerFragment::class.java.canonicalName,
                errorMessage
            )
        }
    }

    override fun onStop() {
        binding.mapView.onStop();
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    private fun selectPlace() {
        val options = SearchOptions()
        options.searchTypes = SearchType.BIZ.value
        val center: Point = binding.mapView.map.cameraPosition.target
        searchManager.submit(center, 20, options, searchListener)
    }

    // Get the best and most recent location of the device
    private fun getDeviceLocation() {
        val locationListener = object : LocationListener {
            override fun onLocationUpdated(location: Location) {
                lastKnownLocation = location
                animateCamera(location.position)
            }

            override fun onLocationStatusUpdated(locationStatus: LocationStatus) {
            }
        }
        locationManager.requestSingleUpdate(locationListener)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getDeviceLocation()
        }
    }

    private fun requestLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED -> {

            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.location_permission_title))
                    .setMessage(getString(R.string.location_permission_message))
                    .setPositiveButton(getString(R.string.btn_allow_text)) { dialog, _ ->
                        dialog.dismiss()
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                    }
                    .setNegativeButton(getString(R.string.btn_cancel_text)) { dialog, _ ->
                        dialog.dismiss()
                        moveCamera(ZERO_POINT, 4.716611f )
                    }
                    .show()
            }
            else -> { requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION) }
        }
    }

    override fun onCameraPositionChanged(
        map: Map,
        cameraPosition: CameraPosition,
        cameraUpdateReason: CameraUpdateReason,
        isFinished: Boolean
    ) {
        if (cameraUpdateReason == CameraUpdateReason.GESTURES) {
            if(isFinished) {
                selectPlace()
            } else {
                hideAddress()
            }
        }
    }

    override fun onObjectAdded(p0: UserLocationView) {}

    override fun onObjectRemoved(p0: UserLocationView) {}

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {}

    companion object {
        const val REQUEST_KEY = "placePickerRequestKey"
        const val KEY_ADDRESS = "placePickerAddress"
        private const val DEFAULT_SCALE = 17f
        private val ZERO_POINT = Point(55.755969527097506, 37.61763538248735)
    }
}