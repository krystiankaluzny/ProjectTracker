package app.obywatel.togglnative

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDexApplication
import app.obywatel.togglnative.di.ApplicationComponent
import app.obywatel.togglnative.di.ApplicationModule
import app.obywatel.togglnative.di.DaggerApplicationComponent
import app.obywatel.togglnative.model.repository.AppDatabase
import com.raizlabs.android.dbflow.config.DatabaseConfig
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager


class TogglNativeApp : MultiDexApplication() {

    lateinit var applicationComponent: ApplicationComponent

    companion object {
        fun component(context: Context): ApplicationComponent = (context.applicationContext as TogglNativeApp).applicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        initComponent()
        initDatabase()
    }

    private fun initComponent() {

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
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