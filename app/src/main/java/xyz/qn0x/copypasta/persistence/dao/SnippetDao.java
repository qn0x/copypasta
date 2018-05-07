package xyz.qn0x.copypasta.persistence.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import xyz.qn0x.copypasta.persistence.entities.Snippet;

@Dao
public interface SnippetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Snippet snippet);

    @Query("SELECT * FROM snippets ORDER BY name ASC")
    LiveData<List<Snippet>> getAllSnippets();

    @Query("SELECT * FROM snippets WHERE snippets.id IN " +
            "(SELECT snippet_id FROM snippetTags WHERE tag_id = :tagId)")
    LiveData<List<Snippet>> getSnippetsByTag(String tagId);
}
