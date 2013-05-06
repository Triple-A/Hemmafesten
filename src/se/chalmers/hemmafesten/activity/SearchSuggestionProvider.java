package se.chalmers.hemmafesten.activity;

import android.content.SearchRecentSuggestionsProvider;

public class SearchSuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "se.chalmers.hemmafesten.activity.SearchSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    
    public SearchSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}