package ru.iplc.gallery.screens.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ru.iplc.gallery.R
import ru.iplc.gallery.databinding.ActivityHomeBinding
import ru.iplc.gallery.screens.comments.CommentsActivity
import ru.iplc.gallery.screens.common.BaseActivity
import ru.iplc.gallery.screens.common.setupAuthGuard
import ru.iplc.gallery.screens.common.setupBottomNavigation
import ru.iplc.gallery.screens.notifications.NotificationsActivity
import ru.iplc.gallery.screens.profile.ProfileActivity
import ru.iplc.gallery.screens.search.SearchActivity
import ru.iplc.gallery.screens.share.ShareActivity

//import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(), FeedAdapter.Listener {
//class HomeActivity : AppCompatActivity(), FeedAdapter.Listener {
    private lateinit var mAdapter: FeedAdapter
    private lateinit var mViewModel: HomeViewModel
    private lateinit var binding : ActivityHomeBinding
    //private lateinit var bindingBottom : Activi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        //setupBottomNavigation()
        //var view = binding.root
        setContentView(binding.root)

        Log.d(TAG, "onCreate")

        mAdapter = FeedAdapter(this)
        binding.feedRecycler.adapter = mAdapter
        binding.feedRecycler.layoutManager = LinearLayoutManager(this)



        setupAuthGuard { uid ->
            setupBottomNavigation(uid, 0, binding.bottomNavigationView)
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

    //+++
    /*private fun setupBottomNavigation() {


        /*binding.bottomNavigationInclude.bottomNavigationView.setItemIconSizeRes(29)
        binding.bottomNavigationInclude.bottomNavigationView.setTextVisibility(false)
        binding.bottomNavigationInclude.bottomNavigationView.enableItemShiftingMode(false)
        binding.bottomNavigationInclude.bottomNavigationView.enableShiftingMode(false)
        binding.bottomNavigationInclude.bottomNavigationView.enableAnimation(false)*/
        /*for (i in 0 until binding.bottomNavigationInclude.bottomNavigationView.menu.size()) {
            binding.bottomNavigationInclude.bottomNavigationView.setIconTintList(i, null)
        }*/
        binding.bottomNavigationView.bottomNavigationView.setOnNavigationItemSelectedListener {
            val nextActivity =
                when (it.itemId) {
                    R.id.nav_item_home -> HomeActivity::class.java
                    R.id.nav_item_search -> SearchActivity::class.java
                    R.id.nav_item_share -> ShareActivity::class.java
                    R.id.nav_item_likes -> NotificationsActivity::class.java
                    R.id.nav_item_profile -> ProfileActivity::class.java
                    else -> {
                        Log.e(HomeActivity.TAG, "unknown nav item clicked $it")
                        null
                    }
                }
            if (nextActivity != null) {
                val intent = Intent(this, nextActivity)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivity(intent)
                overridePendingTransition(0, 0)
                true
            } else {
                false
            }
        }
    }*/


    /*override fun onResume() {
        super.onResume()
        if (binding.bottomNavigationInclude.bottomNavigationView != null) {
            binding.bottomNavigationInclude.bottomNavigationView.menu.getItem(0).isChecked = true
        }
    }*/

    //--
}