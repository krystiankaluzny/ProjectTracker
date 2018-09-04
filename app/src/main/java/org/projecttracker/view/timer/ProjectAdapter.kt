package org.projecttracker.view.timer

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import org.projecttracker.R
import org.projecttracker.databinding.ProjectRowBinding
import org.projecttracker.view.bind
import org.projecttracker.viewmodel.timer.DailyTimerViewModel
import org.projecttracker.viewmodel.timer.UpdateProjectsListener

class ProjectAdapter(private val dailyTimerViewModel: DailyTimerViewModel)
    : RecyclerView.Adapter<ProjectAdapter.ViewHolder>(), UpdateProjectsListener {

    override fun getItemCount() = dailyTimerViewModel.projectsCount()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.bind(R.layout.project_row))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.viewModel = dailyTimerViewModel.singleProjectViewModel(position)
    }

    override fun onUpdateProjects() {
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ProjectRowBinding) : RecyclerView.ViewHolder(binding.content)
}