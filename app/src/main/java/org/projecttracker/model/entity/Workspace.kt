package org.projecttracker.model.entity

import org.projecttracker.model.db.AppDatabase
import com.raizlabs.android.dbflow.annotation.*

@Table(database = AppDatabase::class)
data class Workspace(
    @PrimaryKey @Unique var id: Long = -1,
    @Column var name: String = "",
    @ForeignKey(stubbedRelationship = true) var user: User? = null
)