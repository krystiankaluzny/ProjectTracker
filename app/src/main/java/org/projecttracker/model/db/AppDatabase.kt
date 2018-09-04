package org.projecttracker.model.db

import org.projecttracker.model.entity.Project
import org.projecttracker.model.entity.User
import com.raizlabs.android.dbflow.annotation.Database
import com.raizlabs.android.dbflow.annotation.Migration
import com.raizlabs.android.dbflow.sql.SQLiteType
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration

@Database(version = 7, foreignKeyConstraintsEnforced = true)
object AppDatabase {

    @Migration(version = 2, database = AppDatabase::class)
    class AddFullNameToUser : AlterTableMigration<User>(User::class.java) {

        override fun onPreMigrate() {
            addColumn(SQLiteType.TEXT, "fullName")
        }
    }

    @Migration(version = 5, database = AppDatabase::class)
    class AddColorIdToProject : AlterTableMigration<Project>(Project::class.java) {

        override fun onPreMigrate() {
            addColumn(SQLiteType.TEXT, "colorId")
        }
    }

    @Migration(version = 6, database = AppDatabase::class)
    class AddActiveWorkspaceIdToUser : AlterTableMigration<User>(User::class.java) {

        override fun onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "activeWorkspaceId")
        }
    }

    @Migration(version = 7, database = AppDatabase::class)
    class AddLastSynchronizationTimeToUser : AlterTableMigration<User>(User::class.java) {

        override fun onPreMigrate() {
            addColumn(SQLiteType.TEXT, "lastSynchronizationTime")
        }
    }
}