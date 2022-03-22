package com.alexbezhan.instagram.screens.editprofile

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.alexbezhan.instagram.R
import com.alexbezhan.instagram.databinding.ActivityCommentsBinding
import com.alexbezhan.instagram.databinding.ActivityEditProfileBinding
import com.alexbezhan.instagram.models.User
import com.alexbezhan.instagram.screens.common.*
//import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : BaseActivity(), PasswordDialog.Listener {
    private lateinit var mUser: User
    private lateinit var mPendingUser: User
    private lateinit var mCamera: CameraHelper
    private lateinit var mViewModel: EditProfileViewModel
    private lateinit var binding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_edit_profile)
        setContentView(binding.root)


        mCamera = CameraHelper(this)

        binding.backImage.setOnClickListener { finish() }
        binding.saveImage.setOnClickListener { updateProfile() }
        binding.changePhotoText.setOnClickListener { mCamera.takeCameraPicture() }

        setupAuthGuard {
            mViewModel = initViewModel()

            mViewModel.user.observe(this, Observer {
                it?.let {
                    mUser = it
                    binding.nameInput.setText(mUser.name)
                    binding.usernameInput.setText(mUser.username)
                    binding.websiteInput.setText(mUser.website)
                    binding.bioInput.setText(mUser.bio)
                    binding.emailInput.setText(mUser.email)
                    binding.phoneInput.setText(mUser.phone?.toString())
                    binding.profileImage.loadUserPhoto(mUser.photo)
                }
            })
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mCamera.REQUEST_CODE && resultCode == RESULT_OK) {
            mViewModel.uploadAndSetUserPhoto(mCamera.imageUri!!)
        }
    }

    private fun updateProfile() {
        mPendingUser = readInputs()
        val error = validate(mPendingUser)
        if (error == null) {
            if (mPendingUser.email == mUser.email) {
                updateUser(mPendingUser)
            } else {
                PasswordDialog().show(supportFragmentManager, "password_dialog")
            }
        } else {
            showToast(error)
        }
    }

    private fun readInputs(): User {
        return User(
                name = binding.nameInput.text.toString(),
                username = binding.usernameInput.text.toString(),
                email = binding.emailInput.text.toString(),
                website = binding.websiteInput.text.toStringOrNull(),
                bio = binding.bioInput.text.toStringOrNull(),
                phone = binding.phoneInput.text.toString().toLongOrNull()
        )
    }

    override fun onPasswordConfirm(password: String) {
        if (password.isNotEmpty()) {
            mViewModel.updateEmail(
                    currentEmail = mUser.email,
                    newEmail = mPendingUser.email,
                    password = password)
                    .addOnSuccessListener { updateUser(mPendingUser) }
        } else {
            showToast(getString(R.string.you_should_enter_your_password))
        }
    }

    private fun updateUser(user: User) {
        mViewModel.updateUserProfile(currentUser = mUser, newUser = user)
                .addOnSuccessListener {
                    showToast(getString(R.string.profile_saved))
                    finish()
                }
    }

    private fun validate(user: User): String? =
            when {
                user.name.isEmpty() -> getString(R.string.please_enter_name)
                user.username.isEmpty() -> getString(R.string.please_enter_username)
                user.email.isEmpty() -> getString(R.string.please_enter_email)
                else -> null
            }

    companion object {
        const val TAG = "EditProfileActivity"
    }
}