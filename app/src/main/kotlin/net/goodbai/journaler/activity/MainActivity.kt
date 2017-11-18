package net.goodbai.journaler.activity

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import net.goodbai.journaler.R.layout.activity_main
import net.goodbai.journaler.R
import net.goodbai.journaler.fragment.ItemsFragment
import net.goodbai.journaler.navigation.NavigationDrawerAdapter
import net.goodbai.journaler.navigation.NavigationDrawerItem
import net.goodbai.journaler.preferences.PreferencesConfiguration
import net.goodbai.journaler.preferences.PreferencesProvider

class MainActivity : BaseActivity() {
    override fun getActivityTitle(): Int = R.string.app_name
    override val tag = "Main activity"
    override fun getLayout(): Int = activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val provider = PreferencesProvider()
        val config = PreferencesConfiguration("journaler_prefs", Context.MODE_PRIVATE)
        val preferences = provider.obtain(config, this)

        pager.adapter = ViewPagerAdapter(supportFragmentManager)
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                Log.v(tag, "Page [ $position ]")
                preferences.edit().putInt(keyPagePosition, position).apply()
            }

        })
        val pagerPosition = preferences.getInt(keyPagePosition, 0)
        pager.setCurrentItem(pagerPosition, true)

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

    private val keyPagePosition = "keyPagePosition"
}

