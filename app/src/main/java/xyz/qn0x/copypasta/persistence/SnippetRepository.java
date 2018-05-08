package xyz.qn0x.copypasta.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import xyz.qn0x.copypasta.persistence.dao.SnippetDao;
import xyz.qn0x.copypasta.persistence.dao.SnippetTagsDao;
import xyz.qn0x.copypasta.persistence.dao.TagDao;
import xyz.qn0x.copypasta.persistence.entities.Snippet;
import xyz.qn0x.copypasta.persistence.entities.SnippetTags;
import xyz.qn0x.copypasta.persistence.entities.Tag;
import xyz.qn0x.copypasta.ui.activities.MainActivity;

public class SnippetRepository {
    private SnippetDao snippetDao;
    private TagDao tagDao;
    private SnippetTagsDao snippetTagsDao;
    private LiveData<List<Snippet>> allSnippets;
    private LiveData<List<Tag>> allTags;
    private LiveData<List<SnippetTags>> allSnippetTags;

    public SnippetRepository(Application application) {
        SnippetDatabase db = SnippetDatabase.getDatabase(application);
        snippetDao = db.snippetDao();
        tagDao = db.tagDao();
        snippetTagsDao = db.snippetTagsDao();
        allTags = tagDao.getAllTags();
        allSnippets = snippetDao.getAllSnippets();
        allSnippetTags = snippetTagsDao.getAllSnippetTags();
    }

    public long insert(Snippet snippet) {
        new insertAsyncTask(snippetDao).execute(snippet);
        return snippet.getId();
    }

    // make sure data is inserted outside of UI thread
    private static class insertAsyncTask extends AsyncTask<Snippet, Void, Long> {

        private SnippetDao mAsyncTaskDao;

        insertAsyncTask(SnippetDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Long doInBackground(final Snippet... params) {
            return mAsyncTaskDao.insert(params[0]);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            // TODO
            MainActivity.saveTagsForSnippetId(MainActivity.TAGS_LIST, aLong);
            System.out.println("ID is " + aLong);
        }
    }

    public List<Long> insert(Tag... tags) {
        return new insertTagsAsyncTask(tagDao).execute(tags);
    }

    private static class insertTagsAsyncTask extends AsyncTask<Tag[], Void, List<Long>> {

        private TagDao mAsyncTaskDao;

        public insertTagsAsyncTask(TagDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected List<Long> doInBackground(Tag[]... tags) {
            return null;
        }
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
}
