package xyz.qn0x.copypasta;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

import xyz.qn0x.copypasta.persistence.SnippetRepository;
import xyz.qn0x.copypasta.persistence.entities.Snippet;
import xyz.qn0x.copypasta.persistence.entities.SnippetTags;
import xyz.qn0x.copypasta.persistence.entities.Tag;

/**
 * This class represents the ViewModel for the main activity.
 *
 * @author Janine Kostka
 */
public class SnippetViewModel extends AndroidViewModel {

    private final static String TAG = "SnippetViewModel";

    // source of data
    private SnippetRepository snippetRepository;

    // containers that hold the state of the data
    private LiveData<List<Snippet>> allSnippets;
    private LiveData<List<Tag>> allTags;
    private LiveData<List<SnippetTags>> allSnippetTags;

    public SnippetViewModel(Application application) {
        super(application);
        snippetRepository = new SnippetRepository(application);
        allSnippets = snippetRepository.getAllSnippets();
        allTags = snippetRepository.getAllTags();
        allSnippetTags = snippetRepository.getAllSnippetTags();
    }

    public LiveData<List<SnippetTags>> getAllSnippetTags() {
        return allSnippetTags;
    }

    public LiveData<List<Snippet>> getAllSnippets() {
        return allSnippets;
    }

    public LiveData<List<Tag>> getAllTags() {
        return allTags;
    }

    public long insert(Snippet snippet) {
        return snippetRepository.insert(snippet);
    }

    public void insert(Tag tag) {
        snippetRepository.insert(tag);
    }

    public List<Snippet> getSnippetsByName(String query) {
        String sqlQuery = "%" + query + "%";
        Log.d(TAG, "searching database with query: " + sqlQuery);
        return snippetRepository.getSnippetsByName(sqlQuery);
    }

    public List<Snippet> getAllFavorites() {
        return snippetRepository.getAllFavorites();
    }

    public long updateFavoriteStatus(long snippetId, boolean favorite) {
        return snippetRepository.updateFavoriteStatus(snippetId, favorite);
    }
}
