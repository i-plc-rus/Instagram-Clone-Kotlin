package com.alexbezhan.instagram.screens.addfriends

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexbezhan.instagram.R
import com.alexbezhan.instagram.databinding.ActivityAddFriendsBinding
import com.alexbezhan.instagram.models.User
import com.alexbezhan.instagram.screens.common.BaseActivity
import com.alexbezhan.instagram.screens.common.setupAuthGuard
//import kotlinx.android.synthetic.main.activity_add_friends.*

class AddFriendsActivity : BaseActivity(), FriendsAdapter.Listener {
    private lateinit var mUser: User
    private lateinit var mAdapter: FriendsAdapter
    private lateinit var mViewModel: AddFriendsViewModel
    private lateinit var binding: ActivityAddFriendsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFriendsBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_add_friends)
        setContentView(binding.root)

        mAdapter = FriendsAdapter(this)

        setupAuthGuard {
            mViewModel = initViewModel()

            binding.backImage.setOnClickListener { finish() }

            binding.addFriendsRecycler.adapter = mAdapter
            binding.addFriendsRecycler.layoutManager = LinearLayoutManager(this)

            mViewModel.userAndFriends.observe(this, Observer {
                it?.let { (user, otherUsers) ->
                    mUser = user
                    mAdapter.update(otherUsers, mUser.follows)
                }
            })
        }
    }

    override fun follow(uid: String) {
        setFollow(uid, true) {
            mAdapter.followed(uid)
        }
    }

    override fun unfollow(uid: String) {
        setFollow(uid, false) {
            mAdapter.unfollowed(uid)
        }
    }

    private fun setFollow(uid: String, follow: Boolean, onSuccess: () -> Unit) {
        mViewModel.setFollow(mUser.uid, uid, follow)
                .addOnSuccessListener { onSuccess() }
    }
}