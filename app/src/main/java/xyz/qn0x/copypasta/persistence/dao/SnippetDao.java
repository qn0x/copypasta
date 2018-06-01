package xyz.qn0x.copypasta.persistence.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import xyz.qn0x.copypasta.persistence.entities.Snippet;

@Dao
public interface SnippetDao {

    /**
     * inserts a snippet into the db
     *
     * @param snippet snippet to insert
     * @return new snippet id
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Snippet snippet);

    /**
     * fetches all snippets form the db
     *
     * @return list of snippets wrapped as live data
     */
    @Query("SELECT * FROM snippets ORDER BY name ASC")
    LiveData<List<Snippet>> getAllSnippets();

    /**
     * fetches all snippets that are associated with a tag
     *
     * @param tag tag to search for
     * @return list of snippet ids
     */
    @Query("SELECT DISTINCT st.snippet_id FROM snippetTags st WHERE st.tag like :tag")
    List<Long> getSnippetsByTag(String tag);

    /**
     * searches the db for snippets with a given name
     *
     * @param name name to search for
     * @return list of ids
     */
    @Query("SELECT s.id FROM snippets s WHERE name LIKE :name")
    List<Long> getSnippetsByName(String name);

    /**
     * fetches a snippet for a given id from the db
     *
     * @param snippetId snippet id
     * @return snippet object
     */
    @Query("SELECT * FROM snippets WHERE id = :snippetId")
    Snippet getSnippetsForId(long snippetId);

    /**
     * fetches all current favorites from the db
     *
     * @return list of snippets marked as favorite
     */
    @Query("SELECT * FROM snippets WHERE favorite = 1")
    List<Snippet> getAllFavorites();

    /**
     * deletes a given snippet
     *
     * @param snippet snippet to delete
     */
    @Delete
    void deleteSnippet(Snippet snippet);

    /**
     * update favorite status
     *
     * @param snippetId id to set
     * @param favorite  favorite status
     * @return id that was set
     */
    @Query("UPDATE snippets SET favorite = :favorite WHERE id = :snippetId")
    long updateFavoriteStatus(long snippetId, boolean favorite);

    /**
     * search for the query in all of the snippet texts
     *
     * @param query query to search for
     * @return any found snippet ids
     */
    @Query("SELECT s.id FROM snippets s WHERE s.text LIKE :query")
    List<Long> getSnippetsForText(String query);

    /**
     * update a snippet's name
     *
     * @param snippetId candidate snippet
     * @param newName   new name
     */
    @Query("UPDATE snippets SET name = :newName WHERE id = :snippetId")
    void updateSnippetName(long snippetId, String newName);

    /**
     * update a snippet's text
     *
     * @param snippetId candidate snippet
     * @param newText   new text
     */
    @Query("UPDATE snippets SET text = :newText WHERE id = :snippetId")
    void updateSnippetText(long snippetId, String newText);
}


