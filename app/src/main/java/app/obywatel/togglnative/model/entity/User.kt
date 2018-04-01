package app.obywatel.togglnative.model.entity

import app.obywatel.togglnative.model.repository.AppDatabase
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.annotation.Unique

@Table(database = AppDatabase::class, useBooleanGetterSetters = false)
class User(
    @PrimaryKey @Unique var id: Long = -1L,
    @Unique @Column var apiToken: String = "",
    @Column var selected: Boolean = false
)