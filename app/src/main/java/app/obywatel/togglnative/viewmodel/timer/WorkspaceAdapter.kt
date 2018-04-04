package app.obywatel.togglnative.viewmodel.timer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckedTextView

class WorkspaceAdapter(context: Context, private val timerViewModel: TimerViewModel) : BaseAdapter() {

    companion object {
        private const val resource = android.R.layout.simple_spinner_dropdown_item
    }

    private val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val textView: CheckedTextView = (convertView ?: inflater.inflate(resource, parent, false)) as CheckedTextView

        textView.text = getItem(position)

        return textView
    }

    override fun getItem(position: Int): String = timerViewModel.getWorkspaceName(position)

    override fun getItemId(position: Int): Long = timerViewModel.getWorkspaceId(position)

    override fun getCount(): Int = timerViewModel.getWorkspacesCount()
}