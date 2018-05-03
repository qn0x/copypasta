package xyz.qn0x.copypasta;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import xyz.qn0x.copypasta.persistence.entities.Snippet;

public class SnippetViewModel extends AndroidViewModel {
    private SnippetRepository snippetRepository;
    private LiveData<List<Snippet>> allSnippets;

    public SnippetViewModel(Application application) {
        super(application);
        snippetRepository = new SnippetRepository(application);
        allSnippets = snippetRepository.getAllSnippets();
    }

    LiveData<List<Snippet>> getAllSnippets() {
        return allSnippets;
    }

    public void insert(Snippet snippet) {
        snippetRepository.insert(snippet);
    }
}
