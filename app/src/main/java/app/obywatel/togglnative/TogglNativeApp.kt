package app.obywatel.togglnative

import android.app.Application
import app.obywatel.togglnative.model.repository.AppDatabase
import com.raizlabs.android.dbflow.config.DatabaseConfig
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager

class TogglNativeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initDatabase()
    }

    private fun initDatabase() {

        val databaseConfig = DatabaseConfig.builder(AppDatabase.javaClass)
                .databaseName("AppDatabase")
                .build()

        val flowConfig = FlowConfig.builder(this)
                .addDatabaseConfig(databaseConfig)
                .build()

        FlowManager.init(flowConfig)
    }
}