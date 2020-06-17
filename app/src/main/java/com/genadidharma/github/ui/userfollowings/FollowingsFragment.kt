package com.genadidharma.github.ui.userfollowings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.genadidharma.github.R
import com.genadidharma.github.model.UserFollowingsItem
import com.genadidharma.github.repository.UserFollowingsRepository
import com.genadidharma.github.ui.userfollowings.viewmodel.UserFollowingsViewModel
import com.genadidharma.github.ui.userfollowings.viewmodel.UserFollowingsViewModelFactory
import com.genadidharma.github.ui.util.Constants
import kotlinx.android.synthetic.main.fragment_followings.*

class FollowingsFragment : Fragment() {
    companion object {
        const val USERNAME_TAG = "username"
        private val TAG = FollowingsFragment::class.java.simpleName
        private lateinit var viewModel: UserFollowingsViewModel
    }

    private var username: String? = null
    private lateinit var adapter: UserFollowingsAdapter
    private lateinit var skeleton: Skeleton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = this.arguments
        username = bundle?.getString(USERNAME_TAG)

        adapter = UserFollowingsAdapter()
        rv_following.adapter = adapter
        skeleton =
            rv_following.applySkeleton(R.layout.user_list_item, Constants.SKELETON_ITEM_COUNT)
                .apply {
                    showSkeleton()
                }

        username?.let { setupViewModel(it) }
    }

    fun newInstance(username: String?): Fragment {
        val fragment = FollowingsFragment()
        val bundle = Bundle()
        bundle.putString(USERNAME_TAG, username)
        fragment.arguments = bundle
        return fragment
    }

    private fun setupViewModel(username: String) {
        val factory =
            UserFollowingsViewModelFactory(
                UserFollowingsRepository.instance
            )
        viewModel =
            ViewModelProvider(this, factory).get(UserFollowingsViewModel::class.java).apply {
                viewState.observe(
                    viewLifecycleOwner,
                    Observer(this@FollowingsFragment::handleState)
                )
                getFollowings(username)
            }
    }

    private fun handleState(viewState: UserFollowingsViewState?) {
        viewState?.let {
            toggleLoading(it.loading)
            it.data?.let { data -> showData(data) }
            it.error?.let { error -> showError(error) }
        }
    }

    private fun toggleLoading(loading: Boolean) {
        if (!loading) skeleton.showOriginal()
    }

    private fun showData(data: MutableList<UserFollowingsItem>) {
        adapter.updateData(data)
        tv_error.visibility = View.GONE
        rv_following.visibility = View.VISIBLE
    }

    private fun showError(e: Exception) {
/*        tv_error.visibility = View.VISIBLE
        tv_error.text = e.message
        rv_user.visibility = View.GONE*/
        Log.e(TAG, "Error: $e")
    }
}