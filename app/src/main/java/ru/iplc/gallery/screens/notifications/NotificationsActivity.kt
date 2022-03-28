package ru.iplc.gallery.screens.notifications

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ru.iplc.gallery.R
import ru.iplc.gallery.databinding.ActivityNotificationsBinding
import ru.iplc.gallery.screens.common.BaseActivity
import ru.iplc.gallery.screens.common.setupAuthGuard
import ru.iplc.gallery.screens.common.setupBottomNavigation
//import kotlinx.android.synthetic.main.activity_notifications.*

class NotificationsActivity : BaseActivity() {
    private lateinit var mAdapter: NotificationsAdapter
    private lateinit var binding: ActivityNotificationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_notifications)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate")

        setupAuthGuard { uid ->
            setupBottomNavigation(uid,3, binding.bottomNavigationView)
            mAdapter = NotificationsAdapter()
            binding.notificationsRecycler.layoutManager = LinearLayoutManager(this)
            binding.notificationsRecycler.adapter = mAdapter

            val viewModel = initViewModel<NotificationsViewModel>()
            viewModel.init(uid)
            viewModel.notifications.observe(this, Observer {
                it?.let {
                    mAdapter.updateNotifications(it)
                    viewModel.setNotificationsRead(it)
                }
            })
        }
    }

    companion object {
        const val TAG = "LikesActivity"
    }
}
