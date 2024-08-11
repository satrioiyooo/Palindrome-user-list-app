package com.example.suitmedia.thirdscreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.suitmedia.R

class UserAdaptor(
    private val context: Context,
    private val onItemClick: (DataItem) -> Unit
) : RecyclerView.Adapter<UserAdaptor.ViewHolder>() {

    private val users: ArrayList<DataItem> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: DataItem = users[position]

        holder.name.text = "${user.firstName} ${user.lastName}"
        holder.email.text = user.email

        Glide.with(context)
            .load(user.avatar)
            .placeholder(R.drawable.ic_user_placeholder)
            .into(holder.avatar)

        holder.itemView.setOnClickListener {
            onItemClick(user)
        }
    }

    override fun getItemCount(): Int = users.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar: ImageView = itemView.findViewById(R.id.imageView_singleAllNews)
        val name: TextView = itemView.findViewById(R.id.username)
        val email: TextView = itemView.findViewById(R.id.email)
    }

    fun clear() {
        users.clear()
        notifyDataSetChanged()
    }

    fun addAll(newUsers: List<DataItem?>) {
        users.addAll(newUsers.filterNotNull())
        notifyDataSetChanged()
    }
}