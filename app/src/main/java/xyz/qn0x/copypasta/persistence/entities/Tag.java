package xyz.qn0x.copypasta.persistence.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Represents a tag entity.
 * Tags are identified by their name.
 *
 * @author Janine Kostka
 */
@Entity(tableName = "tags",
        indices = {@Index(value = {"tag"}, unique = true)})
public class Tag {

    @ColumnInfo(name = "tag")
    @PrimaryKey
    @NonNull
    private String tag;

    public Tag(@NonNull String tag) {
        this.tag = tag;
    }

    @NonNull
    public String getTag() {
        return tag;
    }

    public void setTag(@NonNull String tag) {
        this.tag = tag;
    }
}
