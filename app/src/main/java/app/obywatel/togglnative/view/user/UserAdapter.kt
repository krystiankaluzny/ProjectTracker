package app.obywatel.togglnative.view.user

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.obywatel.togglnative.R
import app.obywatel.togglnative.databinding.UserRowBinding
import app.obywatel.togglnative.view.bind
import app.obywatel.togglnative.viewmodel.user.UserViewModel


class UserAdapter(private val userViewModel: UserViewModel) : RecyclerView.Adapter<UserAdapter.ViewHolder>(), UserViewModel.Listener {

    init {
        userViewModel.addListener(this)
    }

    override fun getItemCount() = userViewModel.userCount()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.bind(R.layout.user_row))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.viewModel = userViewModel.singleUserViewModel(position)
    }

    override fun usersUpdated() {
        notifyItemInserted(userViewModel.userCount() - 1)
    }

    class ViewHolder(val binding: UserRowBinding) : RecyclerView.ViewHolder(binding.content)
}
