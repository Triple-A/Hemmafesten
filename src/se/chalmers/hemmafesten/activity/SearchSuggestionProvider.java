package se.chalmers.hemmafesten.activity;

import android.content.SearchRecentSuggestionsProvider;

public class SearchSuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "se.chalmers.hemmafesten.activity.SearchSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;


    /**
     * get search suggestion which in this case are recent searches
     */
    public SearchSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}