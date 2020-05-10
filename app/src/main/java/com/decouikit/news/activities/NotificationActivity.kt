package com.decouikit.news.activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.R
import com.decouikit.news.activities.common.BaseActivity
import com.decouikit.news.adapters.BaseListAdapter
import com.decouikit.news.database.Config
import com.decouikit.news.database.InMemory
import com.decouikit.news.database.Preference
import com.decouikit.news.extensions.openPostActivity
import com.decouikit.news.interfaces.OpenPostListener
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.utils.ActivityUtil
import kotlinx.android.synthetic.main.activity_notifications.*

class NotificationActivity : BaseActivity(), View.OnClickListener, OpenPostListener {

    private lateinit var adapter: BaseListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        ActivityUtil.setLayoutDirection(this, getLayoutDirection(), R.id.notification_parent)
        initLayout()
        initListeners()
    }

    private fun initLayout() {
        if (Preference(this).isRtlEnabled) {
            ivBack.rotation = 180f
        }
        //Creating list type
        val adapterType = Config.getNotificationAdapterConfig()
        adapter = BaseListAdapter(arrayListOf(), adapterType)
        rvNotifications.layoutManager = GridLayoutManager(this, adapterType.columns)

        adapter.setItemClickListener(this)
        rvNotifications.adapter = adapter
        hideContent(true)
        //swipe to remove item listener
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvNotifications)
    }

    private var callback: ItemTouchHelper.SimpleCallback = object :
        ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.START or ItemTouchHelper.END
        ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            //Remove swiped item from list and notify the RecyclerView
            val post = adapter.getItemByPosition(viewHolder.adapterPosition)
            InMemory.removeNotificationPosts(this@NotificationActivity, post.id)
            adapter.removeItemByPosition(viewHolder.adapterPosition)
            hideContent(adapter.itemCount == 0)
        }
    }

    private fun initListeners() {
        ivBack.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        adapter.setData(InMemory.getNotificationPosts(this) as ArrayList<PostItem>)
        hideContent(adapter.itemCount == 0)
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