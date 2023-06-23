package com.medi.dailynews.db;

import androidx.room.TypeConverter;

import com.medi.dailynews.models.Source;

public class Converters {
    @TypeConverter
    public static Source fromString(String name) {
        return new Source(name);
    }

    @TypeConverter
    public static String toString(Source source) {
        return source.getName();
    }
}