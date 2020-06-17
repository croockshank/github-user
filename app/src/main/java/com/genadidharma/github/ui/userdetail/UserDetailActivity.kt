package com.genadidharma.github.ui.userdetail

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
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
        setupViewPager()

        username = intent.getStringExtra(MainActivity.USERNAME_ITEM_TAG)
        skeleton = skl_user_detail.createSkeleton()
        skeleton.showSkeleton()

        Log.i(TAG, "Username: $username")

        setupViewModel(username.toString())
    }

    private fun setupViewPager() {
        val userDetailViewPagerAdapter = UserDetailViewPagerAdapter(this, supportFragmentManager)
        vp_tabs.adapter = userDetailViewPagerAdapter
        tabs.setupWithViewPager(vp_tabs)
        vp_tabs.addOnPageChangeListener(object : TabLayout.TabLayoutOnPageChangeListener(tabs) {
            override fun onPageScrollStateChanged(state: Int) {
                toggleRefreshing(state == ViewPager.SCROLL_STATE_DRAGGING)
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
            Log.i(TAG, "data: ${it.data}")
            it.error?.let { error -> showError(error) }
        }
    }

    private fun toggleLoading(loading: Boolean) {
        if (!loading) skeleton.showOriginal()
        srl_user_detail.isRefreshing = loading
    }

    fun toggleRefreshing(enabled: Boolean) {
        if (srl_user_detail != null) {
            srl_user_detail.isEnabled = enabled
        }
    }

    private fun showData(data: UserDetailItem) {
        Picasso.get().load(data.avatarUrl).into(iv_banner)
        Picasso.get().load(data.avatarUrl).into(iv_avatar)
        tv_title.text = data.name
        tv_name.text = data.name
        tv_email.text = data.email
        tv_repository.text = data.publicRepos.toString()
        tv_followers.text = data.followers.toString()
        tv_following.text = data.following.toString()
    }

    private fun showError(e: Exception) {
/*        tv_error.visibility = View.VISIBLE
        tv_error.text = e.message
        rv_user.visibility = View.GONE*/
        Log.e(UserDetailActivity::class.java.simpleName, "Error: $e")
    }
}