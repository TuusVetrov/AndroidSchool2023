package com.example.hxh_project.presentation.ui.settings

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import coil.transform.CircleCropTransformation
import com.example.hxh_project.BuildConfig
import com.example.hxh_project.R
import com.example.hxh_project.databinding.FragmentSettingsBinding
import com.example.hxh_project.presentation.ui.place_picker.PlacePickerFragment
import com.example.hxh_project.presentation.ui.utils.SnackbarListener
import com.example.hxh_project.presentation.ui.utils.dialogs.JobBottomSheetDialog
import com.example.hxh_project.presentation.ui.utils.dialogs.PhotoPickerBottomDialog
import com.example.hxh_project.presentation.ui.profile.ProfileFragment
import com.example.hxh_project.utils.TakePictureWithUriReturnContract
import com.example.hxh_project.utils.extensions.compressImageFile
import com.example.hxh_project.utils.extensions.navigateLogout
import com.example.hxh_project.utils.extensions.navigateTo
import com.example.hxh_project.utils.extensions.setError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class SettingsFragment :
    Fragment(),
    JobBottomSheetDialog.SettingsBottomSheetDialogListener,
    PhotoPickerBottomDialog.PhotoPickerBottomDialogListener
{
    private lateinit var binding: FragmentSettingsBinding

    private val viewModel: SettingsViewModel by viewModels()

    private lateinit var jobDialog: JobBottomSheetDialog
    private lateinit var jobListener: JobBottomSheetDialog.SettingsBottomSheetDialogListener
    private lateinit var photoDialog: PhotoPickerBottomDialog
    private lateinit var photoListener: PhotoPickerBottomDialog.PhotoPickerBottomDialogListener

    private var snackbarListener: SnackbarListener? = null


    private var isFirstLaunch = true
    private var imgPath: String = ""

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            takePhoto()
        }
    }

    private val selectImageFromGalleryResult =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                var queryImageUrl = it.path!!

                CoroutineScope(Dispatchers.Main).launch {
                    queryImageUrl = compressImageFile(queryImageUrl, false, it)

                    if (queryImageUrl.isNotEmpty()) {
                        viewModel.setImage(queryImageUrl)
                        binding.ivProfile.load(File(queryImageUrl)) {
                            error(R.drawable.ic_profile_photo)
                            transformations(CircleCropTransformation())
                        }
                    }
                }
            }
        }

    private val takeImageResult =
        registerForActivityResult(
            TakePictureWithUriReturnContract())
        { (isSuccess, imageUri) ->
            if (isSuccess) {
                var queryImageUrl = imgPath
                CoroutineScope(Dispatchers.Main).launch {
                    queryImageUrl = compressImageFile(queryImageUrl, false, imageUri)

                    if (queryImageUrl.isNotEmpty()) {
                        viewModel.setImage(queryImageUrl)

                        binding.ivProfile.load(imageUri) {
                            error(R.drawable.ic_profile_photo)
                            transformations(CircleCropTransformation())
                        }
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        jobListener = this
        photoListener = this
        snackbarListener = requireActivity() as? SnackbarListener
        viewModel.getUserProfile()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        jobDialog = JobBottomSheetDialog(jobListener)
        photoDialog = PhotoPickerBottomDialog(photoListener)
        setListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
    }

    private fun setListeners() {
        setToolBarMenuLister()

        binding.layoutJob.editText?.apply {
            isFocusable = false
            isClickable = true

            setOnClickListener {
                jobDialog.show(parentFragmentManager, JobBottomSheetDialog.TAG)
            }
        }

        binding.ivProfile.setOnClickListener {
            photoDialog.show(parentFragmentManager, PhotoPickerBottomDialog.TAG)
        }

        binding.layoutName.editText?.addTextChangedListener {
            viewModel.setName(it.toString())
        }

        binding.layoutSurname.editText?.addTextChangedListener {
            viewModel.setSurname(it.toString())
        }

        binding.layoutOtherJob.editText?.addTextChangedListener {
            viewModel.setJob(it.toString())
        }

        binding.btnChangeData.setOnClickListener {
            viewModel.changeCredentials()
        }

        binding.layoutOtherJob.editText?.setOnEditorActionListener {  _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                event.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_ENTER
            ){
                viewModel.changeCredentials()
                true
            } else {
                false
            }
        }
    }

    private fun setToolBarMenuLister() {
        binding.toolbarSettings.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    settingsStateHandler(it)
                }
            }
        }
    }

    private fun setInputLayoutErrors(state: SettingsState) {
        binding.layoutName.setError(state.isValidName == false) {
            getString(R.string.message_field_name_invalid)
        }

        binding.layoutSurname.setError(state.isValidSurname == false) {
            getString(R.string.message_field_surname_invalid)
        }

        binding.layoutOtherJob.setError(state.isValidJob == false) {
            getString(R.string.message_field_other_job_invalid)
        }
    }

    private fun settingsStateHandler(state: SettingsState) {
        if (!state.isUserLoggedIn) {
            navigateLogout()
            return
        }

        if (state.isSuccessfullyModified) {
            navigateToProfile()
        }

        if (isFirstLaunch && state.userImage != null) {
            binding.ivProfile.load(state.userImage) {
                crossfade(true)
                placeholder(R.drawable.ic_profile_photo)
                error(R.drawable.ic_profile_photo)
                transformations(CircleCropTransformation())
            }

            binding.layoutName.editText?.setText(state.name)
            binding.layoutJob.editText?.setText(state.job)
            binding.layoutSurname.editText?.setText(state.surname)
            isFirstLaunch = false
        }

        setInputLayoutErrors(state)

        val errorMessage = state.error
        if (errorMessage != null) {
            snackbarListener?.showError(errorMessage)
            viewModel.clearError()
        }
    }

    private fun navigateToProfile() {
        val message = getString(R.string.settings_successfully_changed)
        snackbarListener?.showSuccess(message)
        navigateTo<ProfileFragment>(false)
    }

    override fun onJobClick(jobTitle: String) {
        viewModel.setJob(jobTitle)
        binding.layoutJob.editText?.setText(jobTitle)

        val jobOther = getString(R.string.bottom_sheet_job_other)
        binding.layoutOtherJob.visibility = if (jobTitle == jobOther) View.VISIBLE else View.GONE

        jobDialog.dismiss()
    }

    override fun onTakePhoto() {
        requestCameraPermission()
        photoDialog.dismiss()
    }

    override fun onGetFromGallery() {
        selectImageFromGalleryResult.launch("image/*")
        photoDialog.dismiss()
    }

    private fun takePhoto() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                takeImageResult.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val folder = File("${requireContext().getExternalFilesDir(Environment.DIRECTORY_DCIM)}")
        folder.mkdirs()

        val file = File(folder, "tmp_image_file.jpg")
        if (file.exists())
            file.delete()
        file.createNewFile()
        val imageUri = FileProvider.getUriForFile(
            requireContext(),
            BuildConfig.APPLICATION_ID + getString(R.string.file_provider_name),
            file
        )
        imgPath = file.absolutePath
        return imageUri!!
    }

    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA,
            ) == PackageManager.PERMISSION_GRANTED -> {
                takePhoto()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.camera_permission_title))
                    .setMessage(getString(R.string.camera_permission_message))
                    .setPositiveButton(getString(R.string.btn_allow_text)) { dialog, _ ->
                        dialog.dismiss()
                        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                    .setNegativeButton(getString(R.string.btn_cancel_text)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
            else -> { requestPermissionLauncher.launch(Manifest.permission.CAMERA) }
        }
    }
}