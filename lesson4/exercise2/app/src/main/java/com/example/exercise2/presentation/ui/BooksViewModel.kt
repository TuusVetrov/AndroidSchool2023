package com.example.exercise2.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exercise2.data.repository.Author
import com.example.exercise2.data.repository.Book
import com.example.exercise2.data.repository.BookAvailability
import com.example.exercise2.data.repository.MockRepository
import com.example.exercise2.domain.model.AuthorItem
import com.example.exercise2.domain.use_case.MockUseCase
import com.example.exercise2.utils.Exception
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

sealed class BookListUiState {
    object Init: BookListUiState()
    data class Success(val data: List<AuthorItem>) : BookListUiState()
    data class Error(val message: Int) : BookListUiState()
}

class BooksViewModel() : ViewModel() {
    private val _uiState: MutableStateFlow<BookListUiState> = MutableStateFlow(BookListUiState.Init)
    val uiState: StateFlow<BookListUiState?> = _uiState

    private val mockUseCase = MockUseCase(MockRepository())

    fun fetchData() {
        viewModelScope.launch {
            try {
                supervisorScope {
                    val booksDeferred = async {
                        mockUseCase.getBooks()
                    }
                    val authorsDeferred = async {
                        mockUseCase.getAuthors()
                    }
                    val availabilityDeferred = async {
                        mockUseCase.getAvailability()
                    }

                    val booksResult = booksDeferred.await().sortedBy { it.title }
                    val authorsResult = authorsDeferred.await().sortedBy { it.name }
                    val availabilityResult = availabilityDeferred.await()

                    val data = sortBook(booksResult, authorsResult, availabilityResult)

                    _uiState.update {
                        BookListUiState.Success(data)
                    }
                }
            }catch (e: Exception.RepositoryException) {
                _uiState.update {
                    BookListUiState.Error(e.messageId)
                }
            }
        }
    }

    private fun sortBook(
        books: List<Book>,
        authors: List<Author>,
        availability: List<BookAvailability>): List<AuthorItem> {
        val booksAvailability = availability.associateBy { it.bookId }
        val authorBooks = books.groupBy { it.authorId }

        return authors.map { author ->
            val authorName = author.name
            val booksList = authorBooks[author.authorId]?.map { book ->
                Pair(book.title, booksAvailability[book.bookId]?.inStock ?: false)
            } ?: emptyList()

            AuthorItem(authorName, booksList)
        }

    }
}