package com.example.halalah.sqlite.storage;

import android.content.Context;

import com.example.halalah.sqlite.model.RequestModel;
import com.example.halalah.sqlite.repository.impl.Repository;

public class RequestRepository extends Repository<RequestModel> {

    public RequestRepository(Context context) {
        super(context);
    }

    public RequestModel getById(String username) {
        RequestModel wrapper = new RequestModel();
        wrapper.setRequestId(username);

        return queryById(wrapper);
    }
}
