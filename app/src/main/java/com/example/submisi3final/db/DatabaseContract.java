package com.example.submisi3final.db;

import android.provider.BaseColumns;

public class DatabaseContract {
    public static final class NoteColumns implements BaseColumns{
        public static final String TABLE_NAME = "content";

        public static final String TITLE = "title";
        public static final String DATE = "date";
        public static final String DESC = "desc";
        public static final String RATE = "rate";
        public static final String POSTER = "poster";

    }
}
