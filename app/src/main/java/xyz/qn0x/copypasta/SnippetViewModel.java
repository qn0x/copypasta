package xyz.qn0x.copypasta;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import xyz.qn0x.copypasta.persistence.SnippetRepository;
import xyz.qn0x.copypasta.persistence.entities.Snippet;
import xyz.qn0x.copypasta.persistence.entities.SnippetTags;
import xyz.qn0x.copypasta.persistence.entities.Tag;

public class SnippetViewModel extends AndroidViewModel {
    private SnippetRepository snippetRepository;
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
        long id = snippetRepository.insert(snippet);
        return id;
    }
}
