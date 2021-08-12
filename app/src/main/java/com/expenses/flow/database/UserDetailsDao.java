package com.expenses.flow.database;

import android.graphics.Bitmap;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;


import com.expenses.flow.ItemList;

import java.util.ArrayList;
import java.util.List;


@Dao
public interface UserDetailsDao {
//    @Query("SELECT CreditList FROM userdetails where Email=:email")
//    @TypeConverters(Converter.class)
//    ArrayList<ItemList> getCreditListFromDB(String email);
//
//    @Query("SELECT DebitList FROM userdetails where Email=:email")
//    @TypeConverters(Converter.class)
//    ArrayList<ItemList> getDebitListFromDB(String email);

    @Query("SELECT *  FROM userdetails where Email=:email")
    UserDetails getUserDetails(String email);

    @Query("UPDATE userdetails set credit_list=:creditList where Email=:email")
    void updateCreditListInDB(String email,ArrayList<ItemList> creditList);

    @Query("UPDATE userdetails set debit_list=:debitList where Email=:email")
    void updateDebitListInDB(String email,ArrayList<ItemList> debitList);

    @Query("UPDATE userdetails set total_debit_amount=:totalDebitAmount where Email=:email")
    void updateTotalDebit(String email,int totalDebitAmount);

    @Query("UPDATE userdetails set total_credit_amount=:totalCreditAmount where Email=:email")
    void updateTotalCredit(String email,int totalCreditAmount);

    @Query("SELECT profile_image  FROM userdetails where Email=:email")
    Bitmap getProfileImage(String email);

    @Query("SELECT profile_image_url  FROM userdetails where Email=:email")
    String getProfileImageUrl(String email);


    @Insert
    void insert(UserDetails... users);

    @Delete
    void delete(UserDetails user);


}
