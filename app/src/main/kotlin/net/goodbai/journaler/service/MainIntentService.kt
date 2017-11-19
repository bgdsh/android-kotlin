package net.goodbai.journaler.service

import android.app.IntentService
import android.content.Intent


/**
 * A constructor is mandatory!
 */
class MainIntentService : IntentService("MainIntentService") {

    /**
     * All important work is performed here.
     */
    override fun onHandleIntent(intent: Intent?) {
        // Your implementation for handling received intents.

    }
}