package com.example.lesson4.presentation.ui.temperature

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

import com.example.lesson4.databinding.FragmentTemperatureBinding
import com.example.lesson4.utils.State
import kotlinx.coroutines.launch

class TemperatureFragment : Fragment() {
    private lateinit var binding: FragmentTemperatureBinding
    private val viewModel: TemperatureViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTemperatureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelObserver()

        binding.buttonGetTemperature.setOnClickListener {
            viewModel.fetchTemperature(binding.textTemperature.text.toString())
        }

        binding.textTemperature.setOnEditorActionListener {  _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                event.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_ENTER
            ){
                viewModel.fetchTemperature(binding.textTemperature.text.toString())
                true
            } else {
                false
            }
        }
    }

    private fun viewModelObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                   when(it){
                       is State.Loading -> {}
                       is State.Success -> {
                           it.data.let { TemperatureData ->
                               binding.tvTemperature.text = TemperatureData.main.temp.toString()
                           }
                       }
                       is State.Error -> {
                           Toast.makeText(activity,
                                          it.message,
                                          Toast.LENGTH_SHORT)
                                          .show()
                       }
                       else -> {}
                   }
                }
            }
        }
    }

    companion object {
        fun newInstance() = TemperatureFragment()
    }
}