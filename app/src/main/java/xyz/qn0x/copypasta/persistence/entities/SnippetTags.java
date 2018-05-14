package xyz.qn0x.copypasta.persistence.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Represents the helper table to model the many-to-many relation of Tags and Snippets.
 *
 * @author Janine Kostka
 */
@Entity(tableName = "snippetTags", foreignKeys = {
        @ForeignKey(entity = Snippet.class, parentColumns = "id", childColumns = "snippet_id",
                onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
        @ForeignKey(entity = Tag.class, parentColumns = "tag", childColumns = "tag",
                onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
}, primaryKeys = {"snippet_id", "tag"})
public class SnippetTags {

    @ColumnInfo(name = "snippet_id")
    private long snippet_id;

    @ColumnInfo(name = "tag")
    @NonNull
    private String tag;

    public SnippetTags(long snippet_id, @NonNull String tag) {
        this.snippet_id = snippet_id;
        this.tag = tag;
    }

    public long getSnippet_id() {
        return snippet_id;
    }

    public String getTag() {
        return tag;
    }

    public void setSnippet_id(long snippet_id) {
        this.snippet_id = snippet_id;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
