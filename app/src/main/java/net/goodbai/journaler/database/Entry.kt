package net.goodbai.journaler.database

import android.location.Location

abstract class Entry(
        var title: String,
        var message: String,
        var location: Location
) : DbModel()

