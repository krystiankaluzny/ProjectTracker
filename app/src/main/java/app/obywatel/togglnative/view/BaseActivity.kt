package app.obywatel.togglnative.view

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.user_activity.*
import kotlinx.android.synthetic.main.user_activity_content.*

abstract class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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

        return true
    }
}