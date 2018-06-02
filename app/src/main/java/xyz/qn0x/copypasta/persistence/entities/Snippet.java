package xyz.qn0x.copypasta.persistence.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a snippet.
 * <p>
 * Snippets contain a name, a text, their favorite status and a list of tags
 */
@Entity(tableName = "snippets",
        indices = {@Index(value = {"name", "text"}, unique = true)})
public class Snippet {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "text")
    private String text;

    @ColumnInfo(name = "favorite")
    private boolean favorite;

    @Ignore
    private List<Tag> tags;

    public Snippet(@NonNull String name, String text) {
        this.name = name;
        this.text = text;
        this.favorite = false;
        this.tags = new LinkedList<>();
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

}
