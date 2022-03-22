package ru.iplc.gallery.screens.addfriends

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ru.iplc.gallery.R
import ru.iplc.gallery.databinding.ActivityAddFriendsBinding
import ru.iplc.gallery.models.User
import ru.iplc.gallery.screens.common.BaseActivity
import ru.iplc.gallery.screens.common.setupAuthGuard
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