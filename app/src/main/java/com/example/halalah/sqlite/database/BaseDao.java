package com.example.halalah.sqlite.database;

import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;
import java.util.Map;

public interface BaseDao<T> {

	SQLiteOpenHelper getDbHelper();

	/**
	 * Auto generate primary key,call insert(T,true);
	 * 
	 * @param entity to be saved into the db
	 * @return
	 */
	long insert(T entity);

	/**
	 * 插入实体类
	 * 
	 * @param entity to be saved into the db
	 * @param flag true:Auto generate primary key,false:need to specify a primary key.
	 * @return
	 */
	long insert(T entity, boolean flag);

	void delete(int id);

	void delete(Integer... ids);

	void update(T entity);

	T get(int id);

	List<T> rawQuery(String sql, String[] selectionArgs);

	List<T> find();

	List<T> find(String[] columns, String selection,
                                 String[] selectionArgs, String groupBy, String having,
                                 String orderBy, String limit);

	boolean isExist(String sql, String[] selectionArgs);

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