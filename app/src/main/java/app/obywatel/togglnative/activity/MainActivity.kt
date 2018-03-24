package app.obywatel.togglnative.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import app.obywatel.togglnative.R
import app.obywatel.togglnative.repository.AppDatabase
import com.raizlabs.android.dbflow.config.DatabaseConfig
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDatabase()
    }

    override fun onResume() {
        super.onResume()
        startActivity(UserActivity.newIntent(this))
    }

    private fun initDatabase() {

        val databaseConfig = DatabaseConfig.builder(AppDatabase.javaClass)
                .databaseName("AppDatabase")
                .build();

        val flowConfig = FlowConfig.builder(this)
                .addDatabaseConfig(databaseConfig)
                .build()

        FlowManager.init(flowConfig)
    }
}
