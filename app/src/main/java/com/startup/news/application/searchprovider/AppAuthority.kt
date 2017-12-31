package com.startup.news.application.searchprovider

import android.content.SearchRecentSuggestionsProvider.DATABASE_MODE_2LINES
import android.content.SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES

/**
 * Created by admin on 12/30/2017.
 */

object AppAuthority {
    internal val AUTHORITY = "com.startup.news.application.searchprovider.SearchContentProvider"
    internal val MODE = DATABASE_MODE_QUERIES or DATABASE_MODE_2LINES
}
