package com.expenses.flow;

public class ItemList{
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
}
