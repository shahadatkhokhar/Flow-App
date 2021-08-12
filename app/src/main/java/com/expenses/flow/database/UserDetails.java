package com.expenses.flow.database;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;


import com.expenses.flow.ItemList;



import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@Entity
public class UserDetails {
    @PrimaryKey
    @ColumnInfo(name = "Email")
    @NotNull
    public String email;

    @ColumnInfo(name = "Name")
    public String name;

    @ColumnInfo(name = "credit_list")
    @TypeConverters(ArrayListConverter.class)
    public ArrayList<ItemList> CreditList;

    @ColumnInfo(name = "debit_list")
    @TypeConverters(ArrayListConverter.class)
    public ArrayList<ItemList> DebitList;

    @ColumnInfo(name = "total_debit_amount")
    public int totalDebitAmount;

    @ColumnInfo(name = "total_credit_amount")
    public int totalCreditAmount;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB, name="profile_image")
    @TypeConverters(BitmapConverter.class)
    public Bitmap profileImage;

    @ColumnInfo(name = "profile_image_url")
    public String profileImageUrl;


}

