package net.goodbai.journaler.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
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
import net.goodbai.journaler.service.MainService

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
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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
                Runnable { pager.setCurrentItem(0, true) }
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
        menuItems.add(synchronize)
        val navigationDrawerAdapter = NavigationDrawerAdapter(this, menuItems)
        left_drawer.adapter = navigationDrawerAdapter


        // Service
        val serviceIntent = Intent(this, MainService::class.java)
        startService(serviceIntent)
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

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, MainService::class.java)
        bindService(intent, serviceConnection, android.content.Context.BIND_AUTO_CREATE)
    }

    override fun onPause() {
        super.onPause()
        unbindService(serviceConnection)
    }

    private class ViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
        override fun getItem(position: Int): Fragment = ItemsFragment()
        override fun getCount(): Int = 5
    }

    private val keyPagePosition = "keyPagePosition"

    private var service: MainService? = null

    private val synchronize: NavigationDrawerItem by lazy {
        NavigationDrawerItem(
                getString(R.string.synchronize),
                Runnable { service?.synchronize() },
                false
        )
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            if (binder is MainService.MainServiceBinder) {
                service = binder.getService()
                service?.let {
                    synchronize.enabled = true
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            service = null
            synchronize.enabled = false
        }
    }
}

