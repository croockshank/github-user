package com.genadidharma.github.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class UserFavoriteItem(
    @PrimaryKey val favoriteId: Int? = null,

    @field:SerializedName("login")
    val login: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("avatar_url")
    val avatarUrl: String? = null,

    var isFavorite: Boolean = false
) : Parcelable