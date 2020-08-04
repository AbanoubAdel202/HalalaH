package com.example.halalah.database;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( { java.lang.annotation.ElementType.FIELD })
/**
 * 
 * @author caixh
 *
 */
public @interface Column {
	/**
	 * the name of column
	 * 
	 * @return
	 */
	public abstract String name();
	/**
	 * if the type is String,the style is regular expressions that restrict strings，
	 * if the type is byte/int/short/long/double,the style is the size range "min, max"
	 *    (+And - denote positive and negative infinity respectively)
	 * if the type is an array,the style is the range of length "min, max"
	 * @return
	 */
	public abstract String style() default "";
	/**
	 *
	 *is it unique
	 *
	 * @return
	 */
	public abstract boolean unique() default false;
}