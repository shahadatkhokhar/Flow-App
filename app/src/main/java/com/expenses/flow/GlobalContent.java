package com.expenses.flow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.room.Room;

import com.expenses.flow.database.UserDetails;
import com.expenses.flow.database.UserDetailsDB;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class GlobalContent {
    private static int DEBIT = 0;
    private static int CREDIT = 1;
    private static int ALL = 2;
    static int totalDebitAmount=0;
    static int totalCreditAmount=0;

    public static int dropdownValue = DEBIT;

    public static boolean isUserRegistered = false;
    public static boolean isSignUpcomplete = false;

    private static String userName;
    private static String userEmail;
    private static Bitmap profileImage;

    private static ArrayList<ItemList> creditList = new ArrayList<>();
    private static ArrayList<ItemList> debitList = new ArrayList<>();

    static final UserDetailsDB[] db = new UserDetailsDB[1];


    public static String getUserName(){
        return userName;
    }

    public static String getUserEmail(){
        return userEmail;
    }

    public static void setUserName(String name){
        userName = name;
    }

    public static void setUserEmail(String email){
        userEmail = email;
    }

    public static void setDebit()
    {
        dropdownValue = DEBIT;
    }

    public static void setCredit()
    {
        dropdownValue = CREDIT;
    }

    public static void setAll()
    {
        dropdownValue = ALL;
    }

    public static int getDropdownValue(){
        return dropdownValue;
    }

    public static void setTotalDebit(int amount){
        totalDebitAmount = amount;
    }

    public static void setTotalCredit(int amount){
        totalCreditAmount = amount;
    }

    public static int getTotalDebitAmount(){
        return totalDebitAmount;
    }

    public static int getTotalCreditAmount(){
        return totalCreditAmount;
    }

    public static int getSavings(){
        return totalCreditAmount-totalDebitAmount;
    }

    public static Bitmap getProfileImage(){
        return profileImage;
    }

    public static void setProfileImage(Bitmap image){
        profileImage = image;
    }

    public static void setCreditList(ArrayList<ItemList> list){
        if(creditList!=null && list != null) {
            creditList.clear();
            creditList.addAll(list);
        }
    }

    public static void setDebitList(ArrayList<ItemList> list){
        if(debitList!=null && list != null){
            debitList.clear();
            debitList.addAll(list);
        }
    }

    public static ArrayList<ItemList> getCreditList() {
        return creditList;
    }

    public static ArrayList<ItemList> getDebitList() {
        return debitList;
    }


}
