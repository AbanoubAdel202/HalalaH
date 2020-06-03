package com.example.halalah.cloudpos.aidl.emv;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * EMV参数控制
 * 
 * @author Tianxiaobo
 * 
 */
public class EmvTransData implements Parcelable {
	private byte transtype;
	private byte requestAmtPosition;
	private boolean isEcashEnable;
	private boolean isSmEnable;
	private boolean isForceOnline;
	private byte emvFlow;
	private byte slotType;
	private byte[] reserv;

	public EmvTransData(Parcel source) {
		this.transtype = source.readByte();
		this.requestAmtPosition = source.readByte();
		this.isEcashEnable = (Boolean) source.readValue(Boolean.class
				.getClassLoader());
		this.isSmEnable = (Boolean) source.readValue(Boolean.class
				.getClassLoader());
		this.isForceOnline = (Boolean) source.readValue(Boolean.class
				.getClassLoader());
		this.emvFlow = source.readByte();
		this.slotType = source.readByte();
		this.reserv = (byte[]) source.readValue(byte[].class.getClassLoader());
	}
	
	
	
	public EmvTransData(byte transtype, byte requestAmtPosition,
			boolean isEcashEnable, boolean isSmEnable, boolean isForceOnline,
			byte emvFlow, byte slotType, byte[] reserv) {
		super();
		this.transtype = transtype;
		this.requestAmtPosition = requestAmtPosition;
		this.isEcashEnable = isEcashEnable;
		this.isSmEnable = isSmEnable;
		this.isForceOnline = isForceOnline;
		this.emvFlow = emvFlow;
		this.slotType = slotType;
		this.reserv = reserv;
	}



	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte(this.transtype);
		dest.writeByte(this.requestAmtPosition);
		dest.writeValue(isEcashEnable);
		dest.writeValue(isSmEnable);
		dest.writeValue(isForceOnline);
		dest.writeByte(emvFlow);
		dest.writeByte(slotType);
		dest.writeValue(reserv);

	}

	public static final Creator<EmvTransData> CREATOR = new Creator<EmvTransData>() {

		@Override
		public EmvTransData createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			EmvTransData emvTransData = new EmvTransData(source);
			return emvTransData;
		}

		@Override
		public EmvTransData[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}
	};

	public byte getTranstype() {
		return transtype;
	}

	public void setTranstype(byte transtype) {
		this.transtype = transtype;
	}

	public byte getRequestAmtPosition() {
		return requestAmtPosition;
	}

	public void setRequestAmtPosition(byte requestAmtPosition) {
		this.requestAmtPosition = requestAmtPosition;
	}

	public boolean isEcashEnable() {
		return isEcashEnable;
	}

	public void setEcashEnable(boolean isEcashEnable) {
		this.isEcashEnable = isEcashEnable;
	}

	public boolean isSmEnable() {
		return isSmEnable;
	}

	public void setSmEnable(boolean isSmEnable) {
		this.isSmEnable = isSmEnable;
	}

	public boolean isForceOnline() {
		return isForceOnline;
	}

	public void setForceOnline(boolean isForceOnline) {
		this.isForceOnline = isForceOnline;
	}

	public byte getEmvFlow() {
		return emvFlow;
	}

	public void setEmvFlow(byte emvFlow) {
		this.emvFlow = emvFlow;
	}

	public byte getSlotType() {
		return slotType;
	}

	public void setSlotType(byte slotType) {
		this.slotType = slotType;
	}

	public byte[] getReserv() {
		return reserv;
	}

	public void setReserv(byte[] reserv) {
		this.reserv = reserv;
	}

	public static Creator<EmvTransData> getCreator() {
		return CREATOR;
	}
}
