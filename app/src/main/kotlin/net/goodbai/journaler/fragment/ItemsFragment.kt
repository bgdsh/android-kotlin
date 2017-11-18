package net.goodbai.journaler.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationSet
import android.view.animation.BounceInterpolator
import net.goodbai.journaler.R
import net.goodbai.journaler.activity.NoteActivity
import net.goodbai.journaler.activity.TodoActivity
import net.goodbai.journaler.model.MODE
import java.text.SimpleDateFormat
import java.util.*

class ItemsFragment: BaseFragment() {
    override val logTag = "Items fragment"
    override fun getLayout(): Int = R.layout.fragment_items

    override fun onCreateView(
            inflater: LayoutInflater?,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(getLayout(), container, false)
        val btn = view?.findViewById<FloatingActionButton>(R.id.new_item)
        btn?.setOnClickListener{
            animate(btn, true)
            val items = arrayOf(
                    getString(R.string.todos),
                    getString(R.string.notes)
            )
            val builder = AlertDialog
                    .Builder(this@ItemsFragment.context)
                    .setTitle(R.string.choose_a_type)
                    .setOnCancelListener { animate(btn, false) }
                    .setOnDismissListener{ animate(btn, false) }
                    .setItems(
                            items,
                            {
                                _, which ->
                                when(which) {
                                    TODO_REQUEST-> {
                                        openCreateTodo()
                                    }
                                    NOTE_REQUEST -> {
                                        openCreateNote()
                                    }
                                }

                            }
                    )
            builder.show()
        }
        return view
    }

    private val NOTE_REQUEST: Int = 1

    private fun openCreateNote(){
        val intent = Intent(context, NoteActivity::class.java)
        val data = Bundle()
        data.putInt(MODE.EXTRAS_KEY, MODE.CREATE.mode)
        intent.putExtras(data)
        startActivityForResult(intent, NOTE_REQUEST)
    }

    private val TODO_REQUEST: Int = 0

    private fun openCreateTodo() {
        val date = Date(System.currentTimeMillis())
        val dateFormat = SimpleDateFormat("MMM dd YYYY", Locale.CHINESE)
        val timeFormat = SimpleDateFormat("MM:HH", Locale.CHINESE)

        val intent = Intent(context, TodoActivity::class.java)
        val data = Bundle()
        data.putInt(MODE.EXTRAS_KEY, MODE.CREATE.mode)
        data.putString(TodoActivity.EXTRA_DATE, dateFormat.format(date))
        data.putString(TodoActivity.EXTRA_TIME, timeFormat.format(date))
        intent.putExtras(data)
        startActivityForResult(intent, TODO_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            TODO_REQUEST -> {
                if (requestCode == Activity.RESULT_OK) {
                    Log.i(logTag, "We created new TODO")
                } else {
                    Log.w(logTag, "we didn't created new TODO")
                }
            }
            NOTE_REQUEST -> {
                if (requestCode == Activity.RESULT_OK) {
                    Log.i(logTag, "We created new note")
                } else {
                    Log.w(logTag, "we didn't created new note")
                }
            }
        }
    }

    private fun animate(btn: FloatingActionButton, expand: Boolean = true) {
//        btn.animate()
//                .setInterpolator(BounceInterpolator())
//                .scaleX(if(expand){ 1.5f }else{ 1.0f })
//                .scaleY(if(expand){ 1.5f }else{ 1.0f })
//                .setDuration(2000)
//                .start()
        val animation1 = ObjectAnimator.ofFloat(btn, "scaleX", if(expand){ 1.5f }else{ 1.0f })
        animation1.duration = 2000
        animation1.interpolator = BounceInterpolator()

        val animation2 = ObjectAnimator.ofFloat(btn, "scaleY", if(expand){ 1.5f }else{ 1.0f })
        animation2.duration = 2000
        animation2.interpolator = BounceInterpolator()

        val animation3 = ObjectAnimator.ofFloat(btn, "alpha", if(expand){ 0.3f }else{ 1.0f })
        animation3.duration = 500
        animation3.interpolator = BounceInterpolator()

        val set = AnimatorSet()
        set.play(animation1).with(animation2).before(animation3)
        set.start()

    }
}
