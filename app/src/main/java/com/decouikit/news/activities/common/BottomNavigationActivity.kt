package com.decouikit.news.activities.common

import android.os.Bundle
import android.view.MenuItem
import com.decouikit.news.R
import com.decouikit.news.activities.NewsApplication
import com.decouikit.news.extensions.replaceFragment
import com.decouikit.news.fragments.*
import com.decouikit.news.interfaces.HomeFragmentListener
import com.decouikit.news.utils.ActivityUtil
import com.decouikit.news.utils.NewsConstants
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_bottom_navigation.*

class BottomNavigationActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    HomeFragmentListener {

    private var fragmentPosition: Int? = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)
        ActivityUtil.setLayoutDirection(this, getLayoutDirection(), R.id.parent)

        fragmentPosition = intent.getIntExtra(NewsConstants.FRAGMENT_POSITION, -1)
        navigation?.setOnNavigationItemSelectedListener(this)
    }

    override fun onResume() {
        super.onResume()
        if (fragmentPosition == 5) {
            ActivityUtil.setAppBarElevation(appBar, 8f)
            replaceFragment(SettingsFragment.newInstance(), R.id.navigation_container)
            navigation.selectedItemId = R.id.nav_settings
            fragmentPosition = -1
        } else {
            replaceFragment(HomeFragment.newInstance(), R.id.navigation_container)
            navigation.selectedItemId = R.id.nav_home
        }
        NewsApplication.activityResumed()
    }

    override fun onPause() {
        NewsApplication.activityPaused()
        super.onPause()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId != R.id.nav_home) {
            ActivityUtil.setAppBarElevation(appBar, 8f)
        }
        when(item.itemId) {
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


    override fun homeFragmentBehavior() {
        ActivityUtil.setAppBarElevation(appBar, 0f)
    }
}