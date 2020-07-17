package com.genadidharma.github.ui.usersearch

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.genadidharma.github.R
import com.genadidharma.github.model.UserSearchItem
import com.genadidharma.github.repository.usersearch.UserSearchRepository
import com.genadidharma.github.ui.usersearch.viewmodel.UserSearchViewModel
import com.genadidharma.github.ui.usersearch.viewmodel.UserSearchViewModelFactory
import com.genadidharma.github.ui.util.CircleTransform
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        const val USER_FAVORITE_ITEM_TAG = "favorite"
        const val USER_IS_FAVORITE_TAG = "isFavorite"
        const val USER_POSITION_TAG = "position"
        const val REQUEST_LIKE = 200
        const val RESULT_LIKE = 300
    }

    private lateinit var viewModel: UserSearchViewModel
    private val adapter = UserSearchAdapter()
    private lateinit var skeleton: Skeleton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        setupViewModel()

        rv_user.adapter = adapter.withLoadStateHeaderAndFooter(
            header = UserSearchLoadStateAdapter { adapter.retry() },
            footer = UserSearchLoadStateAdapter { adapter.retry() }
        )

        skeleton = rv_user.applySkeleton(R.layout.user_list_item, 10)

        adapter.addLoadStateListener { loadStates ->

            // Handle initial load state
            toggleLoading(loadStates.refresh is LoadState.Loading)

            // Handle initial error load state
            if (loadStates.refresh is LoadState.Error) {
                val errorState = loadStates.refresh as LoadState.Error
                showError(errorState.error)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_LIKE -> {
                when (resultCode) {
                    RESULT_LIKE -> {
                        val bundle = data?.extras
                        val position = bundle!!.getInt(USER_POSITION_TAG)
                        val isFavorite = bundle.getBoolean(USER_IS_FAVORITE_TAG)
//                        adapter.updateItem(position, isFavorite)
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.ic_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView? = searchItem?.actionView as SearchView
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                updatedUsersFromInput(query.toString())
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        setupAvatar(menu)
        return true
    }

    private fun setupAvatar(menu: Menu) {
        val profileItem = menu.findItem(R.id.ic_profile)
        Picasso
            .get()
            .load(R.drawable.img_avatar)
            .transform(CircleTransform())
            .error(R.drawable.ic_baseline_person_24)
            .into(object : Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    profileItem.icon = errorDrawable
                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    val d = BitmapDrawable(resources, bitmap)
                    profileItem.icon = d
                }

            })
    }

    private fun setupViewModel() {
        val factory =
            UserSearchViewModelFactory(
                UserSearchRepository.instance
            )
        viewModel = ViewModelProvider(this, factory).get(UserSearchViewModel::class.java).apply {
            viewState.observe(this@MainActivity, Observer(this@MainActivity::handleState))
            srl_user.setOnRefreshListener {
                updatedUsersFromInput(keyword.toString())
            }
        }
    }

    private fun updatedUsersFromInput(keyword: String) {
        keyword.trim().let {
            if (it.isNotEmpty()) {
                search(keyword)
                rv_user.smoothScrollToPosition(0)
            }
        }
    }

    private fun search(query: String) {
        viewModel.keyword = query
    }

    private fun handleState(viewState: UserSearchViewState?) {
        viewState?.let {
            it.data?.let { data -> showData(data) }
        }
    }

    private fun toggleLoading(loading: Boolean) {
        if (loading) {
            gr_search_hint.visibility = View.GONE
            gr_error.visibility = View.GONE
            skeleton.showSkeleton()
        } else {
            if (skeleton.isSkeleton()) {
                skeleton.showOriginal()
            }
        }

        if (srl_user.isRefreshing) srl_user.isRefreshing = loading
    }

    private fun showData(data: Flow<PagingData<UserSearchItem>>) {
        lifecycleScope.launch {
            data.collectLatest {
                adapter.submitData(it)
            }
        }
        rv_user.visibility = View.VISIBLE
        gr_error.visibility = View.GONE
        gr_search_hint.visibility = View.GONE
    }

    private fun showError(e: Throwable) {
        rv_user.visibility = View.GONE
        tv_error.text = e.message
        gr_error.visibility = View.VISIBLE
        gr_search_hint.visibility = View.GONE
    }

}