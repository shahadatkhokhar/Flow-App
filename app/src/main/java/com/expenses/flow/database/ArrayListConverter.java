package com.expenses.flow.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import com.expenses.flow.ItemList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ArrayListConverter {

    @TypeConverter
    public static String MyListItemListToString(ArrayList<ItemList> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public static ArrayList<ItemList> stringToMyListItemList(@Nullable String data) {
        if (data == null) {
            return new ArrayList<>();
        }

        Type listType = new TypeToken<ArrayList<ItemList>>() {}.getType();

        Gson gson = new Gson();
        Log.d("Data",data);
        return gson.fromJson(data, listType);
    }


}
