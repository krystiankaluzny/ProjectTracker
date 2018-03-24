package app.obywatel.togglnative.ui.user

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import app.obywatel.togglnative.R
import app.obywatel.togglnative.ui.inflate
import kotlinx.android.synthetic.main.user_row.view.*


class UserAdapter(private val userViews: List<UserView>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun getItemCount() = userViews.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(userViews[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.user_row))

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(userView: UserView) = with(view) {
            userIdTextView.text = userView.id.toString()
            userNameTextView.text = userView.name
        }
    }

}
