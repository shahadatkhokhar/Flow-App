package com.expenses.flow;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class GlobalContent {
    private static int DEBIT = 0;
    private static int CREDIT = 1;
    private static int ALL = 2;
    static long totalDebitAmount=0;
    static long totalCreditAmount=0;

    public static int dropdownValue = DEBIT;

    public static boolean isNewUser = false;

    private static String userName;
    private static String userEmail;
    private static Bitmap profileImage;
    private static String profileImageUrl;

    private static ArrayList<ItemList> creditList = new ArrayList<>();
    private static ArrayList<ItemList> debitList = new ArrayList<>();

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

    public static void setTotalDebit(Long amount){
        totalDebitAmount = amount;
    }

    public static void setTotalCredit(long amount){
        totalCreditAmount = amount;
    }

    public static long getTotalDebitAmount(){
        return totalDebitAmount;
    }

    public static long getTotalCreditAmount(){
        return totalCreditAmount;
    }

    public static long getSavings(){
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

    public static String getProfileImageURL(){
        return profileImageUrl;
    }
    public static void setProfileImageUrl(String url){
        profileImageUrl = url;
    }


}
