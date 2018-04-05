package app.obywatel.togglnative.model.entity

import app.obywatel.togglnative.model.db.AppDatabase
import com.raizlabs.android.dbflow.annotation.*

@Table(database = AppDatabase::class)
class Project(
    @PrimaryKey @Unique var id: Long = -1L,
    @Column var name: String = "",
    @Column var color: Int = -1,
    @ForeignKey(stubbedRelationship = true) var workspace: Workspace? = null
)