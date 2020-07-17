package com.genadidharma.github.ui.usersearch

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.genadidharma.github.R
import com.genadidharma.github.model.UserFavoriteItem
import com.genadidharma.github.model.UserSearchItem
import com.genadidharma.github.repository.UserFavoriteRepository
import com.genadidharma.github.repository.usersearch.UserSearchRepository
import com.genadidharma.github.ui.userfavorites.UserFavoritesViewState
import com.genadidharma.github.ui.userfavorites.viewmodel.UserFavoritesViewModel
import com.genadidharma.github.ui.userfavorites.viewmodel.UserFavoritesViewModelFactory
import com.genadidharma.github.ui.usersearch.viewmodel.UserSearchViewModel
import com.genadidharma.github.ui.usersearch.viewmodel.UserSearchViewModelFactory
import com.genadidharma.github.ui.util.CircleTransform
import com.genadidharma.github.ui.util.Constants
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        const val USER_FAVORITE_ITEM_TAG = "favorite"
        const val USER_IS_FAVORITE_TAG = "isFavorite"
        const val USER_POSITION_TAG = "position"
        const val REQUEST_LIKE = 200
        const val RESULT_LIKE = 300
    }

    private var viewModel: UserSearchViewModel? = null
    private var userFavoritesViewModel: UserFavoritesViewModel? = null
    private lateinit var adapter: UserSearchAdapter
    private lateinit var skeleton: Skeleton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        adapter = UserSearchAdapter(this)
        rv_user.adapter = adapter
        skeleton = rv_user.applySkeleton(R.layout.user_list_item, Constants.SKELETON_ITEM_COUNT)
        setupViewModel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_LIKE -> {
                when(resultCode){
                    RESULT_LIKE -> {
                        val bundle = data?.extras
                        val position = bundle!!.getInt(USER_POSITION_TAG)
                        val isFavorite = bundle.getBoolean(USER_IS_FAVORITE_TAG)
                        adapter.updateItem(position, isFavorite)
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
                viewModel?.username = query!!
                userFavoritesViewModel?.apply {
                    getFavorites()
                }
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
        }

        val userFavoriteFactory = UserFavoritesViewModelFactory(UserFavoriteRepository.instance)
        userFavoritesViewModel = ViewModelProvider(this, userFavoriteFactory).get(UserFavoritesViewModel::class.java).apply {
            viewState.observe(this@MainActivity, Observer(this@MainActivity::handleUserFavoritesState))
        }

        srl_user.setOnRefreshListener {
            viewModel?.apply {
                username?.let { getUsers(it) }
            }
            userFavoritesViewModel?.apply {
                getFavorites()
            }
        }
    }

    private fun handleState(viewState: UserSearchViewState?) {
        viewState?.let {
            toggleLoading(it.loading)
            it.data?.let { data -> showData(data) }
            it.error?.let { error -> showError(error) }
        }
    }

    private fun handleUserFavoritesState(viewState: UserFavoritesViewState?) {
        viewState?.let {
            it.data?.let { data -> showDataFavorite(data) }
        }
    }

    private fun toggleLoading(loading: Boolean) {
        if (!loading) skeleton.showOriginal() else {
            gr_search_hint.visibility = View.GONE
            skeleton.showSkeleton()
        }
        if (srl_user.isRefreshing) srl_user.isRefreshing = loading
    }

    private fun showData(data: MutableList<UserSearchItem>) {
        rv_user.visibility = View.VISIBLE
        gr_error.visibility = View.GONE

        adapter.updateData(data)
        rv_user.visibility = View.VISIBLE
        gr_error.visibility = View.GONE
        gr_search_hint.visibility = View.GONE
    }

    private fun showError(e: Exception) {
        rv_user.visibility = View.GONE
        tv_error.text = e.message
        gr_error.visibility = View.VISIBLE
        gr_search_hint.visibility = View.GONE
    }

    private fun showDataFavorite(data: MutableList<UserFavoriteItem>){
        Log.e("Favorite data", data.toString())
        adapter.updateFavoriteData(data)
    }

}