package com.ddt.smsalarm.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ddt.smsalarm.R
import com.ddt.smsalarm.data.db.FilterEntity
import com.ddt.smsalarm.databinding.ItemFilterListBinding
import javax.inject.Inject

class FilterAdapter @Inject constructor() :
    RecyclerView.Adapter<FilterAdapter.ViewHolder>() {

    private var filters = emptyList<FilterEntity>()

    private var deleteClickListener: ((FilterEntity: FilterEntity) -> Unit)? = null
    private var editClickListener: ((id: Int) -> Unit)? = null

    fun setOnClickListeners(
        deleteClickListener: ((FilterEntity: FilterEntity) -> Unit),
        editClickListener: ((id: Int) -> Unit)
    ) {
        this.deleteClickListener = deleteClickListener
        this.editClickListener = editClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemFilterListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = filters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            holder.setData(position)
            ivPopupMenu.setOnClickListener {
                setUpPopUpMenu(it, position)
            }
        }
    }

    inner class ViewHolder(val binding: ItemFilterListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setData(position: Int) {
            binding.apply {
                tvFilterText.text = filters[position].text
            }
        }
    }

    private fun setUpPopUpMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_edit -> {
                    editClickListener?.let { it(filters[position].id) }
                }
                R.id.item_delete -> {
                    deleteClickListener?.let { it(filters[position]) }
                }
            }
            true
        }
        popupMenu.show()
    }

    fun setNewList(newList: List<FilterEntity>) {
        val mentionDiffUtils = MentionDiffUtils(filters, newList)
        val diffUtils = DiffUtil.calculateDiff(mentionDiffUtils)
        filters = newList
        diffUtils.dispatchUpdatesTo(this)
    }

    class MentionDiffUtils(
        private val oldList: List<FilterEntity>, private val newList: List<FilterEntity>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] === newList[newItemPosition]
        }
    }
}