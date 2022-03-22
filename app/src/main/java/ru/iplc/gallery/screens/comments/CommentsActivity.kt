package ru.iplc.gallery.screens.comments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ru.iplc.gallery.R
import ru.iplc.gallery.databinding.ActivityAddFriendsBinding
import ru.iplc.gallery.databinding.ActivityCommentsBinding
import ru.iplc.gallery.models.User
import ru.iplc.gallery.screens.common.BaseActivity
import ru.iplc.gallery.screens.common.loadUserPhoto
import ru.iplc.gallery.screens.common.setupAuthGuard
//import kotlinx.android.synthetic.main.activity_comments.*

class CommentsActivity : BaseActivity() {
    private lateinit var mAdapter: CommentsAdapter
    private lateinit var mUser: User
    private lateinit var binding: ActivityCommentsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_comments)
        setContentView(binding.root)

        val postId = intent.getStringExtra(EXTRA_POST_ID) ?: return finish()

        binding.backImage.setOnClickListener { finish() }

        setupAuthGuard {
            mAdapter = CommentsAdapter()
            binding.commentsRecycler.layoutManager = LinearLayoutManager(this)
            binding.commentsRecycler.adapter = mAdapter

            val viewModel = initViewModel<CommentsViewModel>()
            viewModel.init(postId)
            viewModel.user.observe(this, Observer {
                it?.let {
                    mUser = it
                    binding.userPhoto.loadUserPhoto(mUser.photo)
                }
            })
            viewModel.comments.observe(this, Observer {
                it?.let {
                    mAdapter.updateComments(it)
                }
            })
            binding.postCommentText.setOnClickListener {
                viewModel.createComment(binding.commentText.text.toString(), mUser)
                binding.commentText.setText("")
            }
        }
    }

    companion object {
        private const val EXTRA_POST_ID = "POST_ID"

        fun start(context: Context, postId: String) {
            val intent = Intent(context, CommentsActivity::class.java)
            intent.putExtra(EXTRA_POST_ID, postId)
            context.startActivity(intent)
        }
    }
}