package xyz.qn0x.copypasta.persistence.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


@Entity(tableName = "snippets")
public class Snippet {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    private String name = "";

    @ColumnInfo(name = "text")
    private String text = "";

    @ColumnInfo(name = "tags")
    private String tags = "";

    public Snippet(@NonNull String name, String text, @Nullable String tags) {
        this.name = name;
        this.text = text;
        if (tags != null)
            this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getTags() {
        return tags;
    }

}
