package xyz.qn0x.copypasta.persistence.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import xyz.qn0x.copypasta.persistence.entities.Snippet;

@Dao
public interface SnippetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Snippet snippet);

    @Query("SELECT * FROM snippets ORDER BY name ASC")
    LiveData<List<Snippet>> getAllSnippets();

    @Query("SELECT * FROM snippets s WHERE s.id IN " +
            "(SELECT snippet_id FROM snippetTags WHERE tag = :tagId)")
    LiveData<List<Snippet>> getSnippetsByTagId(String tagId);

    @Query("SELECT * FROM snippets s WHERE s.id = :snippetId")
    LiveData<List<Snippet>> getSnippetById(long snippetId);

    @Query("SELECT * FROM snippets WHERE name LIKE :name")
    List<Snippet> getSnippetsByName(String name);

    @Query("SELECT * FROM snippets WHERE favorite = 1")
    List<Snippet> getAllFavorites();

    @Delete
    void deleteSnippet(Snippet snippet);

    @Query("UPDATE snippets SET favorite = :favorite WHERE id = :snippetId")
    long updateFavoriteStatus(long snippetId, boolean favorite);
}
