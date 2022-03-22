package com.alexbezhan.instagram.screens.share

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.alexbezhan.instagram.R
import com.alexbezhan.instagram.data.firebase.common.FirebaseHelper
import com.alexbezhan.instagram.databinding.ActivitySearchBinding
import com.alexbezhan.instagram.databinding.ActivityShareBinding
import com.alexbezhan.instagram.models.User
import com.alexbezhan.instagram.screens.common.BaseActivity
import com.alexbezhan.instagram.screens.common.CameraHelper
import com.alexbezhan.instagram.screens.common.loadImage
import com.alexbezhan.instagram.screens.common.setupAuthGuard
//import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : BaseActivity() {
    private lateinit var mCamera: CameraHelper
    private lateinit var mFirebase: FirebaseHelper
    private lateinit var mUser: User
    private lateinit var mViewModel: ShareViewModel
    private lateinit var binding: ActivityShareBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_share)
        binding = ActivityShareBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate")

        setupAuthGuard {
            mViewModel = initViewModel()
            mFirebase = FirebaseHelper(this)

            mCamera = CameraHelper(this)
            mCamera.takeCameraPicture()

            binding.backImage.setOnClickListener { finish() }
            binding.shareText.setOnClickListener { share() }

            mViewModel.user.observe(this, Observer {
                it?.let {
                    mUser = it
                }
            })
            mViewModel.shareCompletedEvent.observe(this, Observer {
                finish()
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mCamera.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                binding.postImage.loadImage(mCamera.imageUri?.toString())
            } else {
                finish()
            }
        }
    }

    private fun share() {
        mViewModel.share(mUser, mCamera.imageUri, binding.captionInput.text.toString())
    }

    companion object {
        const val TAG = "ShareActivity"
    }
}