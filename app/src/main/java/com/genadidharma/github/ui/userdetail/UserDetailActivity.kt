package com.genadidharma.github.ui.userdetail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.createSkeleton
import com.genadidharma.github.R
import com.genadidharma.github.model.UserDetailItem
import com.genadidharma.github.repository.UserDetailRepository
import com.genadidharma.github.ui.userdetail.viewmodel.UserDetailViewModel
import com.genadidharma.github.ui.userdetail.viewmodel.UserDetailViewModelFactory
import com.genadidharma.github.ui.usersearch.MainActivity
import com.genadidharma.github.ui.util.Constants
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_user_detail.*
import kotlinx.android.synthetic.main.activity_user_detail_skeleton.*

class UserDetailActivity : AppCompatActivity() {

    companion object {
        val TAG = UserDetailActivity::class.java.simpleName
    }

    private var username: String? = null
    private lateinit var viewModel: UserDetailViewModel
    private lateinit var skeleton: Skeleton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail_skeleton)
        username = intent.getStringExtra(MainActivity.USERNAME_ITEM_TAG)
        setupViewPager()

        skeleton = skl_user_detail.createSkeleton()
        skeleton.showSkeleton()

        setupViewModel(username.toString())
        setupTransition()

        ib_back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupViewPager() {
        val userDetailViewPagerAdapter =
            UserDetailViewPagerAdapter(this, supportFragmentManager, username)
        vp_tabs.adapter = userDetailViewPagerAdapter
        vp_tabs.addOnPageChangeListener(object : TabLayout.TabLayoutOnPageChangeListener(tabs) {
            override fun onPageScrollStateChanged(state: Int) {
                toggleRefreshing(state == ViewPager.SCROLL_STATE_DRAGGING)
            }
        })
        tabs.setupWithViewPager(vp_tabs)
    }

    private fun setupTransition() {
        ml_detail.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {

            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {

            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                toggleRefreshing(p1 == R.id.expanded)
            }
        })
    }

    private fun setupViewModel(username: String) {
        val factory =
            UserDetailViewModelFactory(
                UserDetailRepository.instance
            )
        viewModel = ViewModelProvider(this, factory).get(UserDetailViewModel::class.java).apply {
            viewState.observe(
                this@UserDetailActivity,
                Observer(this@UserDetailActivity::handleState)
            )
            getUser(username)
            srl_user_detail.setOnRefreshListener { getUser(username) }
        }
    }

    private fun handleState(viewState: UserDetailViewState?) {
        viewState?.let {
            toggleLoading(it.loading)
            it.data?.let { data -> showData(data) }
            it.error?.let { error -> showError(error) }
        }
    }

    private fun toggleLoading(loading: Boolean) {
        if (!loading) skeleton.showOriginal()
        srl_user_detail.isRefreshing = loading
        vp_tabs.isEnabled = loading
    }

    fun toggleRefreshing(enabled: Boolean) {
        if (srl_user_detail != null) {
            srl_user_detail.isEnabled = enabled
        }
    }

    private fun showData(data: UserDetailItem) {
        gr_content.visibility = View.VISIBLE
        gr_error.visibility = View.GONE
        lt_error_animation.visibility = View.VISIBLE

        Picasso.get().load(data.avatarUrl)
            .transform(BlurTransformation(this, Constants.BLUR_RADIUS)).into(iv_banner)
        Picasso.get().load(data.avatarUrl).into(iv_avatar)
        tv_title.text = data.login ?: "-"
        tv_name.text = data.name ?: "-"
        tv_email.text = data.email ?: "-"
        tv_repository.text = Constants.convertNumber(data.publicRepos)?: "-"
        tv_followers.text = Constants.convertNumber(data.followers) ?: "-"
        tv_following.text = Constants.convertNumber(data.following) ?: "-"
    }

    private fun showError(e: Exception) {
        gr_content.visibility = View.GONE
        tv_error.text = e.message
        gr_error.visibility = View.VISIBLE
        lt_error_animation.visibility = View.GONE
    }
}