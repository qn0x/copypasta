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
 * And apparently every other activity. This needs refactoring.
 *
 * @author Janine Kostka
 */
public class SnippetViewModel extends AndroidViewModel {

    private final static String TAG = "SnippetViewModel";

    // source of data
    private SnippetRepository snippetRepository;

    // containers that hold the state of the data
    private LiveData<List<Snippet>> allSnippets;
    private List<Tag> allTags;

    public SnippetViewModel(Application application) {
        super(application);
        snippetRepository = new SnippetRepository(application);
        allSnippets = snippetRepository.getAllSnippets();
        allTags = snippetRepository.getAllTags();
    }

    // --------- LiveData ----------
    public LiveData<List<Snippet>> getAllSnippets() {
        return allSnippets;
    }

    public List<Tag> getAllTags() {
        return allTags;
    }


    // --------- insert --------------
    public long insert(Snippet snippet) {
        return snippetRepository.insert(snippet);
    }

    public void insert(Tag tag) {
        snippetRepository.insert(tag);
    }

    public void insert(SnippetTags... snippetTags) {
        snippetRepository.insert(snippetTags);
    }


    // --------- update --------------
    public long updateFavoriteStatus(long snippetId, boolean favorite) {
        return snippetRepository.updateFavoriteStatus(snippetId, favorite);
    }

    public void updateSnippetName(long snippetId, String newName) {
        snippetRepository.updateSnippetName(snippetId, newName);
    }

    public void updateSnippetText(long snippetId, String newText) {
        snippetRepository.updateSnippetText(snippetId, newText);
    }


    // --------- delete --------------
    public void deleteSnippet(Snippet snippet) {
        snippetRepository.deleteSnippet(snippet);
    }

    public void deleteSnippetTagsForSnippetId(long snippetId) {
        snippetRepository.deleteSnippetTagsForSnippetId(snippetId);
    }

    public void deleteStrayTags() {
        snippetRepository.deleteStrayTags();
    }


    // --------- queries --------------
    public List<Long> getSnippetsByName(String query) {
        String sqlQuery = "%" + query + "%";
        Log.d(TAG, "searching database with query: " + sqlQuery);
        return snippetRepository.getSnippetsByName(sqlQuery);
    }

    public List<Long> getSnippetsByTag(String tag) {
        String sqlQuery = "%" + tag + "%";
        return snippetRepository.getSnippetsByTag(sqlQuery);
    }

    public List<Tag> getTagsForSnippetId(long snippetId) {
        return snippetRepository.getTagsForSnippetId(snippetId);
    }

    public Snippet getSnippetForId(long snippetId) {
        Log.d(TAG, "deleting snippet with id: " + String.valueOf(snippetId));
        return snippetRepository.getSnippetForId(snippetId);
    }

    public List<Long> getSnippetsForText(String query) {
        String sqlQuery = "%" + query + "%";
        return snippetRepository.getSnippetsForText(sqlQuery);
    }

}
