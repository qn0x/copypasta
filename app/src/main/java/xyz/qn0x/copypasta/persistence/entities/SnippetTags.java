package xyz.qn0x.copypasta.persistence.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "snippetTags", foreignKeys = {
        @ForeignKey(entity = Snippet.class, parentColumns = "id", childColumns = "snippet_id",
                onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
        @ForeignKey(entity = Tag.class, parentColumns = "id", childColumns = "tag_id",
                onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
}, primaryKeys = {"snippet_id", "tag_id"})
public class SnippetTags {

    @ColumnInfo(name = "snippet_id")
    private long snippet_id;

    @ColumnInfo(name = "tag_id")
    private long tag_id;

    public SnippetTags(long snippet_id, long tag_id) {
        this.snippet_id = snippet_id;
        this.tag_id = tag_id;
    }

    public long getSnippet_id() {
        return snippet_id;
    }

    public long getTag_id() {
        return tag_id;
    }

    public void setSnippet_id(long snippet_id) {
        this.snippet_id = snippet_id;
    }

    public void setTag_id(long tag_id) {
        this.tag_id = tag_id;
    }
}
