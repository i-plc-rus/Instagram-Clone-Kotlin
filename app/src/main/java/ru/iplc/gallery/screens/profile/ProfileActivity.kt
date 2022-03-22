package ru.iplc.gallery.screens.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import ru.iplc.gallery.R
import ru.iplc.gallery.databinding.ActivityProfileBinding
import ru.iplc.gallery.screens.addfriends.AddFriendsActivity
import ru.iplc.gallery.screens.common.*
import ru.iplc.gallery.screens.editprofile.EditProfileActivity
import ru.iplc.gallery.screens.profilesettings.ProfileSettingsActivity
//import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity() {
    private lateinit var mAdapter: ImagesAdapter
    private lateinit var binding: ActivityProfileBinding

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
        mAdapter = ImagesAdapter()
        binding.imagesRecycler.adapter = mAdapter

        setupAuthGuard { uid ->
            setupBottomNavigation(uid,4)
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
    }

    companion object {
        const val TAG = "ProfileActivity"
    }
}