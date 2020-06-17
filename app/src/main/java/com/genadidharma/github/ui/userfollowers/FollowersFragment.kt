package com.genadidharma.github.ui.userfollowers

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
import com.genadidharma.github.model.UserFollowersItem
import com.genadidharma.github.repository.UserFollowersRepository
import com.genadidharma.github.ui.userdetail.UserDetailActivity
import com.genadidharma.github.ui.userfollowers.viewmodel.UserFollowersViewModel
import com.genadidharma.github.ui.userfollowers.viewmodel.UserFollowersViewModelFactory
import com.genadidharma.github.ui.util.Constants
import kotlinx.android.synthetic.main.fragment_followers.*

class FollowersFragment : Fragment() {
    companion object {
        const val USERNAME_TAG = "username"
        private val TAG = FollowersFragment::class.java.simpleName
        private lateinit var viewModel: UserFollowersViewModel
    }

    private var username: String? = null
    private lateinit var adapter: UserFollowersAdapter
    private lateinit var skeleton: Skeleton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = this.arguments
        username = bundle?.getString(USERNAME_TAG)

        adapter = UserFollowersAdapter()
        rv_followers.adapter = adapter
        skeleton =
            rv_followers.applySkeleton(R.layout.user_list_item, Constants.SKELETON_ITEM_COUNT)
                .apply {
                    showSkeleton()
                }

        username?.let { setupViewModel(it) }
    }

    fun newInstance(username: String?): Fragment {
        val fragment = FollowersFragment()
        val bundle = Bundle()
        bundle.putString(USERNAME_TAG, username)
        fragment.arguments = bundle
        return fragment
    }

    private fun setupViewModel(username: String) {
        val factory =
            UserFollowersViewModelFactory(
                UserFollowersRepository.instance
            )
        viewModel = ViewModelProvider(this, factory).get(UserFollowersViewModel::class.java).apply {
            viewState.observe(
                viewLifecycleOwner,
                Observer(this@FollowersFragment::handleState)
            )
            getFollowers(username)
        }
    }

    private fun handleState(viewState: UserFollowersViewState?) {
        viewState?.let {
            toggleLoading(it.loading)
            it.data?.let { data -> showData(data) }
            it.error?.let { error -> showError(error) }
        }
    }

    private fun toggleLoading(loading: Boolean) {
        if (!loading) skeleton.showOriginal()
    }

    private fun showData(data: MutableList<UserFollowersItem>) {
        adapter.updateData(data)
        tv_error.visibility = View.GONE
        rv_followers.visibility = View.VISIBLE
    }

    private fun showError(e: Exception) {
/*        tv_error.visibility = View.VISIBLE
        tv_error.text = e.message
        rv_user.visibility = View.GONE*/
        Log.e(TAG, "Error: $e")
    }

}