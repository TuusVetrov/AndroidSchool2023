package com.example.exercise2.data.repository

import kotlinx.coroutines.delay
import java.util.UUID
import kotlin.random.Random

class MockRepository {
    companion object {
        private val bookIds = (0..4).map {
            UUID.randomUUID().toString()
        }

        private val authorIds = (0..1).map {
            UUID.randomUUID().toString()
        }
    }

    suspend fun getBooks(): Result<List<Book>> {
        randomDelay()
        return randomResult(
            listOf(
                Book(
                    bookIds[0],
                    "The Hobbit",
                    authorIds[0]
                ),
                Book(
                    bookIds[1],
                    "The Winds of Winter",
                    authorIds[1]
                ),
                Book(
                    bookIds[2],
                    "The Silmarillion",
                    authorIds[0]
                ),
                Book(
                    bookIds[3],
                    "The Return of the King",
                    authorIds[0]
                ),
                Book(
                    bookIds[4],
                    "A Dream of Spring",
                    authorIds[1]
                ),
            )
        )
    }

    suspend fun getAuthors(): Result<List<Author>> {
        randomDelay()
        return randomResult(
            listOf(
                Author(
                    authorIds[0],
                    "J. R. R. Tolkien"
                ),
                Author(
                    authorIds[1],
                    "George R. R. Martin"
                ),
            )
        )
    }

    suspend fun getAvailability(): Result<List<BookAvailability>> {
        randomDelay()
        return randomResult(
            bookIds.map { bookId ->
                BookAvailability(
                    bookId = bookId,
                    inStock = Random.nextBoolean()
                )
            }
        )
    }

    private suspend fun randomDelay() {
        delay((100L..1000L).random())
    }

    private fun <T> randomResult(data: T): Result<T> =
        if ((0..100).random() < 5) {
            Result.failure(RuntimeException())
        } else {
            Result.success(data)
        }
}

data class Book(
    val bookId: String,
    val title: String,
    val authorId: String
)

data class Author(
    val authorId: String,
    val name: String
)

data class BookAvailability(
    val bookId: String,
    val inStock: Boolean
)
