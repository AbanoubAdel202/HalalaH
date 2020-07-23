package com.example.halalah.sqlite.repository.component;

import androidx.room.TypeConverter;

import com.example.halalah.TMS.Connection;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class Converters {
    @TypeConverter
    public static String[] fromString(String value) {
        Type listType = new TypeToken<String[]>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(String[] stringArray) {
        Gson gson = new Gson();
        String json = gson.toJson(stringArray);
        return json;
    }

    @TypeConverter
    public static Connection ConnectionFromString(String value) {
        Type connectionType = new TypeToken<Connection>() {}.getType();
        return new Gson().fromJson(value, connectionType);
    }

    @TypeConverter
    public static String stringFromConnection(Connection connection) {
        Gson gson = new Gson();
        String json = gson.toJson(connection);
        return json;
    }
}
