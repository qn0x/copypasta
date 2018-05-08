package xyz.qn0x.copypasta.persistence.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import xyz.qn0x.copypasta.persistence.entities.SnippetTags;


@Dao
public interface SnippetTagsDao {

    @Query("SELECT * FROM snippetTags ORDER BY snippet_id ASC")
    LiveData<List<SnippetTags>> getAllSnippetTags();
}
