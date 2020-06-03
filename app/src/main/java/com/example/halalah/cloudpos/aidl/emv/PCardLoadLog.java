package com.example.halalah.cloudpos.aidl.emv;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 圈存日志
 * 
 * @author Tianxiaobo
 * 
 */
public class PCardLoadLog implements Parcelable {

	/** Put Data 命令的P1 值：1 字节 **/
	private String putdata_p1;

	/** Put Data 命令的P2 值:1 字节 **/
	private String putdata_p2;

	/** Put Data 修改前xxxx（9F79 或DF79）的值：6 字节 **/
	private String before_putdata;

	/** Put Data 修改后xxxx（9F79 或DF79）的值：6 字节 **/
	private String after_putdata;

	/** 交易日期 **/
	private String transDate;

	/** 交易时间 **/
	private String transTime;

	/** 应用交易计数器 */
	private byte[] appTransCount = new byte[2];

	public PCardLoadLog() {
	}
	
	

	public PCardLoadLog(String putdata_p1, String putdata_p2,
			String before_putdata, String after_putdata, String transDate,
			String transTime, byte[] appTransCount) {
		super();
		this.putdata_p1 = putdata_p1;
		this.putdata_p2 = putdata_p2;
		this.before_putdata = before_putdata;
		this.after_putdata = after_putdata;
		this.transDate = transDate;
		this.transTime = transTime;
		this.appTransCount = appTransCount;
	}



	private PCardLoadLog(Parcel source) {

		this.putdata_p1 = source.readString();
		this.putdata_p2 = source.readString();
		this.before_putdata = source.readString();
		this.after_putdata = source.readString();

		this.transDate = source.readString();
		this.transTime = source.readString();

		if (this.appTransCount == null) {
			this.appTransCount = new byte[2];
		}
		source.readByteArray(this.appTransCount);

	}

	public static final Creator<PCardLoadLog> CREATOR = new Creator<PCardLoadLog>() {

		@Override
		public PCardLoadLog createFromParcel(Parcel source) {
			return new PCardLoadLog(source);
		}

		@Override
		public PCardLoadLog[] newArray(int size) {
			return new PCardLoadLog[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(putdata_p1);
		dest.writeString(putdata_p2);
		dest.writeString(before_putdata);
		dest.writeString(after_putdata);
		dest.writeString(transDate);
		dest.writeString(transTime);
		dest.writeByteArray(appTransCount);

	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public byte[] getAppTransCount() {
		return appTransCount;
	}

	public void setAppTransCount(byte[] appTransCount) {
		this.appTransCount = appTransCount;
	}

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	public String getPutdata_p1() {
		return putdata_p1;
	}

	public void setPutdata_p1(String putdata_p1) {
		this.putdata_p1 = putdata_p1;
	}

	public String getPutdata_p2() {
		return putdata_p2;
	}

	public void setPutdata_p2(String putdata_p2) {
		this.putdata_p2 = putdata_p2;
	}

	public String getBefore_putdata() {
		return before_putdata;
	}

	public void setBefore_putdata(String before_putdata) {
		this.before_putdata = before_putdata;
	}

	public String getAfter_putdata() {
		return after_putdata;
	}

	public void setAfter_putdata(String after_putdata) {
		this.after_putdata = after_putdata;
	}

}
