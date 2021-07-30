package com.expenses.flow;

public class GlobalContent {
    private static int DEBIT = 0;
    private static int CREDIT = 1;
    private static int ALL = 2;
    static int totalDebitAmount=0;
    static int totalCreditAmount=0;

    public static int dropdownValue = DEBIT;

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


}
