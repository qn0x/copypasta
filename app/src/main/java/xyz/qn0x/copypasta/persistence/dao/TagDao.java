package xyz.qn0x.copypasta.persistence.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import xyz.qn0x.copypasta.persistence.entities.Tag;

@Dao
public interface TagDao {

    @Query("SELECT * FROM tags ORDER BY tag ASC")
    LiveData<List<Tag>> getAllTags();

    @Query("SELECT * FROM tags WHERE tag = :tagName")
    LiveData<List<Tag>> getTagByTagName(String tagName);

    @Query("SELECT * FROM tags WHERE id = :tagId")
    LiveData<Tag> getTagById(int tagId);

    @Query("SELECT * FROM tags WHERE tags.id IN " +
            "(SELECT tag_id FROM snippetTags WHERE snippet_id = :snippetId)")
    LiveData<List<Tag>> getTagsForSnippetId(int snippetId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(Tag... tagsList);
}
