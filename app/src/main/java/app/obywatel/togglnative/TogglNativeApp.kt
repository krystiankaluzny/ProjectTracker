package app.obywatel.togglnative

import android.support.multidex.MultiDexApplication
import app.obywatel.togglnative.di.*
import app.obywatel.togglnative.model.db.AppDatabase
import app.obywatel.togglnative.model.entity.User
import com.jakewharton.threetenabp.AndroidThreeTen
import com.raizlabs.android.dbflow.config.DatabaseConfig
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class TogglNativeApp : MultiDexApplication() {

    private lateinit var applicationComponent: ApplicationComponent
    private lateinit var userComponent: UserComponent

    companion object {
        var instance: TogglNativeApp by NotNullSingleValueVar()

        fun getAppComponent() = instance.applicationComponent

        fun getUserComponent() = instance.userComponent

        fun createUserComponent(user: User) {
            val app = instance
            app.userComponent = app.applicationComponent
                .plus(UserModule(user))
        }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        instance = this
        initDatabase()

        initComponents()
    }

    private fun initComponents() {

        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()

        val selectedUser = applicationComponent.userSelectionService().getSelectedUser()

        createUserComponent(selectedUser ?: User())
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

    private class NotNullSingleValueVar<T> : ReadWriteProperty<Any?, T> {

        private var value: T? = null
        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return value ?: throw IllegalStateException("${property.name} not initialized")
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            this.value = if (this.value == null) value
            else throw IllegalStateException("${property.name} already initialized")
        }
    }
}