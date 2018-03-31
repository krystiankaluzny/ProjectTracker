package app.obywatel.togglnative.view.user

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import app.obywatel.togglnative.R
import app.obywatel.togglnative.TogglNativeApp
import app.obywatel.togglnative.databinding.UserActivityBinding
import app.obywatel.togglnative.di.UserViewModelModule
import app.obywatel.togglnative.viewmodel.user.UserViewModel
import kotlinx.android.synthetic.main.user_activity.*
import kotlinx.android.synthetic.main.user_app_bar.*
import javax.inject.Inject

class UserActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    internal lateinit var userViewModel: UserViewModel

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, UserActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TogglNativeApp.component(this)
                .plus(UserViewModelModule())
                .inject(this)

        val binding: UserActivityBinding = DataBindingUtil.setContentView(this, R.layout.user_activity)
        binding.viewModel = userViewModel

        setSupportActionBar(toolbar)

        addUserButton.setOnClickListener { showAddUserDialog() }

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

        val userAdapter = UserAdapter(userViewModel)

        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = userAdapter
    }

    private fun showAddUserDialog() {
        val addUserDialog = AddUserDialog()

        addUserDialog.show(fragmentManager, "Add User")
    }
}
