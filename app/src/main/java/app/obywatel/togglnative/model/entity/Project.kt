package app.obywatel.togglnative.model.entity

import app.obywatel.togglnative.model.db.AppDatabase
import com.raizlabs.android.dbflow.annotation.*

@Table(database = AppDatabase::class)
data class Project(
    @PrimaryKey @Unique var id: Long = -1L,
    @Column var name: String = "",
    @Column var colorId: Int = 0,
    @Column var color: Int = 0,
    @ForeignKey(stubbedRelationship = true) var workspace: Workspace? = null
)