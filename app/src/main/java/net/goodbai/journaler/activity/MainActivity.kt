package net.goodbai.journaler.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.GravityCompat
import android.util.Log
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import net.goodbai.journaler.R.layout.activity_main
import net.goodbai.journaler.R
import net.goodbai.journaler.fragment.ItemsFragment
import net.goodbai.journaler.navigation.NavigationDrawerAdapter
import net.goodbai.journaler.navigation.NavigationDrawerItem

class MainActivity : BaseActivity() {
    override fun getActivityTitle(): Int = R.string.app_name
    override val tag = "Main activity"
    override fun getLayout(): Int = activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pager.adapter = ViewPagerAdapter(supportFragmentManager)
        val menuItems = mutableListOf<NavigationDrawerItem>()
        val today = NavigationDrawerItem(
                getString(R.string.today),
                Runnable { pager.setCurrentItem(0,true) }
        )
        val next7Days = NavigationDrawerItem(
                getString(R.string.next_seven_days),
                Runnable {
                    pager.setCurrentItem(1, true)
                }
        )
        val todos = NavigationDrawerItem(
                getString(R.string.todos),
                Runnable {
                    pager.setCurrentItem(2, true)
                }
        )
        val notes = NavigationDrawerItem(
                getString(R.string.notes),
                Runnable {
                    pager.setCurrentItem(3, true)
                }
        )

        menuItems.add(today)
        menuItems.add(next7Days)
        menuItems.add(todos)
        menuItems.add(notes)
        val navigationDrawerAdapter = NavigationDrawerAdapter(this, menuItems)
        left_drawer.adapter = navigationDrawerAdapter
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (if (item != null) item.itemId else null) {
            R.id.drawing_menu -> {
                drawer_layout.openDrawer(GravityCompat.START)
                Log.v(tag, "Main menu")
                return true
            }
            R.id.options_menu -> {
                Log.v(tag, "Options menu")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private class ViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
        override fun getItem(position: Int): Fragment = ItemsFragment()
        override fun getCount(): Int = 5
    }
}

