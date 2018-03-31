package app.obywatel.togglnative.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import app.obywatel.togglnative.R
import app.obywatel.togglnative.view.user.UserActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

    }

    override fun onResume() {
        super.onResume()
        startActivity(UserActivity.newIntent(this))
    }


}
