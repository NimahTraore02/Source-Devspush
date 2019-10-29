package com.decouikit.news.activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.decouikit.news.R
import com.decouikit.news.activities.common.BaseActivity
import com.decouikit.news.adapters.ViewAllAdapter
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.openPostActivity
import com.decouikit.news.interfaces.OpenPostListener
import com.decouikit.news.network.dto.PostItem
import kotlinx.android.synthetic.main.activity_notifications.*

class NotificationActivity : BaseActivity(), View.OnClickListener, OpenPostListener {

    private lateinit var adapter: ViewAllAdapter
    private lateinit var notificationList: ArrayList<PostItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        initLayout()
        initListeners()
    }

    private fun initLayout() {
        notificationList = InMemory.getNotificationPosts()
        if (notificationList.isNullOrEmpty()) {
            hideContent(true)
        } else {
            hideContent(false)
            adapter = ViewAllAdapter(notificationList, this)
            rvNotifications.layoutManager = LinearLayoutManager(this)
            rvNotifications.adapter = adapter
        }
    }

    private fun initListeners() {
        ivBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v) {
            ivBack -> onBackPressed()
        }
    }

    override fun openPost(view: View, item: PostItem) {
        view.openPostActivity(this, item)
    }

    private fun hideContent(isListEmpty: Boolean) {
        if (isListEmpty) {
            rvNotifications.visibility = View.GONE
            empty_container.visibility = View.VISIBLE
        } else {
            rvNotifications.visibility = View.VISIBLE
            empty_container.visibility = View.GONE
        }
    }
}