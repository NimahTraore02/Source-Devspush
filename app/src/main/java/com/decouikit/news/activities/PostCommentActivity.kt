package com.decouikit.news.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.decouikit.news.R
import com.decouikit.news.activities.common.BaseActivity
import com.decouikit.news.database.Preference
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.extensions.validationCommon
import com.decouikit.news.extensions.validationOfEmail
import com.decouikit.news.network.CommentsService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.CommentRequest
import com.decouikit.news.utils.ActivityUtil
import com.decouikit.news.utils.NewsConstants
import kotlinx.android.synthetic.main.activity_post_comment.*
import org.jetbrains.anko.doAsync

class PostCommentActivity : BaseActivity(), View.OnClickListener {

    private var postId: Int = -1
    private val commentsService = RetrofitClientInstance.retrofitInstance?.create(CommentsService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_comment)
        ActivityUtil.setLayoutDirection(this, getLayoutDirection(), R.id.postCommentParent)
        if(Preference(this).isRtlEnabled) {
            ivBack.rotation = 180f
        }

        postId = intent.getIntExtra(NewsConstants.POST_ITEM_ID, -1)
        initListeners()
    }

    private fun initListeners() {
        ivBack.setOnClickListener(this)
        btnPostComment.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v) {
            ivBack -> {
                onBackPressed()
            }
            btnPostComment -> {
                if (validation(v)) {
                    postComment()
                }
            }
        }
    }

    private fun postComment() {
        doAsync {
            commentsService?.saveComment(CommentRequest(postId,
                etComment.text.toString(),
                etName.text.toString(),
                etEmail.text.toString(),
                ""))?.enqueue(result = {
                when (it) {
                    is Result.Success -> {
                        if (it.response.body() != null) {
                            finish()
                        }
                    }
                    is Result.Failure -> {
                        Toast.makeText(this@PostCommentActivity, R.string.generic_error, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    private fun validation(v: View): Boolean {
        return v.validationCommon(etName, R.string.name_validation)
                && v.validationOfEmail(etEmail)
                && v.validationCommon(etComment, R.string.comment_validation)
    }
}