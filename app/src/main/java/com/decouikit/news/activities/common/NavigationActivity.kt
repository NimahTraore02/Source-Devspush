package com.decouikit.news.activities.common

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.decouikit.news.R
import com.decouikit.news.activities.NewsApplication
import com.decouikit.news.database.Config
import com.decouikit.news.extensions.openExternalApp
import com.decouikit.news.extensions.replaceFragment
import com.decouikit.news.fragments.*
import com.decouikit.news.interfaces.HomeFragmentListener
import com.decouikit.news.utils.ActivityUtil
import com.decouikit.news.utils.NewsConstants
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class NavigationActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
    HomeFragmentListener, View.OnClickListener {

    private lateinit var toolbar: Toolbar
    private lateinit var menuItem: Menu
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) //hide default app title in toolbar
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
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
        navView.setNavigationItemSelectedListener(this)
        ActivityUtil.setLayoutDirection(this, getLayoutDirection(), R.id.parent)
        showBannerAds()
        loadFragment(intent.getIntExtra(NewsConstants.FRAGMENT_POSITION, -1))
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
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }

            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, R.string.back_exit_text, Toast.LENGTH_SHORT).show()
            Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        menuItem = menu
        //set true for visible search button
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_search -> {
                Toast.makeText(this, getString(R.string.search), Toast.LENGTH_SHORT).show()
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
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
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
}
