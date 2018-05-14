package xyz.qn0x.copypasta.persistence.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;


@Entity(tableName = "snippets",
        indices = {@Index(value = {"name", "text"}, unique = true)})
public class Snippet {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "name")
    private String name = "";

    @ColumnInfo(name = "text")
    private String text = "";

    @Ignore
    private List<Tag> tags;

    public Snippet(@NonNull String name, String text) {
        this.name = name;
        this.text = text;
        this.tags = new LinkedList<>();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setText(String text) {
        this.text = text;
    }
}
