package xyz.qn0x.copypasta.persistence;

import android.arch.persistence.room.TypeConverter;

import xyz.qn0x.copypasta.persistence.entities.Tag;

/**
 * Converter for the Tag entity. The only attribute of the entity is stored as string in the db.
 */
public class TagConverter {
    @TypeConverter
    public static Tag toTag(String value) {
        return value == null ? null : new Tag(value);
    }

    @TypeConverter
    public static String fromTag(Tag value) {
        return value == null ? null : value.getTag();
    }
}
