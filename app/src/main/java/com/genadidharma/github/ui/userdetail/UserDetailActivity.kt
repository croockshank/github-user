package com.genadidharma.github.ui.userdetail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.createSkeleton
import com.genadidharma.github.R
import com.genadidharma.github.model.UserDetailItem
import com.genadidharma.github.model.UserSearchItem
import com.genadidharma.github.repository.UserDetailRepository
import com.genadidharma.github.repository.usersearch.UserSearchRepository
import com.genadidharma.github.ui.userdetail.viewmodel.UserDetailViewModel
import com.genadidharma.github.ui.userdetail.viewmodel.UserDetailViewModelFactory
import com.genadidharma.github.ui.usersearch.MainActivity
import com.genadidharma.github.ui.util.Constants
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_user_detail.*
import kotlinx.android.synthetic.main.activity_user_detail_skeleton.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class UserDetailActivity : AppCompatActivity() {

    private var userSearchItem: UserSearchItem? = null
    private lateinit var viewModel: UserDetailViewModel
    private lateinit var skeleton: Skeleton
    private val userSearchRepository: UserSearchRepository = UserSearchRepository.instance

    private var isFavorite = false
    private var userId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail_skeleton)

        userSearchItem = intent.getParcelableExtra(MainActivity.USER_FAVORITE_ITEM_TAG)
        setupViewPager()

        skeleton = skl_user_detail.createSkeleton()
        skeleton.showSkeleton()

        userSearchItem?.also {
            setupViewModel(it.login.toString())
            isFavorite = it.isFavorite
            userId = it.id
        }

        toggleFavorite()

        setupTransition()

        fab_favorite.setOnClickListener {
            if (isFavorite) {
                userId?.let { userId -> updateToNotFavorite(userId) }
            } else {
                userId?.let { userId -> updateToFavorite(userId) }
            }
            toggleFavorite()
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(MainActivity.USER_ITEM_IS_FAVORITE_TAG, isFavorite)
        setResult(MainActivity.USER_ITEM_IS_FAVORITE_RESULT_CODE, intent)
        super.onBackPressed()
    }

    private fun setupViewPager() {
        val userDetailViewPagerAdapter =
            UserDetailViewPagerAdapter(this, supportFragmentManager, userSearchItem?.login)
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

                when (p1) {
                    R.id.expanded -> fab_favorite.extend()
                    R.id.collapsed -> fab_favorite.shrink()
                }
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
        ml_detail.visibility = View.VISIBLE
        ll_error.visibility = View.GONE

        Picasso.get().load(data.avatarUrl)
            .transform(BlurTransformation(this, Constants.BLUR_RADIUS)).into(iv_banner)
        Picasso.get().load(data.avatarUrl).into(iv_avatar)
        tv_title.text = data.login ?: "-"
        tv_name.text = data.name ?: "-"
        tv_email.text = data.email ?: "-"
        tv_repository.text = Constants.convertNumber(data.publicRepos)
        tv_followers.text = Constants.convertNumber(data.followers)
        tv_following.text = Constants.convertNumber(data.following)
    }

    private fun showError(e: Exception) {
        ml_detail.visibility = View.GONE
        tv_error.text = e.message
        ll_error.visibility = View.VISIBLE
    }

    private fun toggleFavorite() {
        if (isFavorite) {
            fab_favorite.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_star_24)
            fab_favorite.text = getString(R.string.favorite)
        } else {
            fab_favorite.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_baseline_star_border_24)
            fab_favorite.text = getString(R.string.not_favorite)
        }
    }

    private fun updateToFavorite(userId: Int) {
        lifecycleScope.launch {
            userSearchRepository.updateToFavorite(userId)
        }
        isFavorite = true
    }

    private fun updateToNotFavorite(userId: Int) {
        lifecycleScope.launch {
            userSearchRepository.updateToNotFavorite(userId)
        }
        isFavorite = false
    }
}