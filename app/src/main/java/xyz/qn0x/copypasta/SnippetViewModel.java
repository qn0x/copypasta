package xyz.qn0x.copypasta;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

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

    public LiveData<List<Snippet>> getSnippetsByName(String query) {
        return snippetRepository.getSnippetsByName("%" + query + "%");
    }
}
