package org.projecttracker.model.service

import org.projecttracker.model.entity.Project
import org.projecttracker.model.entity.TimeEntry
import org.projecttracker.model.entity.User
import org.projecttracker.model.entity.Workspace
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId

fun org.ktoggl.entity.User.toEntity() =
    User(
        id = id,
        apiToken = apiToken,
        fullName = fullName,
        selected = false,
        activeWorkspaceId = defaultWorkspaceId
    )

fun org.ktoggl.entity.Workspace.toEntity(user: User) =
    Workspace(
        id = id,
        name = name,
        user = user
    )

fun org.ktoggl.entity.Project.toEntity(workspace: Workspace) =
    Project(
        id = id,
        name = name,
        colorId = colorId,
        color = color,
        workspace = workspace
    )

fun org.ktoggl.entity.DetailedTimeEntry.toEntity(project: Project) =
    TimeEntry(
        id = id,
        description = description ?: project.name,
        startDateTime = OffsetDateTime.ofInstant(Instant.ofEpochSecond(startTimestamp), ZoneId.systemDefault()),
        endDateTime = endTimestamp?.let { OffsetDateTime.ofInstant(Instant.ofEpochSecond(it), ZoneId.systemDefault()) },
        duration = durationMillis?.let { Duration.ofMillis(it) },
        lastUpdateDateTime = OffsetDateTime.ofInstant(Instant.ofEpochSecond(lastUpdateTimestamp), ZoneId.systemDefault()),
        synchronized = false,
        project = project
    )