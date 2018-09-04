package org.projecttracker.model.entity

import org.projecttracker.model.db.AppDatabase
import com.raizlabs.android.dbflow.annotation.*
import org.threeten.bp.Duration
import org.threeten.bp.OffsetDateTime

@Table(database = AppDatabase::class)
data class TimeEntry(
    @PrimaryKey @Unique var id: Long = -1L,
    @Column var description: String = "",
    @Column var startDateTime: OffsetDateTime? = null,
    @Column var endDateTime: OffsetDateTime? = null,
    @Column var duration: Duration? = null,
    @Column var lastUpdateDateTime: OffsetDateTime? = null,
    @Column var synchronized: Boolean = false,
    @ForeignKey(stubbedRelationship = true) var project: Project? = null
)