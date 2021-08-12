package com.expenses.flow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemList implements Serializable {
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
