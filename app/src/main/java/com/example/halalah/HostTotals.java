package com.example.halalah;

public class HostTotals {

    public CardSchemeTotals[] cardSchemeTotals;
    public boolean inBalance;

     HostTotals(int numberofcardscheme)
    {
        cardSchemeTotals=new CardSchemeTotals[numberofcardscheme];
    }
}
