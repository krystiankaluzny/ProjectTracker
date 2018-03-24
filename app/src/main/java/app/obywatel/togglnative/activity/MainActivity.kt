package app.obywatel.togglnative.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import app.obywatel.togglnative.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onResume() {
        super.onResume()
        startActivity(UserActivity.newIntent(this))
    }
}
