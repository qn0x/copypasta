package xyz.qn0x.copypasta.persistence.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import xyz.qn0x.copypasta.persistence.entities.SnippetTags;

/**
 * @author Janine Kostka
 */
@Dao
public interface SnippetTagsDao {

    /**
     * fetches all current snippet/tag associations wrapped as live data
     *
     * @return all current snippet/tag associations
     */
    @Query("SELECT * FROM snippetTags ORDER BY snippet_id ASC")
    LiveData<List<SnippetTags>> getAllSnippetTags();

    /**
     * inserts an array of snippet/tag associations
     *
     * @param snippetTags array to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SnippetTags... snippetTags);

    @Query("DELETE FROM snippetTags WHERE snippet_id = :snippetId")
    void deleteSnippetTagsForSnippetId(long snippetId);


    @Query("DELETE FROM tags WHERE tag NOT IN (SELECT DISTINCT tag FROM snippetTags)")
    void deleteStrayTags();
}
