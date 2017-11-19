package net.goodbai.journaler.service

import android.app.IntentService
import android.content.Intent
import android.util.Log
import net.goodbai.journaler.database.Db
import net.goodbai.journaler.database.Note
import net.goodbai.journaler.model.MODE

class DatabaseService : IntentService("MainIntentService") {

    companion object {
        val EXTRA_ENTRY = "entry"
        val EXTRA_OPERATION = "operation"
    }

    private val tag = "Database service"

    override fun onCreate() {
        super.onCreate()
        Log.v(tag, "[ ON CREATE ]")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.w(tag, "[ ON LOW MEMORY ]")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(tag, "[ ON DESTROY ]")
    }

    override fun onHandleIntent(p0: Intent?) {
        p0?.let {
            val note = p0.getParcelableExtra<Note>(EXTRA_ENTRY)
            note?.let {
                val operation = p0.getIntExtra(EXTRA_OPERATION, -1)
                when(operation) {
                    MODE.CREATE.mode -> {
                        val result = Db.Note.insert(note) > 0
                        if (result) {
                            Log.i(tag, "Note inserted")
                        } else {
                            Log.e(tag, "Not not inserted")
                        }
                    }
                    MODE.EDIT.mode -> {
                        val result = Db.Note.update(note) > 0
                        if (result) {
                            Log.i(tag, "Note updated")
                        } else {
                            Log.e(tag, "Not not updated")
                        }
                    }
                    else -> {
                        Log.w(tag, "Unknown mode [ $operation ]")
                    }

                }
            }
        }
    }
}