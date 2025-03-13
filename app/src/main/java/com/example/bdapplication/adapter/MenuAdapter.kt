package com.example.bdapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bdapplication.databinding.MenuCardBinding
import com.example.bdapplication.model.Menu

class MenuAdapter(private val context: Context, private val menuList: List<Menu>) :
    RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    class ViewHolder(val context: Context, val binding: MenuCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            context,
            MenuCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menu = menuList[position]

        holder.binding.tvMenuId.text = menu.id.toString()
        holder.binding.tvMenuName.text = menu.name
        holder.binding.tvMenuPrice.text = menu.price.toString()
    }
}
