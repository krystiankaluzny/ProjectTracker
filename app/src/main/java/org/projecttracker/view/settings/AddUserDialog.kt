package org.projecttracker.view.settings

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.user_add_by_api_token.view.*
import org.projecttracker.R

class AddUserDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val settingsActivity = activity as SettingsActivity
        val usersViewModel = settingsActivity.userViewModel

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val addUserContentView: View = settingsActivity.layoutInflater.inflate(R.layout.user_add_by_api_token, null)
        val editText: EditText = addUserContentView.apiToken

        builder.setView(addUserContentView)
            .setPositiveButton(R.string.ok, { _, _ ->
                val apiToken: String = editText.text.toString()
                usersViewModel.addUserByApiToken(apiToken)
            })
            .setNegativeButton(R.string.cancel, null)

        return builder.create()
    }
}