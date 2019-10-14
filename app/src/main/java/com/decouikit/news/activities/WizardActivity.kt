package com.decouikit.news.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.decouikit.news.R
import com.decouikit.news.adapters.ViewPagerAdapter
import com.decouikit.news.database.Config
import com.decouikit.news.extensions.openMainPage
import com.decouikit.news.fragments.WizardPageFragment
import com.google.firebase.FirebaseApp
import kotlinx.android.synthetic.main.activity_wizard.*

class WizardActivity : AppCompatActivity(), View.OnClickListener, ViewPager.OnPageChangeListener {

    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wizard)
        setupViewPager()
        FirebaseApp.initializeApp(this)
        tvSkip.setOnClickListener(this)
        tvGetStart.setOnClickListener(this)
    }

    private fun setupViewPager() {
        adapter = ViewPagerAdapter(supportFragmentManager)
        Config.getIntroData(this).forEach {
            adapter.addFragment(WizardPageFragment.newInstance(it))
        }
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(this)
        tabDots.setupWithViewPager(viewPager)

    }

    override fun onClick(v: View?) {
        when (v) {
            tvSkip -> {
                if (tabDots.selectedTabPosition == (adapter.count - 1)) {
                    openMainPage()
                } else {
                    viewPager.currentItem = tabDots.selectedTabPosition + 1
                }
            }
            tvGetStart -> {
                openMainPage()
            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        if (position == (adapter.count - 1)) {
            tvSkip.text = getString(R.string.wizard_finish)
            return
        }
        tvSkip.text = getString(R.string.wizard_skip)
    }
}
