package com.example.halalah.cloudpos.aidl.emv;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 卡数据bean
 * 
 * @author Tianxiaobo
 * 
 */
public class CardInfo implements Parcelable {

	private String cardno = null;

	public CardInfo(Parcel source) {
		this.cardno = source.readString();
	}
	
	public CardInfo(String cardno){
		this.cardno = cardno;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public static Creator<CardInfo> getCreator() {
		return CREATOR;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.cardno);
	}

	public static final Creator<CardInfo> CREATOR = new Creator<CardInfo>() {

		@Override
		public CardInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			CardInfo cardno = new CardInfo(source);
			return cardno;
		}

		@Override
		public CardInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new CardInfo[size];
		}
	};
}
