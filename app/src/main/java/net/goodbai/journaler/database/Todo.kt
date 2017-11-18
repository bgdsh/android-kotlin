package net.goodbai.journaler.database

import android.location.Location

class Todo(
        title: String,
        message: String,
        location: Location,
        var scheduledFor: Long
) : Entry(title, message, location) {
    override var id: Long = 0L

}