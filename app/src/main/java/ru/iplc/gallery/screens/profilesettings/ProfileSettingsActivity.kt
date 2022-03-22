package ru.iplc.gallery.screens.profilesettings

import android.os.Bundle
import ru.iplc.gallery.R
import ru.iplc.gallery.databinding.ActivityProfileBinding
import ru.iplc.gallery.databinding.ActivityProfileSettingsBinding
import ru.iplc.gallery.screens.common.BaseActivity
import ru.iplc.gallery.screens.common.setupAuthGuard
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
