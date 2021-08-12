package com.expenses.flow.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;

public class BitmapConverter{
    @TypeConverter
    public static byte[] encodeToByteArray(Bitmap image) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        if(image!=null){
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS);
        }
        return byteArrayOS.toByteArray();
    }

    @TypeConverter
    public static Bitmap decodeByteArray(byte[] input) {
//        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(input, 0, input.length);
    }
}
