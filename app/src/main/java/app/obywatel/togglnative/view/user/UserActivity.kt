package app.obywatel.togglnative.view.user

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import app.obywatel.togglnative.R
import app.obywatel.togglnative.TogglNativeApp
import app.obywatel.togglnative.databinding.UserActivityBinding
import app.obywatel.togglnative.di.UserViewModelModule
import app.obywatel.togglnative.model.entity.User
import app.obywatel.togglnative.model.service.user.UserSelectionService
import app.obywatel.togglnative.view.BaseActivity
import app.obywatel.togglnative.view.timer.TimerActivity
import app.obywatel.togglnative.viewmodel.user.SelectUserListener
import app.obywatel.togglnative.viewmodel.user.UserViewModel
import kotlinx.android.synthetic.main.user_activity.*
import kotlinx.android.synthetic.main.user_activity_content.*
import javax.inject.Inject

class UserActivity : BaseActivity(), SelectUserListener {

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

        setUpUserList()
        setUpViewListeners()
    }

    override fun onResume() {
        super.onResume()
        TogglNativeApp.releaseUserComponent()
        val selectedUser = userSelectionService.getSelectedUser()
        when {
            selectedUser != null -> {} //startTimerActivity(selectedUser)
            else -> {
                userViewModel.updateUserListeners += userAdapter
                userViewModel.selectUserListeners += this
            }
        }
    }

    override fun onPause() {
        super.onPause()
        userViewModel.updateUserListeners -= userAdapter
        userViewModel.selectUserListeners -= this
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onUserSelected(user: User) {

        startTimerActivity(user)
    }

    override fun inject() {
        TogglNativeApp.getAppComponent()
            .plus(UserViewModelModule())
            .inject(this)
    }

    override fun setUpBinding() {
        val binding: UserActivityBinding = DataBindingUtil.setContentView(this, R.layout.user_activity)
        binding.viewModel = userViewModel
    }

    private fun setUpUserList() {
        userAdapter = UserAdapter(userViewModel)

        val linearLayoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, linearLayoutManager.orientation)

        recycleView.layoutManager = linearLayoutManager
        recycleView.addItemDecoration(dividerItemDecoration)
        recycleView.adapter = userAdapter
    }

    private fun setUpViewListeners() {
        addUserButton.setOnClickListener { showAddUserDialog() }
    }

    private fun startTimerActivity(user: User) {
        TogglNativeApp.createUserComponent(user)
        startActivity(TimerActivity.newIntent(this))
    }

    private fun showAddUserDialog() {
        val addUserDialog = AddUserDialog()

        addUserDialog.show(fragmentManager, "Add User")
    }
}
