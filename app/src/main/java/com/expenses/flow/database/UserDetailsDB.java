package com.expenses.flow.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {UserDetails.class}, version = 1)
@TypeConverters({ArrayListConverter.class, BitmapConverter.class})
public abstract class UserDetailsDB extends RoomDatabase {
    public abstract UserDetailsDao UserDetailsDao();
}

