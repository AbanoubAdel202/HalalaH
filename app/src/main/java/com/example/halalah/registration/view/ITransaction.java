package com.example.halalah.registration.view;

import androidx.annotation.StringRes;

import com.example.halalah.registration.RegistrationData;

public interface ITransaction {
    interface View {
        void showRegistrationScreen();

        void showError(@StringRes int errorMessageId);

        void showError(String errorMessageString);

        void showConnectionStatus(int connectionStatus);

        void showRegistrationSuccess();
    }

    interface Handler {
        void updateRegistrationData(RegistrationData registrationData, ITransaction.View transactionView);
    }
}
