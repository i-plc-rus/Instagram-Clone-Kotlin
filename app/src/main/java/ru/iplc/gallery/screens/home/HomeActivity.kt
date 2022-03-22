package ru.iplc.gallery.screens.home

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ru.iplc.gallery.R
import ru.iplc.gallery.databinding.ActivityCommentsBinding
import ru.iplc.gallery.databinding.ActivityHomeBinding
import ru.iplc.gallery.screens.comments.CommentsActivity
import ru.iplc.gallery.screens.common.BaseActivity
import ru.iplc.gallery.screens.common.setupAuthGuard
import ru.iplc.gallery.screens.common.setupBottomNavigation
//import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(), FeedAdapter.Listener {
    private lateinit var mAdapter: FeedAdapter
    private lateinit var mViewModel: HomeViewModel
    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate")

        mAdapter = FeedAdapter(this)
        binding.feedRecycler.adapter = mAdapter
        binding.feedRecycler.layoutManager = LinearLayoutManager(this)

        setupAuthGuard { uid ->
            setupBottomNavigation(uid, 0)
            mViewModel = initViewModel()
            mViewModel.init(uid)
            mViewModel.feedPosts.observe(this, Observer {
                it?.let {
                    mAdapter.updatePosts(it)
                }
            })
            mViewModel.goToCommentsScreen.observe(this, Observer {
                it?.let { postId ->
                    CommentsActivity.start(this, postId)
                }
            })
        }
    }

    override fun toggleLike(postId: String) {
        Log.d(TAG, "toggleLike: $postId")
        mViewModel.toggleLike(postId)
    }

    override fun loadLikes(postId: String, position: Int) {
        if (mViewModel.getLikes(postId) == null) {
            mViewModel.loadLikes(postId).observe(this, Observer {
                it?.let { postLikes ->
                    mAdapter.updatePostLikes(position, postLikes)
                }
            })
        }
    }

    override fun openComments(postId: String) {
        mViewModel.openComments(postId)
    }

    companion object {
        const val TAG = "HomeActivity"
    }
}