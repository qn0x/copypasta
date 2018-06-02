package xyz.qn0x.copypasta.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import xyz.qn0x.copypasta.persistence.entities.Tag;

@Dao
public interface TagDao {

    @Query("SELECT tag FROM tags ORDER BY tag ASC")
    List<Tag> getAllTags();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insert(Tag... tagsList);

    @Query("SELECT t.tag FROM tags t JOIN snippetTags st ON t.tag = st.tag " +
            "WHERE st.snippet_id = :snippetID ORDER BY t.tag ASC")
    List<Tag> getTagsForSnippetId(long snippetID);
}
