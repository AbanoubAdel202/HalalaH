package com.example.halalah.sqlite.database;

import com.example.halalah.util.BytesUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseStruct implements Cloneable {
    public BaseStruct() {
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String toString() {
        StringBuffer sb = (new StringBuffer(this.getClass().getSimpleName())).append("[");
        List<Class> list = new ArrayList();

        for(Class c = this.getClass(); c != BaseStruct.class; c = c.getSuperclass()) {
            list.add(c);
        }

        for(int i = 0; i < list.size(); ++i) {
            Field[] var4 = ((Class)list.get(i)).getDeclaredFields();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Field field = var4[var6];
                field.setAccessible(true);

                try {
                    sb.append(field.getName()).append("=");
                    Class<?> type = field.getType();
                    if (type == Byte.TYPE) {
                        byte value = (Byte)field.get(this);
                        sb.append("0x").append(Integer.toHexString(value));
                    } else if (type == byte[].class) {
                        byte[] value = (byte[])((byte[])field.get(this));
                        sb.append("0x(").append(BytesUtil.bytes2HexString(value)).append(")");
                    } else if (type == Integer.TYPE) {
                        int value = (Integer)field.get(this);
                        sb.append(Integer.toString(value));
                    } else if (type == Short.TYPE) {
                        short value = (Short)field.get(this);
                        sb.append(Integer.toString(value));
                    } else if (type == Boolean.TYPE) {
                        boolean value = (Boolean)field.get(this);
                        sb.append(Boolean.toString(value));
                    } else {
                        sb.append(field.get(this).toString());
                    }

                    sb.append(",");
                } catch (Exception var10) {
                    var10.printStackTrace();
                }
            }
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }
}
