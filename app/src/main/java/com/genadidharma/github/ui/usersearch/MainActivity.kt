package com.genadidharma.github.ui.usersearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.genadidharma.github.R
import com.genadidharma.github.model.UserSearchItem
import com.genadidharma.github.repository.UserSearchRepository
import com.genadidharma.github.ui.usersearch.viewmodel.UserSearchViewModel
import com.genadidharma.github.ui.usersearch.viewmodel.UserSearchViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: UserSearchViewModel
    private lateinit var adapter: UserSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = UserSearchAdapter()
        rv_user.adapter = adapter

        val factory =
            UserSearchViewModelFactory(
                UserSearchRepository.instance
            )
        viewModel = ViewModelProvider(this, factory).get(UserSearchViewModel::class.java).apply {
            viewState.observe(this@MainActivity, Observer(this@MainActivity::handleState))
            if (viewState.value?.data == null) getUsers("ad")
            srl_user.setOnRefreshListener { getUsers("ad") }
        }
    }

    private fun handleState(viewState: UserSearchViewState?) {
        viewState?.let {
            toggleLoading(it.loading)
            it.data?.let { data -> showData(data) }
            it.error?.let { error -> showError(error) }
        }
    }

    private fun toggleLoading(loading: Boolean) {
        srl_user.isRefreshing = loading
    }

    private fun showData(data: MutableList<UserSearchItem>) {
        adapter.updateData(data)
        tv_error.visibility = View.GONE
        rv_user.visibility = View.VISIBLE
    }

    private fun showError(e: Exception) {
        tv_error.visibility = View.VISIBLE
        tv_error.text = e.message
        rv_user.visibility = View.GONE
    }

}