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

    @Query("DELETE FROM snippets")
    void deleteAll();

    @Query("SELECT * FROM snippets ORDER BY name ASC")
    LiveData<List<Snippet>> getAllSnippets();
}
