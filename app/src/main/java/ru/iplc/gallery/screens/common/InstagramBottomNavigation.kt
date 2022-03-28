package ru.iplc.gallery.screens.common

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent


//import ru.iplc.gallery.databinding.ActivityCommentsBinding
import ru.iplc.gallery.models.Notification
import ru.iplc.gallery.models.NotificationType
import ru.iplc.gallery.screens.home.HomeActivity
import ru.iplc.gallery.screens.notifications.NotificationsActivity
import ru.iplc.gallery.screens.notifications.NotificationsViewModel
import ru.iplc.gallery.screens.profile.ProfileActivity
import ru.iplc.gallery.screens.search.SearchActivity
import ru.iplc.gallery.screens.share.ShareActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nhaarman.supertooltips.ToolTip
import com.nhaarman.supertooltips.ToolTipRelativeLayout
import com.nhaarman.supertooltips.ToolTipView
import ru.iplc.gallery.R
//import kotlinx.android.synthetic.main.bottom_navigation_view.*
//import ru.iplc.gallery.databinding.BottomNavigationViewBinding
//import kotlinx.android.synthetic.main.notifications_tooltip_content.view.*
import ru.iplc.gallery.databinding.NotificationsTooltipContentBinding
import ru.iplc.gallery.databinding.BottomNavigationViewBinding

class InstagramBottomNavigation(
    private val uid: String,
    private val bnv: BottomNavigationView,
    private val tooltipLayout: ToolTipRelativeLayout,
    private val navNumber: Int,
    private val activity: BaseActivity
) : LifecycleObserver {

    private lateinit var mViewModel: NotificationsViewModel
    private lateinit var mNotificationsContentView: View
    private var lastTooltipView: ToolTipView? = null
    //+++
    private lateinit var binding :NotificationsTooltipContentBinding



    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        mViewModel = activity.initViewModel()
        mViewModel.init(uid)
        mNotificationsContentView = activity.layoutInflater.inflate(
            R.layout.notifications_tooltip_content, null, false
        )
        mViewModel.notifications.observe(activity, Observer {
            it?.let {
                showNotifications(it)
            }
        })
        binding = NotificationsTooltipContentBinding.bind(mNotificationsContentView)
    }

    private fun showNotifications(notifications: List<Notification>) {
        if (lastTooltipView != null) {
            val parent = mNotificationsContentView.parent
            if (parent != null) {
                (parent as ViewGroup).removeView(mNotificationsContentView)
                lastTooltipView?.remove()
            }
            lastTooltipView = null
        }

        val newNotifications = notifications.filter { !it.read }
        val newNotificationsMap = newNotifications
            .groupBy { it.type }
            .mapValues { (_, values) -> values.size }

        fun setCount(image: ImageView, textView: TextView, type: NotificationType) {
            val count = newNotificationsMap[type] ?: 0
            if (count == 0) {
                image.visibility = View.GONE
                textView.visibility = View.GONE
            } else {
                image.visibility = View.VISIBLE
                textView.visibility = View.VISIBLE
                textView.text = count.toString()
            }
        }

        with(mNotificationsContentView) {
            setCount(binding.likesImage, binding.likesCountText, NotificationType.Like)
            setCount(binding.followsImage, binding.followsCountText, NotificationType.Follow)
            setCount(binding.commentsImage, binding.commentsCountText, NotificationType.Comment)
        }

        if (newNotifications.isNotEmpty()) {
            bnv.getOrCreateBadge(R.id.nav_item_likes).number = newNotifications.size

            val tooltip = ToolTip()
                .withColor(ContextCompat.getColor(activity, R.color.red))
                .withContentView(mNotificationsContentView)
                .withAnimationType(ToolTip.AnimationType.FROM_TOP)
                .withShadow()
            lastTooltipView = tooltipLayout.showToolTipForView(
                tooltip,
                (bnv[0] as ViewGroup)[NOTIFICATIONS_ICON_POS]
            )
            lastTooltipView?.setOnToolTipViewClickedListener {
                mViewModel.setNotificationsRead(newNotifications)
                (bnv[0] as ViewGroup)[NOTIFICATIONS_ICON_POS].performClick()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        bnv.menu.getItem(navNumber).isChecked = true
    }

    init {
        bnv.setOnNavigationItemSelectedListener {
            val nextActivity =
                when (it.itemId) {
                    R.id.nav_item_home -> HomeActivity::class.java
                    R.id.nav_item_search -> SearchActivity::class.java
                    R.id.nav_item_share -> ShareActivity::class.java
                    R.id.nav_item_likes -> NotificationsActivity::class.java
                    R.id.nav_item_profile -> ProfileActivity::class.java
                    else -> {
                        Log.e(BaseActivity.TAG, "unknown nav item clicked $it")
                        null
                    }
                }
            if (nextActivity != null) {
                val intent = Intent(activity, nextActivity)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                activity.startActivity(intent)
                activity.overridePendingTransition(0, 0)
                true
            } else {
                false
            }
        }
    }

    companion object {
        const val NOTIFICATIONS_ICON_POS = 3
    }
}

fun BaseActivity.setupBottomNavigation(uid: String, navNumber: Int, navigationBinding: BottomNavigationViewBinding) {
    /*lateinit var binding1 :BottomNavigationViewBinding
    lateinit var mBottomNavigationView: View*/

    /*
    binding = ActivityEditProfileBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_edit_profile)
        setContentView(binding.root)
     */
    /*binding1 = BottomNavigationViewBinding.inflate(layoutInflater)
    setContentView(binding1.root)*/
    /*mBottomNavigationView = this.layoutInflater.inflate(
        R.layout.bottom_navigation_view, null, false
    )
    binding1 = BottomNavigationViewBinding.bind(mBottomNavigationView)*/
    //binding1.testText.setText("rrrr")
    val bnv =
        InstagramBottomNavigation(uid, navigationBinding.bottomNavigationView, navigationBinding.tooltipLayout, navNumber, this)

    this.lifecycle.addObserver(bnv)
}