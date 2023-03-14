package com.example.exercise2.presentation.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.exercise2.R
import com.example.exercise2.domain.model.AuthorItem

class AuthorAdapter(
    private val context: Context,
    private val authors: List<AuthorItem>
) : RecyclerView.Adapter<AuthorAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val authorName: TextView = view.findViewById(R.id.authorTextView)
        val authorBooks: TextView = view.findViewById(R.id.booksTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book,parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = authors.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val author = authors[position]
        holder.authorName.text = author.name

        val booksInfo = java.lang.StringBuilder()

        author.books.forEach { book ->
            booksInfo.append(book.first + " ")
            if (book.second) {
                booksInfo.append(" (${context.getString(R.string.book_in_stock)})\n")
            }else {
                booksInfo.append(" (${context.getString(R.string.book_out_stock)})\n")
            }
        }
        holder.authorBooks.text = booksInfo.toString()
    }
}