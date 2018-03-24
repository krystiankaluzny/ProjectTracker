package app.obywatel.togglnative.repository

import com.raizlabs.android.dbflow.annotation.NotNull
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.annotation.Unique

@Table(database = AppDatabase::class, allFields = true)
public class User(
        @PrimaryKey @Unique @NotNull var id: Int? = null,
        @Unique @NotNull var apiToken: String? = null
)