package xyz.qn0x.copypasta.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import xyz.qn0x.copypasta.persistence.dao.SnippetDao;
import xyz.qn0x.copypasta.persistence.dao.TagDao;
import xyz.qn0x.copypasta.persistence.entities.Snippet;
import xyz.qn0x.copypasta.persistence.entities.Tag;

public class SnippetRepository {
    private SnippetDao snippetDao;
    private TagDao tagDao;
    private LiveData<List<Snippet>> allSnippets;
    private LiveData<List<Tag>> allTags;

    public SnippetRepository(Application application) {
        SnippetDatabase db = SnippetDatabase.getDatabase(application);
        snippetDao = db.snippetDao();
        tagDao = db.tagDao();
        allTags = tagDao.getAllTags();
        allSnippets = snippetDao.getAllSnippets();
    }

    public LiveData<List<Snippet>> getAllSnippets() {
        return allSnippets;
    }

    public void insert(Snippet snippet) {
        new insertAsyncTask(snippetDao).execute(snippet);
    }

    // make sure data is inserted outside of UI thread
    private static class insertAsyncTask extends AsyncTask<Snippet, Void, Void> {

        private SnippetDao mAsyncTaskDao;

        insertAsyncTask(SnippetDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Snippet... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }

    }

    public LiveData<List<Tag>> getAllTags() {
        return allTags;
    }
}
