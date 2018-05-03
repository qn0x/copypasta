package xyz.qn0x.copypasta;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import xyz.qn0x.copypasta.persistence.SnippetDatabase;
import xyz.qn0x.copypasta.persistence.dao.SnippetDao;
import xyz.qn0x.copypasta.persistence.entities.Snippet;

public class SnippetRepository {
    private SnippetDao snippetDao;
    private LiveData<List<Snippet>> allSnippets;

    SnippetRepository(Application application) {
        SnippetDatabase db = SnippetDatabase.getDatabase(application);
        snippetDao = db.snippetDao();
        allSnippets = snippetDao.getAllSnippets();
    }

    LiveData<List<Snippet>> getAllSnippets() {
        return allSnippets;
    }

    public void insert (Snippet snippet) {
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
}
