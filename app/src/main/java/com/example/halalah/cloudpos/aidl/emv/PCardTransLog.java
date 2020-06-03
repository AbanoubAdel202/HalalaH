package com.example.halalah.cloudpos.aidl.emv;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * IC卡交易日志
 * 
 * @author Tianxiaobo
 * 
 */
public class PCardTransLog implements Parcelable {
	/** 交易日期 */
	private String transDate;
	/**交易时间*/
	private String transTime;
	/** 授权金额 */
	private String amt;
	/** 其他金额 */
	private String otheramt;
	/** 国家代码 */
	private String countryCode;
	/** 货币代码 */
	private String moneyCode;
	/** 商户名称 */
	private String merchantName;
	/** 交易类型 */
	private int transtype;
	/** 应用交易计数器 */
	private byte[] appTransCount = new byte[2];

	public PCardTransLog() {

	}
	
	public PCardTransLog(String transDate, String transTime, String amt,
			String otheramt, String countryCode, String moneyCode,
			String merchantName, int transtype, byte[] appTransCount) {
		super();
		this.transDate = transDate;
		this.transTime = transTime;
		this.amt = amt;
		this.otheramt = otheramt;
		this.countryCode = countryCode;
		this.moneyCode = moneyCode;
		this.merchantName = merchantName;
		this.transtype = transtype;
		this.appTransCount = appTransCount;
	}




	private PCardTransLog(Parcel source) {

		this.transDate = source.readString();
		this.transTime = source.readString();
		this.amt = source.readString();
		this.otheramt = source.readString();
		this.countryCode = source.readString();
		this.moneyCode = source.readString();
		this.merchantName = source.readString();
		this.transtype = source.readInt();
		if (this.appTransCount == null) {
			this.appTransCount = new byte[2];
		}
		source.readByteArray(this.appTransCount);

	}

	public static final Creator<PCardTransLog> CREATOR = new Creator<PCardTransLog>() {

		@Override
		public PCardTransLog createFromParcel(Parcel source) {
			return new PCardTransLog(source);
		}

		@Override
		public PCardTransLog[] newArray(int size) {
			return new PCardTransLog[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(transDate);
		dest.writeString(transTime);
		dest.writeString(amt);
		dest.writeString(otheramt);
		dest.writeString(countryCode);
		dest.writeString(moneyCode);
		dest.writeString(merchantName);
		dest.writeInt(transtype);
		dest.writeByteArray(appTransCount);

	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public String getAmt() {
		return amt;
	}

	public void setAmt(String amt) {
		this.amt = amt;
	}

	public String getOtheramt() {
		return otheramt;
	}

	public void setOtheramt(String otheramt) {
		this.otheramt = otheramt;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getMoneyCode() {
		return moneyCode;
	}

	public void setMoneyCode(String moneyCode) {
		this.moneyCode = moneyCode;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public int getTranstype() {
		return transtype;
	}

	public void setTranstype(int transtype) {
		this.transtype = transtype;
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

}
