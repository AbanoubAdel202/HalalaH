package com.example.halalah.database;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.topwise.cloudpos.struct.BytesUtil;

public class BaseDaoImpl<T> implements BaseDao<T> {
	private String TAG = "AHibernate";
	private SQLiteOpenHelper dbHelper;
	private String tableName;
	private String idColumn;
	private Class<T> clazz;
	private List<Field> allFields;
	private static final int METHOD_INSERT = 0;
	private static final int METHOD_UPDATE = 1;

	private static final int TYPE_NOT_INCREMENT = 0;
	private static final int TYPE_INCREMENT = 1;

	@SuppressWarnings("unchecked")
	public BaseDaoImpl(SQLiteOpenHelper dbHelper, Class<T> clazz) {
		this.dbHelper = dbHelper;
		if (clazz == null) {
			this.clazz = ((Class<T>) ((java.lang.reflect.ParameterizedType) super
					.getClass().getGenericSuperclass())
					.getActualTypeArguments()[0]);
		} else {
			this.clazz = clazz;
		}

		if (this.clazz.isAnnotationPresent(Table.class)) {
			Table table = (Table) this.clazz.getAnnotation(Table.class);
			this.tableName = table.name();
		}

		// 加载所有字段
		this.allFields = TableHelper.joinFields(this.clazz.getDeclaredFields(),
				this.clazz.getSuperclass().getDeclaredFields());

		// 找到主键
		for (Field field : this.allFields) {
			if (field.isAnnotationPresent(Id.class)) {
				Column column = (Column) field.getAnnotation(Column.class);
				this.idColumn = column.name();
				break;
			}
		}

		Log.d(TAG, "clazz:" + this.clazz + " tableName:" + this.tableName
				+ " idColumn:" + this.idColumn);
	}

	public BaseDaoImpl(SQLiteOpenHelper dbHelper) {
		this(dbHelper, null);
	}

	public SQLiteOpenHelper getDbHelper() {
		return dbHelper;
	}

	public T get(int id) {
		String selection = this.idColumn + " = ?";
		String[] selectionArgs = { Integer.toString(id) };
		Log.d(TAG, "[get]: select * from " + this.tableName + " where "
				+ this.idColumn + " = '" + id + "'");
		List<T> list = find(null, selection, selectionArgs, null, null, null,
				null);
		if ((list != null) && (list.size() > 0)) {
			return (T) list.get(0);
		}
		return null;
	}

	public List<T> rawQuery(String sql, String[] selectionArgs) {
		Log.d(TAG, "[rawQuery]: " + getLogSql(sql, selectionArgs));

		List<T> list = new ArrayList<T>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = this.dbHelper.getReadableDatabase();
			cursor = db.rawQuery(sql, selectionArgs);

			getListFromCursor(list, cursor);
		} catch (Exception e) {
			Log.e(this.TAG, "[rawQuery] from DB Exception.");
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}

		return list;
	}

	public boolean isExist(String sql, String[] selectionArgs) {
		Log.d(TAG, "[isExist]: " + getLogSql(sql, selectionArgs));

		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = this.dbHelper.getReadableDatabase();
			cursor = db.rawQuery(sql, selectionArgs);
			if (cursor.getCount() > 0) {
				return true;
			}
		} catch (Exception e) {
			Log.e(this.TAG, "[isExist] from DB Exception.");
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return false;
	}

	public List<T> find() {
		return find(null, null, null, null, null, null, null);
	}

	public List<T> find(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {
		Log.d(TAG, "[find]");

		List<T> list = new ArrayList<T>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = this.dbHelper.getReadableDatabase();
			cursor = db.query(this.tableName, columns, selection,
					selectionArgs, groupBy, having, orderBy, limit);

			getListFromCursor(list, cursor);
		} catch (Exception e) {
			Log.e(this.TAG, "[find] from DB Exception");
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}

		return list;
	}

	private void getListFromCursor(List<T> list, Cursor cursor)
			throws IllegalAccessException, InstantiationException {
		while (cursor.moveToNext()) {
			T entity = this.clazz.newInstance();

			for (Field field : this.allFields) {
				Column column = null;
				if (field.isAnnotationPresent(Column.class)) {
					column = (Column) field.getAnnotation(Column.class);

					field.setAccessible(true);
					Class<?> fieldType = field.getType();

					int c = cursor.getColumnIndex(column.name());
					if (c < 0) {
						continue; // if not exist continue
					} else if ((Integer.TYPE == fieldType)
							|| (Integer.class == fieldType)) {
						field.set(entity, cursor.getInt(c));
					} else if (String.class == fieldType) {
						field.set(entity, cursor.getString(c));
					} else if ((Long.TYPE == fieldType)
							|| (Long.class == fieldType)) {
						field.set(entity, Long.parseLong(cursor.getString(c)));
					} else if ((Float.TYPE == fieldType)
							|| (Float.class == fieldType)) {
						field.set(entity, Float.parseFloat(cursor.getString(c)));
					} else if ((Short.TYPE == fieldType)
							|| (Short.class == fieldType)) {
						field.set(entity, Short.parseShort(cursor.getString(c)));
					} else if ((Double.TYPE == fieldType)
							|| (Double.class == fieldType)) {
						field.set(entity, Double.parseDouble(cursor.getString(c)));
					} else if (Date.class == fieldType) {
						Date date = new Date();
						date.setTime(cursor.getLong(c));
						field.set(entity, date);
					}else if (Boolean.class == fieldType) {
						boolean bool = Boolean.parseBoolean(cursor.getString(c));
						field.set(entity, bool);
					} else if (byte[].class == fieldType) {
						field.set(entity, hexStr2Bytes(cursor.getString(c)));
					} else if (Character.TYPE == fieldType) {
						String fieldValue = cursor.getString(c);
						if ((fieldValue != null) && (fieldValue.length() > 0)) {
							field.set(entity, Character.valueOf(fieldValue.charAt(0)));
						}
					}
				}
			}

			list.add((T) entity);
		}
	}

	public long insert(T entity) {
		return insert(entity, true);
	}

	public long insert(T entity, boolean flag) {
		try {
			((BaseModel)entity).check();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
			return -1;
		}
		String sql = "";
		SQLiteDatabase db = null;
		try {
			db = this.dbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			if (flag) {
				sql = setContentValues(entity, cv, TYPE_INCREMENT,
						METHOD_INSERT);// id Self increasing
			} else {
				sql = setContentValues(entity, cv, TYPE_NOT_INCREMENT,
						METHOD_INSERT);// id not Self increasing
			}
			Log.d(TAG, "[insert]: insert into " + this.tableName + " " + sql);
			long row = db.insert(this.tableName, null, cv);
			return row;
		} catch (Exception e) {
			Log.d(this.TAG, "[insert] into DB Exception.");
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}

		return 0L;
	}

	public void delete(int id) {
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		String where = this.idColumn + " = ?";
		String[] whereValue = { Integer.toString(id) };

		Log.d(TAG, "[delete]: delelte from " + this.tableName + " where "
				+ where.replace("?", String.valueOf(id)));

		db.delete(this.tableName, where, whereValue);
		db.close();
	}

	public void delete(Integer... ids) {
		if (ids.length > 0) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < ids.length; i++) {
				sb.append('?').append(',');
			}
			sb.deleteCharAt(sb.length() - 1);
			SQLiteDatabase db = this.dbHelper.getWritableDatabase();
			String sql = "delete from " + this.tableName + " where "
					+ this.idColumn + " in (" + sb + ")";

			Log.d(TAG, "[delete]: " + getLogSql(sql, ids));

			db.execSQL(sql, (Object[]) ids);
			db.close();
		}
	}

	public void update(T entity) {
		try {
			((BaseModel)entity).check();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
			return;
		}
		SQLiteDatabase db = null;
		try {
			db = this.dbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();

			String sql = setContentValues(entity, cv, TYPE_NOT_INCREMENT,
					METHOD_UPDATE);

			String where = this.idColumn + " = ?";
			int id = Integer.parseInt(cv.get(this.idColumn).toString());
			cv.remove(this.idColumn);

			Log.d(TAG, "[update]: update " + this.tableName + " set " + sql
					+ " where " + where.replace("?", String.valueOf(id)));

			String[] whereValue = { Integer.toString(id) };
			db.update(this.tableName, cv, where, whereValue);
		} catch (Exception e) {
			Log.d(this.TAG, "[update] DB Exception.");
			e.printStackTrace();
		} finally {
			if (db != null)
				db.close();
		}
	}

	private String setContentValues(T entity, ContentValues cv, int type,
			int method) throws IllegalAccessException {		
		StringBuffer strField = new StringBuffer("(");
		StringBuffer strValue = new StringBuffer(" values(");
		StringBuffer strUpdate = new StringBuffer(" ");
		for (Field field : this.allFields) {
			if (!field.isAnnotationPresent(Column.class)) {
				continue;
			}
			Column column = (Column) field.getAnnotation(Column.class);

			field.setAccessible(true);
			Object fieldValue = field.get(entity);
			String value = null;
			if (fieldValue == null){
				cv.putNull(column.name());
			}
			else if ((type == TYPE_INCREMENT) && (field.isAnnotationPresent(Id.class))) {
				continue;
			}
			else if (Integer.class == field.getType()) {
				cv.put(column.name(), (int) fieldValue);
				value = Integer.toString((int) fieldValue);
			}
			else if (Date.class == field.getType()) {
				Date date = (Date) fieldValue;
				cv.put(column.name(), date.getTime());
				value = date.toString();
			}
			else if (Boolean.class == field.getType()) {
				value = Boolean.toString((Boolean) fieldValue);
				cv.put(column.name(), value);
			}
			else if (Byte.class == field.getType()) {
				value = Byte.toString((Byte) fieldValue);
				cv.put(column.name(), value);
			}
			else if (Short.class == field.getType()) {
				value = Short.toString((Short) fieldValue);
				cv.put(column.name(), value);
			}
			else if (Long.class == field.getType()) {
				value = Long.toString((Long) fieldValue);
				cv.put(column.name(), value);
			}
			else if (Float.class == field.getType()) {
				value = Float.toString((Float) fieldValue);
				cv.put(column.name(), value);
			}
			else if (Double.class == field.getType()) {
				value = Double.toString((Double) fieldValue);
				cv.put(column.name(), value);
			}
			else if (byte[].class == field.getType()) {
				value = bytes2HexStr((byte[]) fieldValue);
				cv.put(column.name(), value);
			}
			else {
				value = String.valueOf(fieldValue);
				cv.put(column.name(), value);
			}
			if (method == METHOD_INSERT) {
				strField.append(column.name()).append(",");
				strValue.append("'").append(value).append("',");
			} else {
				strUpdate.append(column.name()).append("=").append("'").append(value).append("',");
			}

		}
		if (method == METHOD_INSERT) {
			strField.deleteCharAt(strField.length() - 1).append(")");
			strValue.deleteCharAt(strValue.length() - 1).append(")");
			return strField.toString() + strValue.toString();
		} else {
			return strUpdate.deleteCharAt(strUpdate.length() - 1).append(" ")
					.toString();
		}
	}

	/**
	 * execute sql query and return the query results
	 * @param sql
	 * @param selectionArgs
	 * @return The keys in the returned map are all lowercase.
	 */
	public List<Map<String, String>> query2MapList(String sql,
			String[] selectionArgs) {
		Log.d(TAG, "[query2MapList]: " + getLogSql(sql, selectionArgs));
		SQLiteDatabase db = null;
		Cursor cursor = null;
		List<Map<String, String>> retList = new ArrayList<Map<String, String>>();
		try {
			db = this.dbHelper.getReadableDatabase();
			cursor = db.rawQuery(sql, selectionArgs);
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (String columnName : cursor.getColumnNames()) {
					int c = cursor.getColumnIndex(columnName);
					if (c < 0) {
						continue; // if not exist continue
					} else {
						map.put(columnName, cursor.getString(c));
					}
				}
				retList.add(map);
			}
		} catch (Exception e) {
			Log.e(TAG, "[query2MapList] from DB exception");
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}

		return retList;
	}

	/**
	 * execute sql.
	 * 
	 * @param sql
	 * @param selectionArgs
	 */
	public void execSql(String sql, Object[] selectionArgs) {
		SQLiteDatabase db = null;
		Log.d(TAG, "[execSql]: " + getLogSql(sql, selectionArgs));
		try {
			db = this.dbHelper.getWritableDatabase();
			if (selectionArgs == null) {
				db.execSQL(sql);
			} else {
				db.execSQL(sql, selectionArgs);
			}
		} catch (Exception e) {
			Log.e(TAG, "[execSql] DB exception.");
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

	private String getLogSql(String sql, Object[] args) {
		if (args == null || args.length == 0) {
			return sql;
		}
		for (int i = 0; i < args.length; i++) {
			sql = sql.replaceFirst("\\?", "'" + String.valueOf(args[i]) + "'");
		}
		return sql;
	}

	private byte[] hexStr2Bytes(String data) {
		if(data==null)
			return null;
		if(data.length() == 0)
			return new byte[0];
		byte[] result = new byte[(data.length() + 1) / 2];
		if ((data.length() & 1) == 1) {
			data = data + "0";
		}
		for(int i = 0; i < result.length; ++i) {
			result[i] = (byte)(hex2byte(data.charAt(i * 2 + 1)) | hex2byte(data.charAt(i * 2)) << 4);
		}
		return result;
	}

	private byte hex2byte(char hex) {
		if (hex <= 'f' && hex >= 'a') {
			return (byte)(hex - 97 + 10);
		} else if (hex <= 'F' && hex >= 'A') {
			return (byte)(hex - 65 + 10);
		} else {
			return hex <= '9' && hex >= '0' ? (byte)(hex - 48) : 0;
		}
	}

	private String bytes2HexStr(byte[] data) {
		if(data==null)
			return null;
		if(data.length==0)
			return "";
		StringBuilder buffer = new StringBuilder();
		for (byte b : data) {
			String hex = Integer.toHexString(b & 0xFF);
			if (hex.length() == 1) {
				buffer.append('0');
			}
			buffer.append(hex);
		}
		return buffer.toString().toUpperCase();
	}
}