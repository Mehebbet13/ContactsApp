package com.vholodynskyi.assignment.presentation.contactslist

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vholodynskyi.assignment.databinding.ItemContactListBinding
import com.vholodynskyi.assignment.domain.model.DbContact
import com.vholodynskyi.assignment.presentation.details.DetailsFragment.Companion.BLANK

class ContactAdapter(
    private val context: Activity,
    private val onItemClicked: ItemClick
) : RecyclerView.Adapter<ViewHolder>() {

    var items: List<DbContact?> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun removeAt(index: Int) {
        items.toMutableList().removeAt(index)
        notifyItemRemoved(index)
    }
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(
            ItemContactListBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            text.text = StringBuilder().append(items[position]?.firstName).append(BLANK).append(items[position]?.lastName)
            root.setOnClickListener {
                item?.let { contact -> onItemClicked(contact.id) }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class ViewHolder(val binding: ItemContactListBinding) : RecyclerView.ViewHolder(binding.root)

typealias ItemClick = (Int) -> Unit
