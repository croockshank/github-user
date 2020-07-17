package com.genadidharma.github.ui.userdetail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.createSkeleton
import com.genadidharma.github.R
import com.genadidharma.github.model.UserDetailItem
import com.genadidharma.github.model.UserFavoriteItem
import com.genadidharma.github.repository.UserDetailRepository
import com.genadidharma.github.repository.UserFavoriteRepository
import com.genadidharma.github.ui.userdetail.viewmodel.UserDetailViewModel
import com.genadidharma.github.ui.userdetail.viewmodel.UserDetailViewModelFactory
import com.genadidharma.github.ui.userfavorites.UserFavoritesViewState
import com.genadidharma.github.ui.userfavorites.viewmodel.UserFavoritesViewModel
import com.genadidharma.github.ui.userfavorites.viewmodel.UserFavoritesViewModelFactory
import com.genadidharma.github.ui.usersearch.MainActivity
import com.genadidharma.github.ui.util.Constants
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_user_detail.*
import kotlinx.android.synthetic.main.activity_user_detail_skeleton.*

class UserDetailActivity : AppCompatActivity() {

    private var userFavoriteItem: UserFavoriteItem? = null
    private var position: Int? = null
    private lateinit var viewModel: UserDetailViewModel
    private lateinit var userFavoritesViewModel: UserFavoritesViewModel
    private lateinit var skeleton: Skeleton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail_skeleton)
        userFavoriteItem = intent.getParcelableExtra(MainActivity.USER_FAVORITE_ITEM_TAG)
        position = intent.getIntExtra(MainActivity.USER_POSITION_TAG, 0)
        setupViewPager()

        skeleton = skl_user_detail.createSkeleton()
        skeleton.showSkeleton()

        userFavoriteItem?.apply {
            setupViewModel(login.toString())
        }
        setupTransition()

        fab_favorite.setOnClickListener {
            userFavoriteItem?.also {
                addRemoveFromFavorite(it)
            }
        }

        ib_back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        val bundle = Bundle()
        val intent = Intent()
        position?.let { bundle.putInt(MainActivity.USER_POSITION_TAG, it) }
        userFavoriteItem?.isFavorite?.let { bundle.putBoolean(MainActivity.USER_IS_FAVORITE_TAG, it) }
        intent.putExtras(bundle)
        setResult(MainActivity.RESULT_LIKE, intent)
        super.onBackPressed()
    }

    private fun setupViewPager() {
        val userDetailViewPagerAdapter =
            UserDetailViewPagerAdapter(this, supportFragmentManager, userFavoriteItem?.login)
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

        val userFavoriteFactory = UserFavoritesViewModelFactory(UserFavoriteRepository.instance)
        userFavoritesViewModel =
            ViewModelProvider(this, userFavoriteFactory).get(UserFavoritesViewModel::class.java)
                .apply {
                    viewState.observe(this@UserDetailActivity, Observer(this@UserDetailActivity::handleFavoriteState))
                    isFavorite = userFavoriteItem!!.isFavorite
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
        tv_repository.text = Constants.convertNumber(data.publicRepos)
        tv_followers.text = Constants.convertNumber(data.followers)
        tv_following.text = Constants.convertNumber(data.following)
    }

    private fun showError(e: Exception) {
        gr_content.visibility = View.GONE
        tv_error.text = e.message
        gr_error.visibility = View.VISIBLE
        lt_error_animation.visibility = View.GONE
    }

    private fun addRemoveFromFavorite(userFavoriteItem: UserFavoriteItem) {
        if (!userFavoriteItem.isFavorite) {
            userFavoritesViewModel.addToFavorite(userFavoriteItem)
        } else {
            userFavoritesViewModel.removeFromFavorite(userFavoriteItem.login.toString())
        }
    }

    private fun handleFavoriteState(viewState: UserFavoritesViewState) {
        viewState.let {
            toggleFabFavorite(it.isFavorite)
            it.error.let {
                Snackbar.make(
                    skl_user_detail,
                    getString(R.string.snackbar_error_message),
                    Snackbar.LENGTH_SHORT
                )
                    .setAction(R.string.retry) {
                        addRemoveFromFavorite(userFavoriteItem!!)
                    }
            }
        }
    }

    private fun toggleFabFavorite(isFavorite: Boolean) {
        if (isFavorite) fab_favorite.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_favorite_24))
        else fab_favorite.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_favorite_border_24))

        userFavoriteItem?.isFavorite = isFavorite
    }
}