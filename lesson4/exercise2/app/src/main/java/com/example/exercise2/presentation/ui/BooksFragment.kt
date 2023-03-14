package com.example.exercise2.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exercise2.databinding.FragmentBooksBinding
import kotlinx.coroutines.launch

class BooksFragment : Fragment() {
    private lateinit var binding: FragmentBooksBinding
    private val booksViewModel: BooksViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        booksViewModelObserver()

        binding.repeatButton.setOnClickListener {
            booksViewModel.fetchData()
        }
    }

    private fun booksViewModelObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                booksViewModel.uiState.collect() {
                    when(it) {
                        is BookListUiState.Success -> {
                            binding.errorTextView.visibility = View.GONE
                            binding.recyclerView.apply {
                                layoutManager = LinearLayoutManager(requireContext())
                                adapter = AuthorAdapter(requireContext(), it.data)
                            }
                        }
                        is BookListUiState.Error -> {
                            binding.errorTextView.text = getText(it.message)
                            binding.errorTextView.visibility = View.VISIBLE
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = BooksFragment()
    }
}