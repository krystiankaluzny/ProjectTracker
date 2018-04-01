package app.obywatel.togglnative

import android.content.Context
import android.support.multidex.MultiDexApplication
import app.obywatel.togglnative.di.*
import app.obywatel.togglnative.model.entity.User
import app.obywatel.togglnative.model.repository.AppDatabase
import com.raizlabs.android.dbflow.config.DatabaseConfig
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager

class TogglNativeApp : MultiDexApplication() {

    private lateinit var applicationComponent: ApplicationComponent
    private var userComponent: UserComponent? = null

    companion object {
        fun instance(context: Context) = context.applicationContext as TogglNativeApp

        fun getAppComponent(context: Context) = instance(context).applicationComponent

        fun getUserComponent(context: Context) = instance(context).userComponent!!

        fun createUserComponent(context: Context, user: User) {
            val app = instance(context)
            app.userComponent = app.applicationComponent
                .plus(UserModule(user))
        }

        fun releaseUserComponent(context: Context) {
            instance(context).userComponent = null
        }
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