package com.expenses.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemList {
    private String itemName;
    private Integer itemAmount;

    public ItemList(){}
    public ItemList(String Name,Integer Amount){
        itemName = Name;
        itemAmount = Amount;
    }
    public String getItemName(){
        return itemName;
    }

    public Integer getItemAmount(){
        return itemAmount;
    }

    public void setItemName(String itemName){
        this.itemName = itemName;
    }
    public void setItemAmount(Integer itemAmount){
        this.itemAmount = itemAmount;
    }
//    private static List<String> creditList = new ArrayList<>();
//    private static List<String> debitList = new ArrayList<>();
//    private static List<String> allList = new ArrayList<>();
//
//    private static List<Integer> creditAmountList = new ArrayList<>();
//    private static List<Integer> debitAmountList = new ArrayList<>();
//
//    public int totalCreditAmount;
//    public int totalDebitAmount;
//
//    public static void addCreditItem(String itemName, Integer amount){
//        creditList.add(itemName);
//        creditAmountList.add(creditList.indexOf(itemName),amount);
//    }
//    public void addDebitItem(String itemName, Integer amount){
//        debitList.add(itemName);
//        debitAmountList.add(creditList.indexOf(itemName),amount);
//    }
//    public void deleteCreditItem(String itemName){
//
//    }
//    public void deleteDebitItem(String itemName){
//
//    }
//    public static List<String> getCreditList()
//    {
//        return creditList;
//    }
//    public static List<String> getCreditAmountList()
//    {
//        return creditList;
//    }
}
