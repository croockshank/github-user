package com.genadidharma.github.ui.usersearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.genadidharma.github.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.load_state_item.*

class UserSearchLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<UserSearchLoadStateViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): UserSearchLoadStateViewHolder =
        UserSearchLoadStateViewHolder(
            parent.rootView,
            LayoutInflater.from(parent.context).inflate(R.layout.load_state_item, parent, false),
            retry
        )

    override fun onBindViewHolder(holder: UserSearchLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

}

class UserSearchLoadStateViewHolder(
    private val parentView: View,
    itemView: View,
    private val retry: () -> Unit
) : RecyclerView.ViewHolder(itemView), LayoutContainer {
    override val containerView: View
        get() = itemView

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            if (loadState.error !is NullPointerException) {
                Snackbar.make(
                    parentView,
                    loadState.error.message.toString(),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(R.string.retry) {
                    retry.invoke()
                }.show()
            }
        }
        progress_bar.isVisible = loadState is LoadState.Loading
    }
}