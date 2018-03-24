package app.obywatel.togglnative.ui.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import app.obywatel.togglnative.R
import kotlinx.android.synthetic.main.user_activity.*
import kotlinx.android.synthetic.main.user_app_bar.*
import java.util.*

class UserActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val random = Random()
    private lateinit var userViews: MutableList<UserView>

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, UserActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_activity)
        setSupportActionBar(toolbar)

        addUserButton.setOnClickListener { addUser() }

        initHamburgerButton()
        initUserList()

        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initHamburgerButton() {
        val hamburgerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)
        drawerLayout.addDrawerListener(hamburgerToggle)
        hamburgerToggle.syncState()
    }

    private fun initUserList() {

        val userView1 = UserView(1, "Dupa")
        val userView2 = UserView(24, "Blada")

        userViews = mutableListOf(userView1, userView2)

        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = UserAdapter(userViews)

        Log.d("DUPA", "after init user list")
    }

    private fun addUser() {
        val userView = UserView(random.nextInt(100), "Sraka maka")
        userViews.add(userView)
        recycleView.adapter.notifyItemInserted(userViews.size)
    }
}
