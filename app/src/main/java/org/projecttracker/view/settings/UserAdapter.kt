package org.projecttracker.view.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckedTextView
import org.projecttracker.model.entity.User
import org.projecttracker.viewmodel.user.UpdateUserListener
import org.projecttracker.viewmodel.user.UserViewModel

class UserAdapter(context: Context, private val userViewModel: UserViewModel) : BaseAdapter(), UpdateUserListener {

    companion object {
        private const val resource = android.R.layout.simple_spinner_dropdown_item
    }

    private val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val textView: CheckedTextView = (convertView ?: inflater.inflate(resource, parent, false)) as CheckedTextView

        textView.text = getItem(position)

        return textView
    }

    override fun getItem(position: Int): String = userViewModel.getUserName(position)

    override fun getItemId(position: Int): Long = userViewModel.getUserId(position)

    override fun getCount(): Int = userViewModel.userCount()

    override fun onAddUser(position: Int) {
        notifyDataSetChanged()
    }

    override fun onUpdateUser(position: Int) {
        notifyDataSetChanged()
    }

}
