package com.example.halalah.database;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( { java.lang.annotation.ElementType.TYPE })
public @interface Table {
	/**
	 * the name of table
	 * 
	 * @return
	 */
	public abstract String name();
}