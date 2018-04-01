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
import app.obywatel.togglnative.model.entity.User
import app.obywatel.togglnative.model.service.user.UserSelectionService
import app.obywatel.togglnative.view.timer.TimerActivity
import app.obywatel.togglnative.viewmodel.user.SelectUserListener
import app.obywatel.togglnative.viewmodel.user.UserViewModel
import kotlinx.android.synthetic.main.user_activity.*
import kotlinx.android.synthetic.main.user_app_bar.*
import javax.inject.Inject

class UserActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SelectUserListener {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, UserActivity::class.java)
        }
    }

    @Inject lateinit var userViewModel: UserViewModel
    @Inject lateinit var userSelectionService: UserSelectionService

    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TogglNativeApp.getAppComponent(this)
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

    override fun onResume() {
        super.onResume()
        if (userSelectionService.getSelectedUser() != null) {
            startActivity(TimerActivity.newIntent(this))
        } else {
            userViewModel.addUserListeners += userAdapter
            userViewModel.selectUserListeners += this
        }
    }

    override fun onPause() {
        super.onPause()
        userViewModel.addUserListeners -= userAdapter
        userViewModel.selectUserListeners -= this
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        return true
    }

    override fun onUserSelected(user: User) {

        TogglNativeApp.createUserComponent(this, user)

        startActivity(TimerActivity.newIntent(this))
    }

    private fun initHamburgerButton() {
        val hamburgerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)
        drawerLayout.addDrawerListener(hamburgerToggle)
        hamburgerToggle.syncState()
    }

    private fun initUserList() {

        userAdapter = UserAdapter(userViewModel)

        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = userAdapter
    }

    private fun showAddUserDialog() {
        val addUserDialog = AddUserDialog()

        addUserDialog.show(fragmentManager, "Add User")
    }
}
