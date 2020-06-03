package com.example.halalah.ui.merchant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.halalah.R;

public class MerchantFragment extends Fragment {

    private MerchantViewModel merchantViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        merchantViewModel =
                ViewModelProviders.of(this).get(MerchantViewModel.class);
        View root = inflater.inflate(R.layout.fragment_merchant, container, false);

        return root;
    }
}
