package com.decouikit.news.activities.common

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.decouikit.news.R
import com.decouikit.news.activities.NewsApplication
import com.decouikit.news.activities.NotificationActivity
import com.decouikit.news.activities.SearchActivity
import com.decouikit.news.extensions.replaceFragment
import com.decouikit.news.extensions.showNetworkMessage
import com.decouikit.news.fragments.*
import com.decouikit.news.interfaces.HomeFragmentListener
import com.decouikit.news.network.receiver.NetworkReceiver
import com.decouikit.news.network.receiver.NetworkReceiverListener
import com.decouikit.news.utils.ActivityUtil
import com.decouikit.news.utils.NewsConstants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_bottom_navigation.*

class BottomNavigationActivity : BaseActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener,
    HomeFragmentListener,
    NetworkReceiverListener, View.OnClickListener {

    private var doubleBackToExitPressedOnce = false

    private lateinit var snackBar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)


        ActivityUtil.setLayoutDirection(this, getLayoutDirection(), R.id.parent)
        navigation?.setOnNavigationItemSelectedListener(this)
        loadFragment(intent.getIntExtra(NewsConstants.FRAGMENT_POSITION, -1))

        snackBar = Snackbar.make(findViewById(R.id.parent),
            getString(R.string.check_internet_connection), Snackbar.LENGTH_LONG)

        initListeners()

//        registerReceiver(NetworkReceiver(this), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
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

    private fun initListeners() {
        ivSearch.setOnClickListener(this)
        ivNotification.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v) {
            ivSearch -> {
                startActivity(Intent(this, SearchActivity::class.java))
            }
            ivNotification -> {
                startActivity(Intent(this, NotificationActivity::class.java))
            }
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