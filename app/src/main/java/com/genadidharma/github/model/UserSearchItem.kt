package com.genadidharma.github.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "users_search")
data class UserSearchItem(
    @PrimaryKey
    val rowId: Int? = null,

    @field:SerializedName("gists_url")
    val gistsUrl: String? = null,

    @field:SerializedName("repos_url")
    val reposUrl: String? = null,

    @field:SerializedName("following_url")
    val followingUrl: String? = null,

    @field:SerializedName("starred_url")
    val starredUrl: String? = null,

    @field:SerializedName("login")
    val login: String? = null,

    @field:SerializedName("followers_url")
    val followersUrl: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("url")
    val url: String? = null,

    @field:SerializedName("subscriptions_url")
    val subscriptionsUrl: String? = null,

    @field:SerializedName("score")
    val score: Int? = null,

    @field:SerializedName("received_events_url")
    val receivedEventsUrl: String? = null,

    @field:SerializedName("avatar_url")
    val avatarUrl: String? = null,

    @field:SerializedName("events_url")
    val eventsUrl: String? = null,

    @field:SerializedName("html_url")
    val htmlUrl: String? = null,

    @field:SerializedName("site_admin")
    val siteAdmin: Boolean? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("gravatar_id")
    val gravatarId: String? = null,

    @field:SerializedName("node_id")
    val nodeId: String? = null,

    @field:SerializedName("organizations_url")
    val organizationsUrl: String? = null,

    var indexInResponse: Int = -1,

    var isFavorite: Boolean = false
) : Parcelable {
    @Parcelize
    data class UserSearchResponse(
        @field:SerializedName("total_count")
        val totalCount: Int? = null,

        @field:SerializedName("incomplete_results")
        val incompleteResults: Boolean? = null,

        @field:SerializedName("items")
        val items: MutableList<UserSearchItem>? = null
    ) : Parcelable
}
