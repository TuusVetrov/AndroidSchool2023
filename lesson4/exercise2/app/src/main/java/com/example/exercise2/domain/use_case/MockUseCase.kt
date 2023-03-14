package com.example.exercise2.domain.use_case

import com.example.exercise2.R
import com.example.exercise2.data.repository.Author
import com.example.exercise2.data.repository.Book
import com.example.exercise2.data.repository.BookAvailability
import com.example.exercise2.data.repository.MockRepository
import com.example.exercise2.utils.Exception

class MockUseCase(
    private val repository: MockRepository
) {
    suspend fun getBooks(): List<Book> {
        val data = repository.getBooks()
        return data.getOrElse {
            throw Exception.RepositoryException(R.string.error_message_get_books)
        }
    }

    suspend fun getAuthors(): List<Author> {
        val data = repository.getAuthors()
        return data.getOrElse {
            throw Exception.RepositoryException(R.string.error_message_get_authors)
        }
    }

    suspend fun getAvailability(): List<BookAvailability> {
        val data = repository.getAvailability()
        return data.getOrElse {
            throw Exception.RepositoryException(R.string.error_message_get_availability)
        }
    }
}