package net.goodbai.journaler.activity

import net.goodbai.journaler.R

class NoteActivity: ItemActivity() {
    override fun getActivityTitle(): Int = R.string.activity_note_title

    override val tag: String = "Note activity"
    override fun getLayout(): Int = R.layout.activity_note

}