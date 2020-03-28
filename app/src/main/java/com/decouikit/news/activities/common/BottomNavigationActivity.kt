package com.decouikit.news.activities.common

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.decouikit.news.R
import com.decouikit.news.activities.NewsApplication
import com.decouikit.news.activities.NotificationActivity
import com.decouikit.news.activities.SearchActivity
import com.decouikit.news.extensions.replaceFragment
import com.decouikit.news.extensions.showNetworkMessage
import com.decouikit.news.fragments.*
import com.decouikit.news.interfaces.HomeFragmentListener
import com.decouikit.news.network.receiver.NetworkReceiverListener
import com.decouikit.news.network.receiver.NetworkReceiver
import com.decouikit.news.utils.ActivityUtil
import com.decouikit.news.utils.NewsConstants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_bottom_navigation.*

class BottomNavigationActivity : BaseActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener,
    HomeFragmentListener,
    NetworkReceiverListener {

    private lateinit var toolbar: Toolbar
    private var doubleBackToExitPressedOnce = false

    private lateinit var snackBar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) //hide default app title in toolbar
        ActivityUtil.setLayoutDirection(this, getLayoutDirection(), R.id.parent)
        navigation?.setOnNavigationItemSelectedListener(this)
        loadFragment(intent.getIntExtra(NewsConstants.FRAGMENT_POSITION, -1))

        snackBar = Snackbar.make(findViewById(R.id.parent),
            getString(R.string.check_internet_connection), Snackbar.LENGTH_LONG)
        registerReceiver(NetworkReceiver(this), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun loadFragment(fragmentPosition: Int) {
        if (fragmentPosition == 5) {
            ActivityUtil.setAppBarElevation(appBar, 8f)
            replaceFragment(SettingsFragment.newInstance(), R.id.navigation_container)
            navigation.selectedItemId = R.id.nav_settings
        } else {
            replaceFragment(HomeFragment.newInstance(), R.id.navigation_container)
            navigation.selectedItemId = R.id.nav_home
        }
    }

    override fun onResume() {
        super.onResume()
        NewsApplication.activityResumed()
    }

    override fun onPause() {
        NewsApplication.activityPaused()
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        //set true for visible search button
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            R.id.action_notification -> {
                startActivity(Intent(this, NotificationActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId != R.id.nav_home) {
            ActivityUtil.setAppBarElevation(appBar, 8f)
        }
        when (item.itemId) {
            R.id.nav_home -> {
                replaceFragment(HomeFragment.newInstance(), R.id.navigation_container)
                return true
            }
            R.id.nav_category -> {
                replaceFragment(CategoryFragment.newInstance(), R.id.navigation_container)
                return true
            }
            R.id.nav_bookmark -> {
                replaceFragment(BookmarkFragment.newInstance(), R.id.navigation_container)
                return true
            }
            R.id.nav_about -> {
                replaceFragment(AboutFragment.newInstance(), R.id.navigation_container)
                return true
            }
            R.id.nav_settings -> {
                replaceFragment(SettingsFragment.newInstance(), R.id.navigation_container)
                return true
            }
        }
        return false
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, R.string.back_exit_text, Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun homeFragmentBehavior() {
        ActivityUtil.setAppBarElevation(appBar, 0f)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        snackBar.showNetworkMessage(this, isConnected)
        if (isConnected) {
            showBannerAds()
        }
    }
}