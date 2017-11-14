package net.goodbai.journaler.activity

import net.goodbai.journaler.R

class TodoActivity : ItemActivity() {
    override fun getActivityTitle(): Int = R.string.activity_todo_title

    override val tag: String = "Todo activity"
    override fun getLayout(): Int = R.layout.activity_todo

}