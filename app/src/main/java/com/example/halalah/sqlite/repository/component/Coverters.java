package com.example.halalah.sqlite.repository.component;

import androidx.room.TypeConverter;

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
}
