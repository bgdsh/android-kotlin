package net.goodbai.journaler.activity

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import kotlinx.android.synthetic.main.activity_note.*
import net.goodbai.journaler.R
import net.goodbai.journaler.database.Db
import net.goodbai.journaler.database.Note
import net.goodbai.journaler.execution.TaskExecutor
import net.goodbai.journaler.location.LocationProvider

class NoteActivity: ItemActivity() {
    private var note: Note? = null
    private var location: Location? = null
    private var handler: Handler? = null
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            updateNote()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    }
    private val locationListener = object : LocationListener {
        override fun onLocationChanged(p0: Location?) {
            p0?.let {
                LocationProvider.unsubscribe(this)
                location = p0
                val title = getNoteTitle()
                val content = getNoteContent()
                note = Note(title, content, p0)
                executor.execute {
                    val param = note
                    var result = false
                    param?.let {
                        result = Db.Note.insert(param) > 0
                    }
                    if (result) {
                        Log.i(tag, "Note inserted.")
                    } else {
                        Log.e(tag, "Note not inserted.")
                    }

                    handler?.post {
                        var color = R.color.vermilion
                        if (result) {
                            color = R.color.green
                        }
                        sendMessage(result)
                    }
                }

            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onProviderDisabled(provider: String?) {
        }

    }
    private val executor = TaskExecutor.getInstance(1)

    private fun getNoteContent(): String = note_content.text.toString()
    private fun getNoteTitle(): String = note_title.text.toString()
    private fun updateNote() {
        if (note == null){
            if (!TextUtils.isEmpty(getNoteTitle()) && !TextUtils.isEmpty(getNoteContent())) {
                LocationProvider.subscribe(locationListener)
            }
        } else {
            note?.title = getNoteTitle()
            note?.message = getNoteContent()
            executor.execute {
                var result = false
                val param = note
                param?.let {
                    result = Db.Note.update(param) > 0
                }
                if (result) {
                    Log.i(tag, "Note updated.")
                } else {
                    Log.e(tag, "Note not updated.")
                }
                sendMessage(result)
            }
        }

    }
    private fun sendMessage(result: Boolean) {
        val msg = handler?.obtainMessage()
        if (result) {
            msg?.arg1 = 1
        } else {
            msg?.arg1 = 0
        }
        handler?.sendMessage(msg)
    }

    override val tag: String = "Note activity"

    override fun getActivityTitle(): Int = R.string.activity_note_title
    override fun getLayout(): Int = R.layout.activity_note
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler = object: Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message?) {
                msg?.let {
                    var color = R.color.vermilion
                    if (msg.arg1 > 0) {
                        color = R.color.green
                    }
                    indicator.setBackgroundColor(ContextCompat.getColor(this@NoteActivity, color))
                }
                super.handleMessage(msg)
            }
        }
        note_title.addTextChangedListener(textWatcher)
        note_content.addTextChangedListener(textWatcher)
    }

}