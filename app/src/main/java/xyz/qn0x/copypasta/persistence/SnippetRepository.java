package xyz.qn0x.copypasta.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.LinkedList;
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
        // TODO
        new insertSnippetTask(snippetDao, tagDao, snippetTagsDao).execute(snippet);
        return snippet.getId();
    }

    // make sure data is inserted outside of UI thread
    private static class insertSnippetTask extends AsyncTask<Snippet, Void, Long> {

        private SnippetDao snippetDao;
        private TagDao tagDao;
        private SnippetTagsDao snippetTagsDao;
        private Snippet snippet;

        insertSnippetTask(SnippetDao sDao, TagDao tDao, SnippetTagsDao stDao) {
            snippetDao = sDao;
            tagDao = tDao;
            snippetTagsDao = stDao;
        }

        @Override
        protected Long doInBackground(final Snippet... params) {
            snippet = params[0];
            return snippetDao.insert(params[0]);
        }

        @Override
        protected void onPostExecute(Long snippetId) {
            snippet.setId(snippetId);
            new insertTagsTask(tagDao, snippetTagsDao, snippet).execute(snippet.getTags()
                    .toArray(new Tag[snippet.getTags().size()]));
        }
    }

    public void insert(Tag... tags) {
        new insertTagsTask(tagDao).execute(tags);
    }

    private static class insertTagsTask extends AsyncTask<Tag, Void, List<Long>> {

        private TagDao mAsyncTaskDao;
        private Snippet snippet;
        private SnippetTagsDao snippetTagsDao;

        insertTagsTask(TagDao dao) {
            mAsyncTaskDao = dao;
        }

        insertTagsTask(TagDao dao, SnippetTagsDao snippetTagsDao, Snippet snippet) {
            mAsyncTaskDao = dao;
            this.snippet = snippet;
            this.snippetTagsDao = snippetTagsDao;
        }

        @Override
        protected List<Long> doInBackground(Tag... tags) {
            return mAsyncTaskDao.insert(tags);
        }

        @Override
        protected void onPostExecute(List<Long> tagIds) {
            tagIds.forEach(System.out::println);
            List<SnippetTags> snippetTags = new LinkedList<>();
            tagIds.forEach(id -> snippetTags.add(new SnippetTags(snippet.getId(), id)));
            System.out.println(snippet.getId());
            new insertSnippetTagsTask(snippetTagsDao).execute(snippetTags.toArray(new SnippetTags[snippetTags.size()]));
        }
    }

    private static class insertSnippetTagsTask extends AsyncTask<SnippetTags, Void, Void> {

        private SnippetTagsDao snippetTagsDao;

        insertSnippetTagsTask(SnippetTagsDao snippetTagsDao) {
            this.snippetTagsDao = snippetTagsDao;
        }

        @Override
        protected Void doInBackground(SnippetTags... snippetTags) {
            snippetTagsDao.insert(snippetTags);
            return null;
        }
    }

    // -----------------------------------------------------------------------------------------
    // repository contents
    // -----------------------------------------------------------------------------------------

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
