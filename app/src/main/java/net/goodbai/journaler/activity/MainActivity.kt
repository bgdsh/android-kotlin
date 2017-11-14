package net.goodbai.journaler.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.View
import kotlinx.android.synthetic.main.activity_header.*
import kotlinx.android.synthetic.main.activity_main.*
import net.goodbai.journaler.R.layout.activity_main
import net.goodbai.journaler.R
import net.goodbai.journaler.fragment.ItemsFragment
import net.goodbai.journaler.fragment.ManualFragment

class MainActivity : BaseActivity() {
    override fun getActivityTitle(): Int = R.string.app_name
    override val tag = "Main activity"
    override fun getLayout(): Int = activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pager.adapter = ViewPagerAdapter(supportFragmentManager)
//        val fragment = ItemsFragment()
//        supportFragmentManager
//                .beginTransaction()
//                .add(R.id.fragment_container, fragment)
//                .commit()
//        filter_menu.text = "H"
//        filter_menu.setOnClickListener {
//            val userManualFrg = ManualFragment()
//            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, userManualFrg)
//                    .addToBackStack("User manual")
//                    .commit()
//        }
    }

    private class ViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
        override fun getItem(position: Int): Fragment = ItemsFragment()
        override fun getCount(): Int = 5
    }
}

