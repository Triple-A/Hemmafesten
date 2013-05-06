package se.chalmers.hemmafesten;

import android.content.SearchRecentSuggestionsProvider;

public class SearchSuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "se.chalmers.hemmafesten.SearchSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    
    public SearchSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}