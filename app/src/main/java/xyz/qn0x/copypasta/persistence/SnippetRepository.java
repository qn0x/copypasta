package xyz.qn0x.copypasta.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import xyz.qn0x.copypasta.persistence.dao.SnippetDao;
import xyz.qn0x.copypasta.persistence.dao.SnippetTagsDao;
import xyz.qn0x.copypasta.persistence.dao.TagDao;
import xyz.qn0x.copypasta.persistence.entities.Snippet;
import xyz.qn0x.copypasta.persistence.entities.SnippetTags;
import xyz.qn0x.copypasta.persistence.entities.Tag;

/**
 * Repository for snippets.
 * <p>
 * Handles CRUD operations for snippets.
 *
 * @author Janine Kostka
 */
public class SnippetRepository {

    private static final String TAG = "SnippetRepository";

    private SnippetDao snippetDao;
    private TagDao tagDao;
    private SnippetTagsDao snippetTagsDao;

    // containers that hold the state of the db content in the application
    private LiveData<List<Snippet>> allSnippets;
    private LiveData<List<Tag>> allTags;
    private LiveData<List<SnippetTags>> allSnippetTags;
    private List<Snippet> allFavorites;


    public SnippetRepository(Application application) {
        SnippetDatabase db = SnippetDatabase.getDatabase(application);
        snippetDao = db.snippetDao();
        tagDao = db.tagDao();
        snippetTagsDao = db.snippetTagsDao();
        allTags = tagDao.getAllTags();
        allSnippets = snippetDao.getAllSnippets();
        allSnippetTags = snippetTagsDao.getAllSnippetTags();
        allFavorites = snippetDao.getAllFavorites();
    }

    /**
     * Inserts a snippet into the db.
     * <p>
     * The operation is called asynchronously from the UI thread.
     *
     * @param snippet snippet to insert into the db
     * @return id of the inserted snippet
     */
    public long insert(Snippet snippet) {
        new insertSnippetTask(snippetDao, tagDao, snippetTagsDao).execute(snippet);
        return snippet.getId();
    }

    /**
     * Inserts a tag asynchronously into the db.
     *
     * @param tags tag to insert
     */
    public void insert(Tag... tags) {
        new insertTagsTask(tagDao).execute(tags);
    }


    // ----------------------------------------------------------------------------
    // the following classes make sure data is inserted outside of UI thread
    // ----------------------------------------------------------------------------

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
            Log.d(TAG, "saved snippet with id " + snippetId);
            snippet.setId(snippetId);
            new insertTagsTask(tagDao, snippetTagsDao, snippet).execute(snippet.getTags()
                    .toArray(new Tag[snippet.getTags().size()]));
        }
    }

    private static class insertTagsTask extends AsyncTask<Tag, Void, List<Long>> {

        private TagDao tagsDao;
        private Snippet snippet;
        private SnippetTagsDao snippetTagsDao;

        insertTagsTask(TagDao dao) {
            tagsDao = dao;
        }

        insertTagsTask(TagDao dao, SnippetTagsDao snippetTagsDao, Snippet snippet) {
            tagsDao = dao;
            this.snippet = snippet;
            this.snippetTagsDao = snippetTagsDao;
        }

        @Override
        protected List<Long> doInBackground(Tag... tags) {
            return tagsDao.insert(tags);
        }

        @Override
        protected void onPostExecute(List<Long> tagIds) {
            List<SnippetTags> snippetTags = new LinkedList<>();
            snippet.getTags().forEach(id -> {
                snippetTags.add(new SnippetTags(snippet.getId(), new Tag(id.getTag())));
                Log.d(TAG, "saved tag with id " + id.getTag());
            });
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
    // repository content holders
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

    public LiveData<List<Snippet>> getSnippetsByName(String name) {
        return snippetDao.getSnippetsByName(name);
    }

    public List<Snippet> getAllFavorites() {
        return allFavorites;
    }
}
