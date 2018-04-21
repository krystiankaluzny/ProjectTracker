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
import app.obywatel.togglnative.view.BaseActivity
import app.obywatel.togglnative.viewmodel.user.UserViewModel
import app.obywatel.togglnative.viewmodel.user.WorkspaceViewModel
import kotlinx.android.synthetic.main.settings_activity.*
import kotlinx.android.synthetic.main.settings_activity_content.*
import kotlinx.android.synthetic.main.timer_activity_content.*
import javax.inject.Inject

class SettingsActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }

    @Inject lateinit var userViewModel: UserViewModel

    private lateinit var userAdapter: UserAdapter
    private lateinit var workspaceAdapter: WorkspaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpUserSpinner()
        setUpWorkspaceSpinner()
        setUpViewListeners()
    }

    override fun onResume() {
        super.onResume()
        userViewModel.updateUserListeners += userAdapter
    }

    override fun onPause() {
        super.onPause()
        userViewModel.updateUserListeners -= userAdapter
    }

    override fun onBackPressed() {
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
    }

    override fun setUpBinding() {
        val binding: SettingsActivityBinding = DataBindingUtil.setContentView(this, R.layout.settings_activity)
        binding.viewModel = userViewModel
    }

    private fun setUpUserSpinner() {
        userAdapter = UserAdapter(this, userViewModel)
        usersSpinner.adapter = userAdapter
    }

    private fun setUpWorkspaceSpinner() {
        workspaceAdapter = WorkspaceAdapter(this, userViewModel.workspaceViewModel)
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
