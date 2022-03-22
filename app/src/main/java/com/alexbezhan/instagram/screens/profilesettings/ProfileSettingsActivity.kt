package com.alexbezhan.instagram.screens.profilesettings

import android.os.Bundle
import com.alexbezhan.instagram.R
import com.alexbezhan.instagram.databinding.ActivityProfileBinding
import com.alexbezhan.instagram.databinding.ActivityProfileSettingsBinding
import com.alexbezhan.instagram.screens.common.BaseActivity
import com.alexbezhan.instagram.screens.common.setupAuthGuard
//import kotlinx.android.synthetic.main.activity_profile_settings.*

class ProfileSettingsActivity : BaseActivity() {
    private lateinit var binding: ActivityProfileSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSettingsBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_profile_settings)
        setContentView(binding.root)

        setupAuthGuard {
            val viewModel = initViewModel<ProfileSettingsViewModel>()
            binding.signOutText.setOnClickListener { viewModel.signOut() }
            binding.backImage.setOnClickListener { finish() }
        }
    }
}
