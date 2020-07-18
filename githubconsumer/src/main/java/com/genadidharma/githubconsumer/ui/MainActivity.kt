package com.genadidharma.githubconsumer.ui

import android.database.Cursor
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.genadidharma.githubconsumer.R
import com.genadidharma.githubconsumer.model.UserFavoriteItem
import com.genadidharma.githubconsumer.ui.util.MappingHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val LOADER_USER_FAVORITE = 1
    }

    private val userFavoriteAdapter =
        UserFavoriteAdapter()
    private val favoriteList = mutableListOf<UserFavoriteItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_user.adapter = userFavoriteAdapter

        val loaderManager = LoaderManager.getInstance(this)
        loaderManager.initLoader(LOADER_USER_FAVORITE, null, mLoaderCallback)

        srl_user.setOnRefreshListener {
            loaderManager.initLoader(LOADER_USER_FAVORITE, null, mLoaderCallback)
        }
    }

    private val mLoaderCallback: LoaderManager.LoaderCallbacks<Cursor> =
        object : LoaderManager.LoaderCallbacks<Cursor> {
            override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
                return CursorLoader(
                    applicationContext,
                    UserFavoriteItem.UserFavoriteItem.CONTENT_URI,
                    null, null, null, null
                )
            }

            override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
                srl_user.isRefreshing = false
                favoriteList.addAll(MappingHelper.mapCursorToArrayList(data))
                userFavoriteAdapter.updateData(favoriteList)
                toggleViews(favoriteList.size == 0)
            }

            override fun onLoaderReset(loader: Loader<Cursor>) {
                srl_user.isRefreshing = false
            }

            private fun toggleViews(favoriteListEmpty: Boolean) {
                if (favoriteListEmpty) {
                    rv_user.visibility = View.GONE
                    gr_empty.visibility = View.VISIBLE
                } else {
                    rv_user.visibility = View.VISIBLE
                    gr_empty.visibility = View.GONE
                }
            }

        }
}
