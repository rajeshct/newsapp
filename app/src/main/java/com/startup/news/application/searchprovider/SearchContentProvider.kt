package com.startup.news.application.searchprovider

import android.content.SearchRecentSuggestionsProvider


/**
 * Created by admin on 12/30/2017.
 */

class SearchContentProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AppAuthority.AUTHORITY, AppAuthority.MODE)
    }
}
