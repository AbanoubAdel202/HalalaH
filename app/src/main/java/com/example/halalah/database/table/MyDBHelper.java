package com.example.halalah.database.table;

import com.example.halalah.database.DBHelper;

import android.content.Context;

public class MyDBHelper extends DBHelper {
	public static final String DBNAME = "oversea.db";
	public static final int DBVERSION = 1;

	private static final Class<?>[] clazz = { Aid.class, Capk.class};

	public MyDBHelper(Context context) {
		super(context, DBNAME, null, DBVERSION, clazz);
	}

}
