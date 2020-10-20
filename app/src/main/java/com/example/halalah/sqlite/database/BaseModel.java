package com.example.halalah.sqlite.database;


import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * The base class of all table classes,and the field with @Column will also be added to the table of sub classes.
 * author caixh
 * */
public class BaseModel extends BaseStruct {
	/**primary key,INT,auto increment*/
	@Id
	@Column(name = "id")
	public int id;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void check() throws Exception {
		Table table = getClass().getAnnotation(Table.class);
		String tableName = table.name();
		for (Field field :getClass().getDeclaredFields()) {
			field.setAccessible(true);
			if (!field.isAnnotationPresent(Column.class)) {
				continue;
			}
			Column column = field.getAnnotation(Column.class);
			String style = column.style();
			if("".equals(style)||"-,+".equals(style)){
				return;
			}
			boolean hasmin = false,hasmax = false;
			double min=0.0,max=0.0;
			if(!Pattern.compile("(\\-)?(\\d+(\\.\\d+)?)?\\,((\\+)|((\\-)?\\d+(\\.\\d+)?)?)").matcher(style).matches()){
				throw new Exception("The style format of the "+field.getName()+" field in the "+tableName+" table does not conform to \"min,max\"");
			}
			String[] sa = style.split(",");
			if(!sa[0].equals("-")){
				hasmin = true;
				min = Double.parseDouble(sa[0]);
			}
			if(!sa[1].equals("+")){
				hasmax = true;
				max = Double.parseDouble(sa[1]);
			}
			if(hasmin&&hasmax&&min>max){
				throw new Exception("In the style format of "+field.getName()+" field in "+tableName+" table, min > max");
			}
			if (byte[].class == field.getType()) {
				byte[] obj = (byte[]) field.get(this);
				if(hasmin&&obj.length<(int)min){
					throw new Exception("The length of "+field.getName()+" field in "+tableName+" table cannot be less than "+(int)min);
				}
				if(hasmax&&obj.length>(int)max){
					throw new Exception("The length of "+field.getName()+" field in "+tableName+" table cannot be greater than "+(int)max);
				}
			}
			else if (String.class == field.getType()) {
				String obj = field.get(this).toString();
				if(hasmin&&obj.length()<(int)min){
					throw new Exception("The length of "+field.getName()+" field in "+tableName+" table cannot be less than "+(int)min);
				}
				if(hasmax&&obj.length()>(int)max){
					throw new Exception("The length of "+field.getName()+" field in "+tableName+" table cannot be greater than "+(int)max);
				}
			}
			else if (Float.TYPE == field.getType()|| Double.TYPE == field.getType()) {
				double obj = Double.parseDouble(field.get(this).toString());
				if(hasmin&&obj<min){
					throw new Exception("The value of "+field.getName()+" field in "+tableName+" table cannot be less than "+(int)min);
				}
				if(hasmax&&obj>max){
					throw new Exception("The value of "+field.getName()+" field in "+tableName+" table cannot be greater than "+(int)max);
				}
			}
			else if (Byte.TYPE == field.getType()|| Short.TYPE == field.getType()|| Integer.TYPE == field.getType()|| Long.TYPE == field.getType()) {
				long obj = Long.parseLong(field.get(this).toString());
				if(hasmin&&obj<(long)min){
					throw new Exception("The value of "+field.getName()+" field in "+tableName+" table cannot be less than "+(int)min);
				}
				if(hasmax&&obj>(long)max){
					throw new Exception("The value of "+field.getName()+" field in "+tableName+" table cannot be greater than "+(int)max);
				}
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		String newLine = System.getProperty("line.separator");

		result.append(this.getClass().getName());
		result.append(" Object {");
		result.append(newLine);

		//determine fields declared in this class only (no fields of superclass)
		Field[] fields = this.getClass().getDeclaredFields();

		//print field names paired with their values
		for (Field field : fields) {
			if (field.getName().endsWith("Str")) {
				continue;
			}
			result.append("  ");
			try {
				field.setAccessible(true);
				result.append(field.getName());
				result.append(": ");
				//requires access to private field:
				result.append(field.get(this));
			} catch (IllegalAccessException ex) {
				System.out.println(ex);
			}
			result.append(newLine);
		}
		result.append("}");

		return result.toString();
	}
	
}
