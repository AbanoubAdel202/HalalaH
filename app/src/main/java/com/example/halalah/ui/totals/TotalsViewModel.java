package com.example.halalah.ui.totals;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TotalsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TotalsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is TOTALS fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}