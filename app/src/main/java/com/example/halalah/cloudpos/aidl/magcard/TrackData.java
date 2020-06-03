package com.example.halalah.cloudpos.aidl.magcard;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 磁条数据
 * 
 * @author Tianxiaobo
 * 
 */
public class TrackData implements Parcelable {

	
	private String firstTrackData = null; // 一磁道数据
	private String secondTrackData = null; // 二磁道数据
	private String thirdTrackData = null; // 三磁道数据
	private String cardno = null; // 卡号
	private String formatTrackData = null; // 二三磁格式化数据
	private String expiryDate = null; // 卡片有效期
	private String serviceCode = null; // 服务码

	public TrackData(String cardno,
			String firstTrackData, String secondTrackData,
			String thirdTrackData, String formatTrackData, String expiryDate,
			String serviceCode) {
		this.cardno = cardno;
		this.firstTrackData = firstTrackData;
		this.secondTrackData = secondTrackData;
		this.thirdTrackData = thirdTrackData;
		this.expiryDate = expiryDate;
		this.formatTrackData = formatTrackData;
		this.serviceCode = serviceCode;
	}

	public TrackData(Parcel des) {
		this.cardno = des.readString();
		this.firstTrackData = des.readString();
		this.secondTrackData = des.readString();
		this.thirdTrackData = des.readString();
		this.formatTrackData = des.readString();
		this.expiryDate = des.readString();
		this.serviceCode = des.readString();
	}

	public TrackData(){}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(cardno); // 卡号
		dest.writeString(firstTrackData); // 一磁道数据
		dest.writeString(secondTrackData); // 二磁道数据
		dest.writeString(thirdTrackData); // 三磁道数据
		dest.writeString(formatTrackData); // 二三磁加密数据
		dest.writeString(expiryDate); // 卡片有效期
		dest.writeString(serviceCode); // 服务码
	}

	public static final Creator<TrackData> CREATOR = new Creator<TrackData>() {

		@Override
		public TrackData createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			TrackData trackData = new TrackData(source);
			return trackData;
		}

		@Override
		public TrackData[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}
	};

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getFirstTrackData() {
		return firstTrackData;
	}

	public void setFirstTrackData(String firstTrackData) {
		this.firstTrackData = firstTrackData;
	}

	public String getSecondTrackData() {
		return secondTrackData;
	}

	public void setSecondTrackData(String secondTrackData) {
		this.secondTrackData = secondTrackData;
	}

	public String getThirdTrackData() {
		return thirdTrackData;
	}

	public void setThirdTrackData(String thirdTrackData) {
		this.thirdTrackData = thirdTrackData;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getFormatTrackData() {
		return formatTrackData;
	}

	public void setFormatTrackData(String formatTrackData) {
		this.formatTrackData = formatTrackData;
	}
}
