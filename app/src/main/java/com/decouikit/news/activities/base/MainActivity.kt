package com.decouikit.news.activities.base

import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.decouikit.news.R
import com.decouikit.news.activities.NewsApplication
import com.decouikit.news.database.Preference
import com.decouikit.news.extensions.replaceFragment
import com.decouikit.news.fragments.*
import com.decouikit.news.utils.ActivityUtil
import com.decouikit.news.utils.NewsConstants
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.doubleclick.PublisherAdRequest
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var toolbar: Toolbar
    private var fragmentPosition: Int? = -1
    private val prefs: Preference by lazy {
        Preference(this)
    }

    private lateinit var mInterstitialAd: PublisherInterstitialAd

    private val adRequest: PublisherAdRequest
        get() = PublisherAdRequest.Builder()
            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            .addTestDevice("D4660E67112CAF17CE018F3C95A8F64D")
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(Preference(this).colorTheme)
        setContentView(R.layout.activity_main)

        ActivityUtil.setLayoutDirection(
            this,
            if (prefs.isRtlEnabled)
                ViewCompat.LAYOUT_DIRECTION_RTL
            else
                ViewCompat.LAYOUT_DIRECTION_LOCALE
        )

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(com.decouikit.news.R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        fragmentPosition = intent.getIntExtra(NewsConstants.FRAGMENT_POSITION, -1)
        navView.setNavigationItemSelectedListener(this)

        MobileAds.initialize(this) {}
        showInterstitialAds()
        showBannerAds()
    }

    private fun showInterstitialAds() {
        mInterstitialAd = PublisherInterstitialAd(this)
        mInterstitialAd.adUnitId = getString(R.string.interstitial_id);
        mInterstitialAd.loadAd(adRequest)
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                if (mInterstitialAd.isLoaded) {
                    mInterstitialAd.show()
                }
            }
        }
    }

    private fun showBannerAds() {
        val bannerId = getString(R.string.banner_id)
        publisherAdView.visibility = if (!TextUtils.isEmpty(bannerId)) View.VISIBLE else View.GONE
        publisherAdView.loadAd(adRequest)
    }

    fun getToolbar(): Toolbar {
        return toolbar
    }

    override fun onResume() {
        super.onResume()
        NewsApplication.activityResumed()
        if (fragmentPosition == 5) {
            replaceFragment(SettingsFragment.newInstance(), R.id.navigation_container)
            nav_view.setCheckedItem(R.id.nav_settings)
        } else {
            replaceFragment(HomeFragment.newInstance(), R.id.navigation_container)
            nav_view.setCheckedItem(R.id.nav_home)
        }
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
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
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
//                replaceFragment(
//                    ViewAllFragment.newInstance(getString(R.string.bookmarked_news), arrayListOf()),
//                    R.id.navigation_container
//                )
            }
            R.id.nav_about -> {
                replaceFragment(AboutFragment.newInstance(), R.id.navigation_container)
            }
            R.id.nav_settings -> {
                replaceFragment(SettingsFragment.newInstance(), R.id.navigation_container)
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
