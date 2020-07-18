package com.genadidharma.githubconsumer.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserFavoriteItem(

    val id: Int,

    val login: String,

    val gistsUrl: String? = null,

    val reposUrl: String? = null,

    val followingUrl: String? = null,

    val starredUrl: String? = null,

    val followersUrl: String? = null,

    val type: String? = null,

    val url: String? = null,

    val subscriptionsUrl: String? = null,

    val score: Int? = null,

    val receivedEventsUrl: String? = null,

    val avatarUrl: String? = null,

    val eventsUrl: String? = null,

    val htmlUrl: String? = null,

    val siteAdmin: Boolean? = null,

    val gravatarId: String? = null,

    val nodeId: String? = null,

    val organizationsUrl: String? = null,

    var indexInResponse: Int = -1,

    var isFavorite: Boolean = false
) : Parcelable{
    class UserFavoriteItem{
        companion object{
            private const val AUTHORITY = "com.genadidharma.github"
            private const val SCHEME = "content"
            private const val TABLE_NAME = "user_search"

            const val ID = "id"
            const val AVATAR_URL = "avatarUrl"
            const val LOGIN = "login"
            const val TYPE = "type"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}
