package app.obywatel.togglnative.view.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckedTextView
import app.obywatel.togglnative.viewmodel.user.UpdateWorkspacesListener
import app.obywatel.togglnative.viewmodel.user.WorkspaceViewModel

class WorkspaceAdapter(context: Context, private val workspaceViewModel: WorkspaceViewModel) : BaseAdapter(), UpdateWorkspacesListener {

    companion object {
        private const val resource = android.R.layout.simple_spinner_dropdown_item
    }

    private val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val textView: CheckedTextView = (convertView ?: inflater.inflate(resource, parent, false)) as CheckedTextView

        textView.text = getItem(position)

        return textView
    }

    override fun getItem(position: Int): String = workspaceViewModel.getWorkspaceName(position)

    override fun getItemId(position: Int): Long = workspaceViewModel.getWorkspaceId(position)

    override fun getCount(): Int = workspaceViewModel.getWorkspacesCount()

    override fun onUpdateWorkspaces() = notifyDataSetChanged()
}