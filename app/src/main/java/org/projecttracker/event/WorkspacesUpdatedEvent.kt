package org.projecttracker.event

import org.projecttracker.model.entity.Workspace

data class WorkspacesUpdatedEvent(
    val workspaces: List<Workspace>
)