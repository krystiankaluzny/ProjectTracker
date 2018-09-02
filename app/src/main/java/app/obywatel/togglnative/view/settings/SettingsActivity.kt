package app.obywatel.togglnative.view.settings

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.view.GravityCompat
import app.obywatel.togglnative.R
import app.obywatel.togglnative.TogglNativeApp
import app.obywatel.togglnative.databinding.SettingsActivityBinding
import app.obywatel.togglnative.di.UserViewModelModule
import app.obywatel.togglnative.model.entity.User
import app.obywatel.togglnative.view.BaseActivity
import app.obywatel.togglnative.viewmodel.user.SelectUserListener
import app.obywatel.togglnative.viewmodel.user.UserViewModel
import app.obywatel.togglnative.viewmodel.user.WorkspaceViewModel
import kotlinx.android.synthetic.main.settings_activity.*
import kotlinx.android.synthetic.main.settings_activity_content.*
import org.slf4j.LoggerFactory
import javax.inject.Inject

class SettingsActivity : BaseActivity(), SelectUserListener {

    companion object {
        private val logger = LoggerFactory.getLogger(SettingsActivity::class.java)
        fun newIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }

    @Inject lateinit var userViewModel: UserViewModel
    private lateinit var workspaceViewModel: WorkspaceViewModel

    private lateinit var userAdapter: UserAdapter
    private lateinit var workspaceAdapter: WorkspaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        logger.trace("on create start")
        super.onCreate(savedInstanceState)

        setUpUserSpinner()
        setUpWorkspaceSpinner()
        setUpViewListeners()

        userViewModel.updateUserListeners += userAdapter
        userViewModel.selectUserListeners += this
        workspaceViewModel.updateWorkspacesListeners += workspaceAdapter
        userViewModel.showSelectedUser()
    }

    override fun onResume() {
        logger.trace("on resume start")
        super.onResume()
        userViewModel.updateUserListeners += userAdapter
        workspaceViewModel.updateWorkspacesListeners += workspaceAdapter
    }

    override fun onPause() {
        logger.trace("on pause start")
        super.onPause()
        userViewModel.updateUserListeners -= userAdapter
        workspaceViewModel.updateWorkspacesListeners -= workspaceAdapter
    }

    override fun onBackPressed() {
        logger.trace("on back pressed start")
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun inject() {
        TogglNativeApp.getAppComponent()
            .plus(UserViewModelModule())
            .inject(this)

        workspaceViewModel = userViewModel.workspaceViewModel
    }

    override fun setUpBinding() {
        val binding: SettingsActivityBinding = DataBindingUtil.setContentView(this, R.layout.settings_activity)
        binding.userViewModel = userViewModel
        binding.workspaceViewModel = workspaceViewModel
    }

    override fun onSelectUser(user: User) {
        TogglNativeApp.createUserComponent(user)
    }

    private fun setUpUserSpinner() {
        userAdapter = UserAdapter(this, userViewModel)
        usersSpinner.adapter = userAdapter
    }

    private fun setUpWorkspaceSpinner() {
        workspaceAdapter = WorkspaceAdapter(this, workspaceViewModel)
        workspaceSpinner.adapter = workspaceAdapter
    }

    private fun setUpViewListeners() {
        addUserButton.setOnClickListener { showAddUserDialog() }
    }

    private fun showAddUserDialog() {
        val addUserDialog = AddUserDialog()

        addUserDialog.show(fragmentManager, "Add User")
    }
}
