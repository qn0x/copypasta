package xyz.qn0x.copypasta.persistence.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.util.Log;

import java.util.List;

import xyz.qn0x.copypasta.persistence.entities.SnippetTags;
import xyz.qn0x.copypasta.persistence.entities.Tag;


@Dao
public interface SnippetTagsDao {

    @Query("SELECT * FROM snippetTags ORDER BY snippet_id ASC")
    LiveData<List<SnippetTags>> getAllSnippetTags();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SnippetTags... snippetTags);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(SnippetTags... snippetTags);

    @Query("DELETE FROM snippetTags WHERE snippet_id = :snippetId")
    void deleteSnippetTagsForSnippetId(long snippetId);

    @Query("DELETE FROM snippetTags WHERE tag = :tag")
    void deleteForTag(Tag tag);

    @Query("DELETE FROM tags WHERE tag NOT IN (SELECT DISTINCT tag FROM snippetTags)")
    void deleteStrayTags();
}
