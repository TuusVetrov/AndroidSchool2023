package com.example.hxh_project.presentation.ui.orders.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.hxh_project.R
import com.example.hxh_project.databinding.OrderItemBinding
import com.example.hxh_project.domain.model.Order
import com.example.hxh_project.utils.FormatUtils.formatDate

class OrdersAdapter(
    private val context: Context,
    private val onButtonCancelClickListener: (Order) -> Unit
): PagingDataAdapter<Order, OrdersAdapter.OrdersViewHolder>(DIFF_CALLBACK) {
    private var loadingPosition: Int? = null

    fun setLoadingPosition(pos: Int?) {
        loadingPosition = pos
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        OrdersViewHolder(
            OrderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    inner class OrdersViewHolder(private val itemBinding: OrderItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root), PopupMenu.OnMenuItemClickListener {

        init {
            itemBinding.btnMore.setOnClickListener {
                val popupMenu = PopupMenu(context, itemBinding.btnMore)
                popupMenu.inflate(R.menu.order_item_menu)
                popupMenu.setOnMenuItemClickListener(this)
                popupMenu.show()
            }
        }

        @SuppressLint("SetTextI18n", "StringFormatMatches")
        fun bind(order: Order?) =
            with(itemBinding) {
                if (order == null) return@with
                var date = formatDate(order.createdAt)
                tvOrderNumber.text = context.getString(
                    R.string.order_info,
                    order.number,
                    date.first,
                    date.second
                )

                ivProductImage.load(order.productPreview) {
                    transformations(
                        RoundedCornersTransformation(
                            itemView.resources.getDimension(R.dimen.preview_corner_radius)
                        )
                    )
                    placeholder(R.drawable.img_logo)
                    error(R.drawable.img_logo)
                }
                ivProductImage.imageAlpha = DEFAULT_IMAGE_ALPHA

                tvDeliveryAddress.text = context.getString(
                    R.string.order_address,
                    order.deliveryAddress
                )

                date = formatDate(order.etd)
                tvEtd.text = context.getString(
                    R.string.order_date,
                    date.first,
                    date.second
                )

                itemBinding.btnMore.visibility = View.INVISIBLE

                when(order.status) {
                    "in_work" -> {
                        if (bindingAdapterPosition == loadingPosition) {
                            btnMore.visibility = View.GONE
                            progressBarContainer.visibility = View.VISIBLE
                        } else {
                            btnMore.visibility = View.VISIBLE
                            progressBarContainer.visibility = View.GONE
                        }
                        tvOrderStatus.text = itemView.resources.getString(R.string.order_status_in_work)
                    }
                    "done" ->
                        tvOrderStatus.text = itemView.resources.getString(R.string.order_status_done)
                    "cancelled" -> {
                        ivProductImage.imageAlpha = CANCELLED_IMAGE_ALPHA
                        tvDeliveryAddress.visibility = View.GONE
                        tvOrderStatus.text = context.getString(R.string.order_status_cancelled)
                        tvOrderStatus.setTextColor(context.getColor(R.color.error))
                        tvEtd.text = context.getString(
                            R.string.order_cancelled_info,
                            date.first,
                            date.second
                        )
                    }
                }

                tvOrderedProductName.text = "${order.productQuantity} × ${order.productSize} •"
            }

        override fun onMenuItemClick(item: MenuItem): Boolean {
            return when(item.itemId) {
                R.id.btnOrderCancel -> {
                    loadingPosition = bindingAdapterPosition
                    notifyItemChanged(bindingAdapterPosition)
                    snapshot()[position]?.let { onButtonCancelClickListener(it) }
                    true
                }
                else -> false
            }
        }
    }

    companion object {
        private const val DEFAULT_IMAGE_ALPHA = 255
        private const val CANCELLED_IMAGE_ALPHA = 50

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean =
                oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean =
                oldItem == newItem
        }
    }
}