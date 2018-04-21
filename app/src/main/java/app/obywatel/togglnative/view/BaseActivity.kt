package app.obywatel.togglnative.view

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import app.obywatel.togglnative.R
import app.obywatel.togglnative.TogglNativeApp
import app.obywatel.togglnative.view.settings.SettingsActivity
import app.obywatel.togglnative.view.timer.TimerActivity
import kotlinx.android.synthetic.main.settings_activity.*
import kotlinx.android.synthetic.main.settings_activity_content.*

abstract class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object { private const val TAG = "BaseActivity" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()
        setUpBinding()
        setUpToolbar()
        setUpNavBar()
    }

    protected abstract fun inject()

    protected abstract fun setUpBinding()

    protected open fun setUpToolbar() {

        setSupportActionBar(toolbar)

        val hamburgerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)
        drawerLayout.addDrawerListener(hamburgerToggle)
        hamburgerToggle.syncState()
    }

    protected open fun setUpNavBar() {

        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        Log.d(TAG, "onNavigationItemSelected: " + item.itemId)
        when (item.itemId) {
            R.id.navTimer -> {
                Log.d(TAG, "onNavigationItemSelected: navTimer")
                when {
                    this is TimerActivity -> drawerLayout.closeDrawer(GravityCompat.START)
                    else -> {
                        Log.d(TAG, "onNavigationItemSelected: is not timer")
                        startActivity(TimerActivity.newIntent(this))
                    }
                }
            }
            R.id.navSettings -> {
                Log.d(TAG, "onNavigationItemSelected: navSettings")
                when {
                    this is SettingsActivity -> drawerLayout.closeDrawer(GravityCompat.START)
                    else -> {
                        Log.d(TAG, "onNavigationItemSelected: is not settings")
                        startActivity(SettingsActivity.newIntent(this))
                    }
                }
            }
        }
        return true
    }
}