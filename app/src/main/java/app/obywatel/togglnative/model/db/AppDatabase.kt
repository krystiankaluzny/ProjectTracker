package app.obywatel.togglnative.model.db

import app.obywatel.togglnative.model.entity.User
import com.raizlabs.android.dbflow.annotation.Database
import com.raizlabs.android.dbflow.annotation.Migration
import com.raizlabs.android.dbflow.sql.SQLiteType
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration

@Database(version = 2, foreignKeyConstraintsEnforced = true)
object AppDatabase {

    @Migration(version = 2, database = AppDatabase::class)
    class AddFullNameToUser : AlterTableMigration<User>(User::class.java) {

        override fun onPreMigrate() {
            addColumn(SQLiteType.TEXT, "fullName")
        }
    }
}