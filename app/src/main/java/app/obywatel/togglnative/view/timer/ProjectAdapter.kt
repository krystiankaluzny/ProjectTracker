package app.obywatel.togglnative.view.timer

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.obywatel.togglnative.R
import app.obywatel.togglnative.databinding.ProjectRowBinding
import app.obywatel.togglnative.view.bind
import app.obywatel.togglnative.viewmodel.timer.DailyTimerViewModel

class ProjectAdapter(private val dailyTimerViewModel: DailyTimerViewModel) : RecyclerView.Adapter<ProjectAdapter.ViewHolder>() {

    override fun getItemCount() = dailyTimerViewModel.projectsCount()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.bind(R.layout.project_row))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.viewModel = dailyTimerViewModel.singleProjectViewModel(position)
    }

    class ViewHolder(val binding: ProjectRowBinding) : RecyclerView.ViewHolder(binding.content)
}