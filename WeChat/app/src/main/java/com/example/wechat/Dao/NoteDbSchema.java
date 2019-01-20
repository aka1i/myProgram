package com.example.wechat.Dao;

public class NoteDbSchema {
    public static final class NoteTable{
        public static final String NAME = "notes";
        public static final class Cols{
            public static final String UUID ="uuid";
            public static final String TITLE = "title";
            public static final String DETAIL = "detail";
            public static final String DATE = "date";
            public static final String DETAILHEMLSTRING = "detailhtmlstring";
            public static final String ISDELETED = "isdeleted";
        }

    }
    public static final class DeletedNoteTable{
        public static final String NAME = "deleted_notes";
        public static final class Cols{
            public static final String UUID ="uuid";
            public static final String TITLE = "title";
            public static final String DETAIL = "detail";
            public static final String DATE = "date";
            public static final String DETAILHEMLSTRING = "detailhtmlstring";
            public static final String ISDELETED = "isdeleted";
        }

    }
}
