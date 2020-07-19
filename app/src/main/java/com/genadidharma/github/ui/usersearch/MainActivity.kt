package com.genadidharma.github.ui.usersearch

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
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
import com.genadidharma.github.ui.settings.SettingsActivity
import com.genadidharma.github.ui.userdetail.UserDetailActivity
import com.genadidharma.github.ui.usersearch.viewmodel.UserSearchViewModel
import com.genadidharma.github.ui.usersearch.viewmodel.UserSearchViewModelFactory
import com.genadidharma.github.ui.util.CircleTransform
import com.genadidharma.github.ui.util.Constants
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
        const val USER_ITEM_IS_FAVORITE_TAG = "user_item_is_favorite"
        const val USER_ITEM_IS_FAVORITE_REQUEST_CODE = 201
        const val USER_ITEM_IS_FAVORITE_RESULT_CODE = 301
    }

    private lateinit var viewModel: UserSearchViewModel
    private lateinit var adapter: UserSearchAdapter
    private lateinit var skeleton: Skeleton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        adapter = UserSearchAdapter() { userSearchItem ->
            val intent = Intent(this@MainActivity, UserDetailActivity::class.java)
            intent.putExtra(USER_FAVORITE_ITEM_TAG, userSearchItem)
            startActivityForResult(intent, USER_ITEM_IS_FAVORITE_REQUEST_CODE)
        }

        rv_user.adapter = adapter.withLoadStateHeaderAndFooter(
            header = UserSearchLoadStateAdapter { adapter.retry() },
            footer = UserSearchLoadStateAdapter { adapter.retry() }
        )

        skeleton = rv_user.applySkeleton(R.layout.user_list_item, Constants.SKELETON_ITEM_COUNT)

        adapter.addLoadStateListener { loadStates ->

            // Handle initial load state
            toggleLoading(loadStates.refresh is LoadState.Loading)

            // Handle initial error load state
            if (loadStates.refresh is LoadState.Error) {
                val errorState = loadStates.refresh as LoadState.Error
                showError(errorState.error)
            }
        }

        setupViewModel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            USER_ITEM_IS_FAVORITE_RESULT_CODE -> {
                val isFavorite = data?.getBooleanExtra(USER_ITEM_IS_FAVORITE_TAG, false)
                isFavorite?.let {
                    if (it) rv_user.smoothScrollToPosition(0)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.ic_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView? = searchItem?.actionView as SearchView
        searchView?.queryHint = resources.getString(R.string.search_view_hint)
        searchView?.findViewById<View>(androidx.appcompat.R.id.search_plate)
            ?.setBackgroundColor(Color.TRANSPARENT)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_profile -> {
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setupAvatar(menu: Menu) {
        val profileItem = menu.findItem(R.id.ic_profile)
        Picasso
            .get()
            .load(R.drawable.img_avatar)
            .transform(CircleTransform())
            .error(R.drawable.img_avatar)
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