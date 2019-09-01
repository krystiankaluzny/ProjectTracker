package org.projecttracker.view.timer

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import kotlinx.android.synthetic.main.start_project_time_entry.view.*
import org.projecttracker.R
import org.threeten.bp.Duration
import org.threeten.bp.OffsetDateTime
import java.util.*

class StartProjectTimeEntryDialog : DialogFragment() {

    private val seekBarStep: Int = 5
    private val seekBarInitMinutes: Int = 10
    private val seekBarMaxMinutes: Int = 120

    private lateinit var textView: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val timerActivity = activity as TimerActivity
        val builder: AlertDialog.Builder = AlertDialog.Builder(timerActivity)
        val startProjectTimeEntry: View = timerActivity.layoutInflater.inflate(R.layout.start_project_time_entry, null)

        textView = startProjectTimeEntry.textView
        setUpSeekBar(startProjectTimeEntry.seekBar2)

        builder.setView(startProjectTimeEntry)
            .setPositiveButton(R.string.ok) { _, _ ->

            }
            .setNegativeButton(R.string.cancel, null)

        return builder.create()
    }

    private fun setUpSeekBar(seekBar: SeekBar) {

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                val minutesAgo = progress * seekBarStep
                val startDate = OffsetDateTime.now().minusMinutes(minutesAgo.toLong())

                setStartTime(startDate)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        seekBar.max = seekBarMaxMinutes / seekBarStep
        seekBar.progress = seekBarInitMinutes / seekBarStep
    }

    private fun setStartTime(startTime: OffsetDateTime) {

        val duration = Duration.between(startTime, OffsetDateTime.now())
        val durationMinutes = duration.toMinutes()

        textView.text = getString(R.string.start_time, Date(startTime.toInstant().toEpochMilli()), durationMinutes)
    }
}