package app.obywatel.togglnative.view.user

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.obywatel.togglnative.R
import app.obywatel.togglnative.databinding.UserRowBinding
import app.obywatel.togglnative.view.bind
import app.obywatel.togglnative.viewmodel.user.UsersViewModel


class UserAdapter(private val usersViewModel: UsersViewModel) : RecyclerView.Adapter<UserAdapter.ViewHolder>(), UsersViewModel.Listener {

    init {
        usersViewModel.addListener(this)
    }

    override fun getItemCount() = usersViewModel.userCount()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.bind(R.layout.user_row))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.viewModel = usersViewModel.singleUserViewModel(position)
    }

    override fun usersUpdated() {
        notifyDataSetChanged()
    }

    override fun error(message: String?) {}

    class ViewHolder(val binding: UserRowBinding) : RecyclerView.ViewHolder(binding.content)
}
