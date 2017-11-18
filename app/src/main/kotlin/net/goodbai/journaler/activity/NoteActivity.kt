package net.goodbai.journaler.activity

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import kotlinx.android.synthetic.main.activity_note.*
import net.goodbai.journaler.R
import net.goodbai.journaler.database.Db
import net.goodbai.journaler.database.Note
import net.goodbai.journaler.location.LocationProvider

class NoteActivity: ItemActivity() {
    override fun getActivityTitle(): Int = R.string.activity_note_title
    override val tag: String = "Note activity"
    override fun getLayout(): Int = R.layout.activity_note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        note_title.addTextChangedListener(textWatcher)
        note_content.addTextChangedListener(textWatcher)
    }

    private var note: Note? = null
    private var location: Location? = null

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
                val task = @SuppressLint("StaticFieldLeak") object :AsyncTask<Note, Void, Boolean>() {
                    override fun doInBackground(vararg params: Note?): Boolean {
                        if (!params.isEmpty()) {
                            val param = params[0]
                            param?.let {
                                return Db.Note.insert(param) > 0
                            }
                        }
                        return false
                    }

                    override fun onPostExecute(result: Boolean?) {
                        result?.let {
                            if (result) {
                                Log.i(tag, "Note inserted.")
                            } else {
                                Log.e(tag, "Note not inserted")
                            }
                        }
                    }

                }
                task.execute(note)
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onProviderDisabled(provider: String?) {
        }

    }

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
            val task = @SuppressLint("StaticFieldLeak")
            object : AsyncTask<Note, Void, Boolean>() {
                override fun doInBackground(vararg params: Note?): Boolean {
                    if (!params.isEmpty()) {
                        val param = params[0]
                        param?.let {
                            return Db.Note.update(param) > 0
                        }
                    }
                    return false
                }

                override fun onPostExecute(result: Boolean?) {
                    result?.let {
                        if (result) {
                            Log.i(tag, "Note updated.")
                        } else {
                            Log.e(tag, "Note not updated.")
                        }
                    }

                }

            }
            task.execute(note)
        }

    }

}