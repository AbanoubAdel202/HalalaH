package com.example.halalah.database;

import java.util.List;
import java.util.Map;

import android.database.sqlite.SQLiteOpenHelper;

public interface BaseDao<T> {

	public SQLiteOpenHelper getDbHelper();

	/**
	 * Auto generate primary key,call insert(T,true);
	 * 
	 * @param entity
	 * @return
	 */
	public abstract long insert(T entity);

	/**
	 * 插入实体类
	 * 
	 * @param entity
	 * @param flag true:Auto generate primary key,false:need to specify a primary key.
	 * @return
	 */
	public abstract long insert(T entity, boolean flag);

	public abstract void delete(int id);

	public abstract void delete(Integer... ids);

	public abstract void update(T entity);

	public abstract T get(int id);

	public abstract List<T> rawQuery(String sql, String[] selectionArgs);

	public abstract List<T> find();

	public abstract List<T> find(String[] columns, String selection,
                                 String[] selectionArgs, String groupBy, String having,
                                 String orderBy, String limit);

	public abstract boolean isExist(String sql, String[] selectionArgs);

	/**
	 * execute sql query and return the query results
	 * @param sql
	 * @param selectionArgs
	 * @return The keys in the returned map are all lowercase.
	 */
	public List<Map<String, String>> query2MapList(String sql,
                                                   String[] selectionArgs);

	/**
	 * execute sql
	 * 
	 * @param sql
	 * @param selectionArgs
	 */
	public void execSql(String sql, Object[] selectionArgs);

}