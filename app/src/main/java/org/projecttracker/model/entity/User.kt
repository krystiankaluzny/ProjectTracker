package org.projecttracker.model.entity

import org.projecttracker.model.db.AppDatabase
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.annotation.Unique
import org.threeten.bp.OffsetDateTime

@Table(database = AppDatabase::class, useBooleanGetterSetters = false)
data class User(
    @PrimaryKey @Unique var id: Long = -1L,
    @Unique @Column var apiToken: String = "",
    @Column var fullName: String = "",
    @Column var selected: Boolean = false,
    @Column var activeWorkspaceId: Long = -1L,
    @Column var lastSynchronizationTime: OffsetDateTime? = null
)