package xyz.qn0x.copypasta.persistence.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "tags",
        indices = {@Index(value = {"tag"}, unique = true)})
public class Tag {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "tag")

    @NonNull
    private String tag;

    public Tag(@NonNull String tag) {
        this.tag = tag;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getTag() {
        return tag;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTag(@NonNull String tag) {
        this.tag = tag;
    }
}
