package com.example.halalah.database;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TableHelper {
	private static final String TAG = "AHibernate";

	public static <T> void createTablesByClasses(SQLiteDatabase db, Class<?>[] clazzs) {
		for (Class<?> clazz : clazzs)
			createTable(db, clazz);
	}

	public static <T> void dropTablesByClasses(SQLiteDatabase db, Class<?>[] clazzs) {
		for (Class<?> clazz : clazzs)
			dropTable(db, clazz);
	}

	public static <T> void upGradeTablesByClasses(SQLiteDatabase db, Class<?>[] clazzs) {
		for (Class<?> clazz : clazzs)
			onUpGradeTable(db, clazz);
	}

	public static <T> void createTable(SQLiteDatabase db, Class<T> clazz) {
		String tableName = "";
		if (clazz.isAnnotationPresent(Table.class)) {
			Table table = (Table) clazz.getAnnotation(Table.class);
			tableName = table.name();
		}

		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ").append(tableName).append(" (");

		List<Field> allFields = TableHelper.joinFields(clazz.getDeclaredFields(),
				clazz.getSuperclass().getDeclaredFields());
		for (Field field : allFields) {
			if (!field.isAnnotationPresent(Column.class)) {
				continue;
			}
			Column column = (Column) field.getAnnotation(Column.class);
			String columnType = getColumnType(field.getType());
			sb.append(column.name() + " " + columnType);
			if (field.isAnnotationPresent(Id.class)) {
				sb.append(" primary key autoincrement");
			}
			if (column.unique()) {
				sb.append(" unique");
			}
			sb.append(", ");
		}

		sb.delete(sb.length() - 2, sb.length() - 1);
		sb.append(")");
		String sql = sb.toString();
		Log.d(TAG, "crate table [" + tableName + "]: " + sql);
		db.execSQL(sql);
	}

	public static <T> void dropTable(SQLiteDatabase db, Class<T> clazz) {
		String tableName = "";
		if (clazz.isAnnotationPresent(Table.class)) {
			Table table = (Table) clazz.getAnnotation(Table.class);
			tableName = table.name();
		}
		String sql = "DROP TABLE IF EXISTS " + tableName;
		Log.d(TAG, "dropTable[" + tableName + "]:" + sql);
		db.execSQL(sql);
	}

	public static <T> void onUpGradeTable(SQLiteDatabase db, Class<T> clazz) {
		String tableName = "";
		if (clazz.isAnnotationPresent(Table.class)) {
			Table table = (Table) clazz.getAnnotation(Table.class);
			tableName = table.name();
		}
		if (!tabIsExist(db, tableName)) {
			System.out.println(tableName);
			createTable(db, clazz);
		}
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		List<Column> columns = getFieldNames(clazz);
		for (Column column : columns) {
			if (checkColumnExist(db, tableName, column.name())) {
				sb1.append(column.name()).append(",");
				sb2.append(column.name()).append(",");
			} else {
				sb1.append(column.name()).append(",");
				sb2.append("null,");
			}
		}
		System.out.println(tableName + "   sb1===" + sb1.toString());
		System.out.println(tableName + "   sb2===" + sb2.toString());
		db.execSQL("ALTER TABLE " + tableName + " RENAME TO " + tableName + "_temp");
		createTable(db, clazz);
		db.execSQL("insert into " + tableName + "(" + sb1.substring(0, sb1.length() - 1) + ") " + "select "
				+ sb2.substring(0, sb2.length() - 1) + " from " + tableName + "_temp");
		db.execSQL("DROP TABLE " + tableName + "_temp");
	}

	private static String getColumnType(Class<?> fieldType) {
		if ((Byte.TYPE == fieldType)||(Integer.TYPE == fieldType)||(Long.TYPE == fieldType)||(Short.TYPE == fieldType)||(Date.class == fieldType)) {
			return "INTEGER";
		}
		else if ((Float.TYPE == fieldType) || (Double.TYPE == fieldType)) {
			return "REAL";
		}
		else if (Boolean.class == fieldType) {
			return "INTEGER";
		}
		else if (byte[].class == fieldType) {
			return "TEXT";
		}
		else
			return "TEXT";
	}

	// Merge field array and remove duplicate, filter out non column fields and put ID in the first field
	public static List<Field> joinFields(Field[] fields1, Field[] fields2) {
		Map<String, Field> map = new LinkedHashMap<String, Field>();
		for (Field field : fields1) {
			// Filter out non column defined fields
			if (!field.isAnnotationPresent(Column.class)) {
				continue;
			}
			Column column = (Column) field.getAnnotation(Column.class);
			map.put(column.name(), field);
		}
		for (Field field : fields2) {
			// Filter out non column defined fields
			if (!field.isAnnotationPresent(Column.class)) {
				continue;
			}
			Column column = (Column) field.getAnnotation(Column.class);
			if (!map.containsKey(column.name())) {
				map.put(column.name(), field);
			}
		}
		List<Field> list = new ArrayList<Field>();
		for (String key : map.keySet()) {
			Field tempField = map.get(key);
			// If it is ID, it will be placed first.
			if (tempField.isAnnotationPresent(Id.class)) {
				list.add(0, tempField);
			} else {
				list.add(tempField);
			}
		}
		return list;
	}

	public static <T> List<Column> getFieldNames(Class<T> clazz) {
		List<Column> columns = new ArrayList<Column>();
		List<Field> allFields = TableHelper.joinFields(clazz.getDeclaredFields(),
				clazz.getSuperclass().getDeclaredFields());
		for (Field field : allFields) {
			if (!field.isAnnotationPresent(Column.class)) {
				continue;
			}
			Column column = (Column) field.getAnnotation(Column.class);
			columns.add(column);
		}
		return columns;
	}

	/**
	 * Check whether a column of a table exists
	 * 
	 * @param db
	 * @param tableName
	 *
	 * @param columnName
	 *
	 * @return
	 */
	public static boolean checkColumnExist(SQLiteDatabase db, String tableName, String columnName) {
		boolean result = false;
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("SELECT * FROM " + tableName, null);
			result = cursor != null && cursor.getColumnIndex(columnName) != -1;
		} catch (Exception e) {
			Log.e(TAG, "checkColumnExists1..." + e.getMessage());
		} finally {
			if (null != cursor && !cursor.isClosed()) {
				cursor.close();
			}
		}

		return result;
	}

	public static boolean tabIsExist(SQLiteDatabase db, String tabName) {
		boolean result = false;
		if (tabName == null) {
			return false;
		}
		Cursor cursor = null;
		try {
			String sql = "select count(*) as c from sqlite_master" + " where type ='table' and " + "name ='"
					+ tabName.trim() + "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;

	}

}