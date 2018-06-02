package xyz.qn0x.copypasta.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import xyz.qn0x.copypasta.persistence.dao.SnippetDao;
import xyz.qn0x.copypasta.persistence.dao.SnippetTagsDao;
import xyz.qn0x.copypasta.persistence.dao.TagDao;
import xyz.qn0x.copypasta.persistence.entities.Snippet;
import xyz.qn0x.copypasta.persistence.entities.SnippetTags;
import xyz.qn0x.copypasta.persistence.entities.Tag;
import xyz.qn0x.copypasta.ui.activities.MainActivity;

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
    private List<Tag> allTags;

    public SnippetRepository(Application application) {
        SnippetDatabase db = SnippetDatabase.getDatabase(application);
        snippetDao = db.snippetDao();
        tagDao = db.tagDao();
        snippetTagsDao = db.snippetTagsDao();
        allTags = tagDao.getAllTags();
        allSnippets = snippetDao.getAllSnippets();
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
        tagDao.insert(tags);
    }


    // ----------------------------------------------------------------------------
    // async tasks
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
            if (snippet.getTags().size() >= 1) {
                Log.d(TAG, "snippet contains tags");
                new insertTagsTask(tagDao, snippetTagsDao, snippet).execute(snippet.getTags()
                        .toArray(new Tag[snippet.getTags().size()]));
            }
        }
    }

    private static class insertTagsTask extends AsyncTask<Tag, Void, List<Long>> {

        private TagDao tagsDao;
        private Snippet snippet;
        private SnippetTagsDao snippetTagsDao;

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

        @Override
        protected void onPostExecute(Void aVoid) {
            MainActivity.instance.updateAdapter();
        }
    }


    // -----------------------------------------------------------------------------------------
    // repository content holders
    // -----------------------------------------------------------------------------------------

    public LiveData<List<Snippet>> getAllSnippets() {
        return allSnippets;
    }

    public List<Tag> getAllTags() {
        return allTags;
    }


    // -------- queries ---------------
    public List<Long> getSnippetsByName(String name) {
        return snippetDao.getSnippetsByName(name);
    }

    public List<Tag> getTagsForSnippetId(long snippetId) {
        return tagDao.getTagsForSnippetId(snippetId);
    }

    public Snippet getSnippetForId(long snippetId) {
        return snippetDao.getSnippetsForId(snippetId);
    }

    public List<Long> getSnippetsByTag(String tag) {
        return snippetDao.getSnippetsByTag(tag);
    }

    public List<Long> getSnippetsForText(String query) {
        return snippetDao.getSnippetsForText(query);
    }

    // -------- update ----------------
    public long updateFavoriteStatus(long snippetId, boolean favorite) {
        Log.d(TAG, "updated favorite to " + favorite + ", id: " + snippetId);
        return snippetDao.updateFavoriteStatus(snippetId, favorite);
    }

    public void updateSnippetName(long snippetId, String newName) {
        snippetDao.updateSnippetName(snippetId, newName);
    }

    public void updateSnippetText(long snippetId, String newText) {
        snippetDao.updateSnippetText(snippetId, newText);
    }

    // -------- delete ----------------
    public void deleteSnippet(Snippet snippet) {
        snippetDao.deleteSnippet(snippet);
    }

    public void deleteSnippetTagsForSnippetId(long snippetId) {
        snippetTagsDao.deleteSnippetTagsForSnippetId(snippetId);
    }


    public void deleteStrayTags() {
        snippetTagsDao.deleteStrayTags();
    }


    // -------- insert ----------------

    public void insert(SnippetTags... snippetTags) {
        snippetTagsDao.insert(snippetTags);
    }

}
