package org.projecttracker.view.settings

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.view.GravityCompat
import org.projecttracker.R
import org.projecttracker.ProjectTrackerApp
import org.projecttracker.databinding.SettingsActivityBinding
import org.projecttracker.di.UserViewModelModule
import org.projecttracker.view.BaseActivity
import org.projecttracker.viewmodel.user.UserViewModel
import org.projecttracker.viewmodel.user.WorkspaceViewModel
import kotlinx.android.synthetic.main.settings_activity.*
import kotlinx.android.synthetic.main.settings_activity_content.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.projecttracker.event.UserAddedEvent
import org.projecttracker.event.UserSelectedEvent
import org.slf4j.LoggerFactory
import javax.inject.Inject

class SettingsActivity : BaseActivity() {

    companion object {
        private val logger = LoggerFactory.getLogger(SettingsActivity::class.java)
        fun newIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }

    @Inject
    lateinit var userViewModel: UserViewModel
    private lateinit var workspaceViewModel: WorkspaceViewModel

    private lateinit var userAdapter: UserAdapter
    private lateinit var workspaceAdapter: WorkspaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        logger.trace("on create start")
        super.onCreate(savedInstanceState)

        setUpUserSpinner()
        setUpWorkspaceSpinner()
        setUpViewListeners()
    }

    override fun onStart() {
        super.onStart()

        EventBus.getDefault().register(this)
        EventBus.getDefault().register(userAdapter)
        EventBus.getDefault().register(workspaceViewModel)
        EventBus.getDefault().register(workspaceAdapter)

        userViewModel.showSelectedUser()
    }

    override fun onResume() {
        logger.trace("on resume start")
        super.onResume()
    }

    override fun onPause() {
        logger.trace("on pause start")
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
        EventBus.getDefault().unregister(workspaceAdapter)
        EventBus.getDefault().unregister(workspaceViewModel)
        EventBus.getDefault().unregister(userAdapter)
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
        ProjectTrackerApp.getAppComponent()
            .plus(UserViewModelModule())
            .inject(this)

        workspaceViewModel = userViewModel.workspaceViewModel
    }

    override fun setUpBinding() {
        val binding: SettingsActivityBinding = DataBindingUtil.setContentView(this, R.layout.settings_activity)
        binding.userViewModel = userViewModel
        binding.workspaceViewModel = workspaceViewModel
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUserSelected(userSelectedEvent: UserSelectedEvent) {
        ProjectTrackerApp.createUserComponent(userSelectedEvent.user)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUserAdded(userAddedEvent: UserAddedEvent) {
        if (userAddedEvent.position == 0) {
            ProjectTrackerApp.createUserComponent(userAddedEvent.user)
        }
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
