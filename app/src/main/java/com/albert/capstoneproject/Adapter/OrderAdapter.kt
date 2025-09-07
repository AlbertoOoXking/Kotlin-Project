package com.albert.capstoneproject.Adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.albert.capstoneproject.Data.model.Order
import com.albert.capstoneproject.FireBase.FireBaseViewModel
import com.albert.capstoneproject.databinding.OrderListItemBinding
import java.time.format.DateTimeFormatter

/**
 * Adapter for displaying a list of orders in a RecyclerView.
 *
 * @param model Instance of FireBaseViewModel used to access current user information.
 * @param orders List of orders to display in the RecyclerView.
 */
class OrderAdapter(
    private val model: FireBaseViewModel,
    private var orders: List<Order>,
) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    /**
     * ViewHolder class for holding the views of each order item.
     *
     * @param binding Binding object for the order item layout.
     */
    inner class ViewHolder(val binding: OrderListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * Inflates the layout for each order item and returns a ViewHolder.
     *
     * @param parent The parent ViewGroup into which the new View will be added.
     * @param viewType The type of view to create.
     * @return A new ViewHolder instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderAdapter.ViewHolder {
        val binding =
            OrderListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    /**
     * Returns the number of orders in the list.
     *
     * @return The number of items in the order list.
     */
    override fun getItemCount(): Int {
        return orders.size
    }

    /**
     * Binds the data for a specific order item to the ViewHolder.
     *
     * @param holder The ViewHolder which should be updated.
     * @param position The position of the item within the adapter's data set.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = orders[position]

        // Format the order date for display.
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        val formattedDateTime = item.orderDate.format(formatter)

        // Check if the order belongs to the current user.
        if (item.user == model.currentUser.value!!.email) {
            // Set order details in the UI.
            holder.binding.tvOrderId.text = item.orderId.toString()
            holder.binding.tvDate.text = formattedDateTime
            holder.binding.tvTotalPrice.text = item.totalPrice.toString()

            // Set up the RecyclerView adapter for the ordered products.
            holder.binding.rvProductsOrderListItem.adapter =
                OrderedProductsAdapter(model, item.items)

            // Set up click listener for expanding/collapsing order details.
            holder.binding.root.setOnClickListener {
                holder.binding.root.animate().apply {
                    // If the order details are visible, collapse them.
                    if (holder.binding.rvProductsOrderListItem.visibility == View.VISIBLE) {
                        holder.binding.rvProductsOrderListItem.animate()
                            .alpha(0f)
                            .scaleY(0f)
                            .scaleX(0.9f)
                            .setDuration(800)
                            .withEndAction {
                                holder.binding.rvProductsOrderListItem.visibility = View.GONE

                                // Reset margin when collapsed
                                val params =
                                    holder.binding.root.layoutParams as ViewGroup.MarginLayoutParams
                                params.setMargins(0, 0, 0, 0) // No margin when collapsed
                                holder.binding.root.layoutParams = params

                                holder.binding.root.animate()
                                    .scaleX(0.9f)
                                    .scaleY(0.9f)
                                    .setDuration(300)
                                    .start()
                            }
                            .start()
                    } else {
                        // If the order details are not visible, expand them.
                        holder.binding.rvProductsOrderListItem.apply {
                            visibility = View.VISIBLE
                            alpha = 0f
                            scaleY = 0f
                            scaleX = 0.9f
                        }

                        // Add margin when expanded
                        val params =
                            holder.binding.root.layoutParams as ViewGroup.MarginLayoutParams
                        params.setMargins(
                            0,
                            24,
                            0,
                            24
                        ) // Adds vertical margin between items when expanded
                        holder.binding.root.layoutParams = params

                        holder.binding.root.animate()
                            .scaleX(1.05f)
                            .scaleY(1.05f)
                            .setDuration(800)
                            .start()

                        holder.binding.rvProductsOrderListItem.animate()
                            .alpha(1f)
                            .scaleY(1f)
                            .scaleX(1f)
                            .setDuration(800)
                            .withEndAction {
                                holder.binding.root.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(300)
                                    .start()
                            }
                            .start()
                    }
                }
            }
        }
    }
}