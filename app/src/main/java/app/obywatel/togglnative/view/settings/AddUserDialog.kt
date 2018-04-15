package app.obywatel.togglnative.view.settings

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.View
import android.widget.EditText
import app.obywatel.togglnative.R
import kotlinx.android.synthetic.main.user_add_by_api_token.view.*

class AddUserDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val usersViewModel = (activity as SettingsActivity).userViewModel

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val addUserContentView: View = activity.layoutInflater.inflate(R.layout.user_add_by_api_token, null)
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