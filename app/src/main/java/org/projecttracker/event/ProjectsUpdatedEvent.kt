package org.projecttracker.event

import org.projecttracker.model.entity.Project

data class ProjectsUpdatedEvent(
    val projects: List<Project>
)