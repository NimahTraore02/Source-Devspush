package com.decouikit.news.activities.common

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.decouikit.news.R
import com.decouikit.news.activities.NewsApplication
import com.decouikit.news.activities.NotificationActivity
import com.decouikit.news.activities.SearchActivity
import com.decouikit.news.adapters.CategoryAdapter
import com.decouikit.news.database.Config
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.openExternalApp
import com.decouikit.news.extensions.replaceFragment
import com.decouikit.news.extensions.showNetworkMessage
import com.decouikit.news.extensions.viewAll
import com.decouikit.news.fragments.*
import com.decouikit.news.interfaces.HomeFragmentListener
import com.decouikit.news.interfaces.OnCategoryItemClickListener
import com.decouikit.news.network.dto.Category
import com.decouikit.news.network.receiver.ConnectionStateMonitor
import com.decouikit.news.utils.ActivityUtil
import com.decouikit.news.utils.NewsConstants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_side_menu_with_bottom_navigation.*
import kotlinx.android.synthetic.main.app_bar_main_with_side_menu_and_bottom_navigation.*


class SideMenuWithBottomNavigationActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
    HomeFragmentListener, View.OnClickListener,
    BottomNavigationView.OnNavigationItemSelectedListener, OnCategoryItemClickListener {

    private lateinit var toolbar: Toolbar
    private lateinit var menuItem: Menu
    private var doubleBackToExitPressedOnce = false
    private lateinit var snackBar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_side_menu_with_bottom_navigation)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) //hide default app title in toolbar
        val drawerLayout: DrawerLayout = findViewById(R.id.parent)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        setSocialNetworksIconVisibility()
        setSideMenu()
        navigation?.setOnNavigationItemSelectedListener(this)
        ActivityUtil.setLayoutDirection(this, getLayoutDirection(), R.id.parent)
        loadFragment(intent.getIntExtra(NewsConstants.FRAGMENT_POSITION, -1))

        snackBar = Snackbar.make(findViewById(R.id.parent),
            getString(R.string.check_internet_connection), Snackbar.LENGTH_LONG)

        checkInternetConnection()
    }

    private fun setSideMenu() {
        val categories = ArrayList(InMemory.getCategoryList(this))
        val adapter = CategoryAdapter(categories, this)
        rvContainer.layoutManager = LinearLayoutManager(this)
        rvContainer.adapter = adapter
    }

    private fun checkInternetConnection() {
        ConnectionStateMonitor(this).observe(this, Observer { isConnected ->
            isConnected?.let {
                snackBar.showNetworkMessage(this, isConnected)
                if (isConnected) {
                    showBannerAds()
                }
            }
        })
    }

    override fun getAdsContainer(): ViewGroup? {
        return viewContainerForAds
    }

    private fun loadFragment(fragmentPosition: Int) {
        if (fragmentPosition == 5) {
            ActivityUtil.setAppBarElevation(appBar, 8f)
            replaceFragment(SettingsFragment.newInstance(), R.id.navigation_container)
            nav_view.setCheckedItem(R.id.nav_settings)
        } else {
            replaceFragment(HomeFragment.newInstance(), R.id.navigation_container)
            nav_view.setCheckedItem(R.id.nav_home)
        }
    }

    private fun setSocialNetworksIconVisibility() {
        if (Config.getInstagramUrl().isNotEmpty()) {
            ivInstagram.setOnClickListener(this)
            ivInstagram.visibility = View.VISIBLE
        } else {
            ivInstagram.visibility = View.GONE
        }
        if (Config.getFacebookUrl().isNotEmpty()) {
            ivFacebook.setOnClickListener(this)
            ivFacebook.visibility = View.VISIBLE
        } else {
            ivFacebook.visibility = View.GONE
        }
        if (Config.getTwitterUrl().isNotEmpty()) {
            ivTwitter.setOnClickListener(this)
            ivTwitter.visibility = View.VISIBLE
        } else {
            ivTwitter.visibility = View.GONE
        }
        if (Config.getYoutubeUrl().isNotEmpty()) {
            ivYoutube.setOnClickListener(this)
            ivYoutube.visibility = View.VISIBLE
        } else {
            ivYoutube.visibility = View.GONE
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

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.parent)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }

            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, R.string.back_exit_text, Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        menuItem = menu
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
        when (item.itemId) {
            R.id.nav_home -> {
                replaceFragment(HomeFragment.newInstance(), R.id.navigation_container)
            }
            R.id.nav_category -> {
                replaceFragment(CategoryFragment.newInstance(), R.id.navigation_container)
            }
            R.id.nav_bookmark -> {
                replaceFragment(BookmarkFragment.newInstance(), R.id.navigation_container)
            }
            R.id.nav_about -> {
                replaceFragment(AboutFragment.newInstance(), R.id.navigation_container)
            }
            R.id.nav_settings -> {
                replaceFragment(SettingsFragment.newInstance(), R.id.navigation_container)
            }
        }
        if (item.itemId != R.id.nav_home) {
            ActivityUtil.setAppBarElevation(appBar, 8f)
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.parent)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun homeFragmentBehavior() {
        ActivityUtil.setAppBarElevation(appBar, 0f)
    }

    override fun onClick(v: View) {
        when (v) {
            ivInstagram -> v.openExternalApp(Config.getInstagramUrl())
            ivFacebook -> v.openExternalApp(Config.getFacebookUrl())
            ivTwitter -> v.openExternalApp(Config.getTwitterUrl())
            ivYoutube -> v.openExternalApp(Config.getYoutubeUrl())
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            llSocialNetwork.visibility = View.GONE
        } else {
            llSocialNetwork.visibility = View.VISIBLE
        }
    }

    override fun onCategoryItemClick(item: Category) {
        viewAll(this, item.id, item.name)
    }
}
