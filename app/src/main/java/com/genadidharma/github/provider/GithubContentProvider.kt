package com.genadidharma.github.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.genadidharma.github.db.GithubDatabaseApplication
import java.lang.IllegalArgumentException

class GithubContentProvider : ContentProvider() {

    companion object {
        private const val AUTHORITY = "com.genadidharma.github"
        private const val USER_FAVORITE = 1
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var databaseApplication: GithubDatabaseApplication

        init {
            sUriMatcher.addURI(AUTHORITY, "user_search", USER_FAVORITE)
        }
    }

    override fun onCreate(): Boolean {
        databaseApplication = GithubDatabaseApplication.getInstance(context as Context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor {
        val cursor: Cursor = when (sUriMatcher.match(uri)) {
            USER_FAVORITE -> databaseApplication.userSearchDao().getUsersFavorite()
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        cursor.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }
}
