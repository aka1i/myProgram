package com.example.wechat.Dao;

public class ScheduleDbSchema {
    public static final class ScheduleTable{
        public static final String NAME = "schedules";
        public static final class Cols{
            public static final String UUID ="uuid";
            public static final String TITLE = "title";
            public static final String DETAIL = "detail";
            public static final String DATE = "date";
            public static final String HAS_REMIND = "has_remind";
        }

    }
}
