package ru.iplc.gallery.screens.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import ru.iplc.gallery.R
import ru.iplc.gallery.databinding.ActivityProfileBinding
import ru.iplc.gallery.models.User
import ru.iplc.gallery.screens.addfriends.AddFriendsActivity
import ru.iplc.gallery.screens.common.*
import ru.iplc.gallery.screens.editprofile.EditProfileActivity
import ru.iplc.gallery.screens.home.HomeActivity
import ru.iplc.gallery.screens.notifications.NotificationsActivity
import ru.iplc.gallery.screens.profilesettings.ProfileSettingsActivity
import ru.iplc.gallery.screens.search.SearchActivity
import ru.iplc.gallery.screens.share.ShareActivity
import ru.iplc.gallery.screens.share.ShareViewModel

//import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity() {
//class ProfileActivity : AppCompatActivity() {
    private lateinit var mAdapter: ImagesAdapter
    private lateinit var binding: ActivityProfileBinding
    private lateinit var mUser: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_profile)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "onCreate")

        binding.editProfileBtn.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
        binding.settingsImage.setOnClickListener {
            val intent = Intent(this, ProfileSettingsActivity::class.java)
            startActivity(intent)
        }
        binding.addFriendsImage.setOnClickListener {
            val intent = Intent(this, AddFriendsActivity::class.java)
            startActivity(intent)
        }
        binding.imagesRecycler.layoutManager = GridLayoutManager(this, 3)
        //setLayoutManager

        mAdapter = ImagesAdapter()
        //Log.d(TAG,"-->>"+ mAdapter.itemCount.toString())
        //mUser.

        binding.imagesRecycler.adapter = mAdapter
        //+++
        //setupBottomNavigation()
        /*val mViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)*/

        /*mViewModel.user.observe(this, Observer {
            it?.let {
                mUser = it
            }
        })*/

        /*binding.profileImage.loadUserPhoto(mUser.photo)
        binding.usernameText.text = mUser.username
        binding.followersCountText.text = mUser.followers.size.toString()
        binding.followingCountText.text = mUser.follows.size.toString()*/



        setupAuthGuard { uid ->
            setupBottomNavigation(uid,4, binding.bottomNavigationView)
            val viewModel = initViewModel<ProfileViewModel>()
            viewModel.init(uid)
            viewModel.user.observe(this, Observer {
                it?.let {
                    binding.profileImage.loadUserPhoto(it.photo)
                    binding.usernameText.text = it.username
                    binding.followersCountText.text = it.followers.size.toString()
                    binding.followingCountText.text = it.follows.size.toString()
                }
            })
            viewModel.images.observe(this, Observer {
                it?.let { images ->
                    mAdapter.updateImages(images)
                    binding.postsCountText.text = images.size.toString()
                }
            })
        }
        //---
    }

    /*private fun setupBottomNavigation() {


        /*binding.bottomNavigationInclude.bottomNavigationView.setItemIconSizeRes(29)
        binding.bottomNavigationInclude.bottomNavigationView.setTextVisibility(false)
        binding.bottomNavigationInclude.bottomNavigationView.enableItemShiftingMode(false)
        binding.bottomNavigationInclude.bottomNavigationView.enableShiftingMode(false)
        binding.bottomNavigationInclude.bottomNavigationView.enableAnimation(false)*/
        /*for (i in 0 until binding.bottomNavigationInclude.bottomNavigationView.menu.size()) {
            binding.bottomNavigationInclude.bottomNavigationView.setIconTintList(i, null)
        }*/
        binding.bottomNavigationInclude.bottomNavigationView.setOnNavigationItemSelectedListener {
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

    companion object {
        const val TAG = "ProfileActivity"
    }
}