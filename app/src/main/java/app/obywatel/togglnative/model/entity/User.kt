package app.obywatel.togglnative.model.entity

import app.obywatel.togglnative.model.repository.AppDatabase
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.annotation.Unique

@Table(database = AppDatabase::class, allFields = true, useBooleanGetterSetters = false)
class User(
        @PrimaryKey @Unique var id: Long = -1L,
        @Unique var apiToken: String = "",
        var selected: Boolean = false
)