package com.example.halalah.packet;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;

import com.example.halalah.POSTransaction;
import com.example.halalah.PosApplication;
import com.example.halalah.Utils;
import com.example.halalah.cache.ConsumeData;
import com.example.halalah.iso8583.BCDASCII;
import com.example.halalah.iso8583.ISO8583;
import com.example.halalah.iso8583.ISO8583Util;

import com.example.halalah.storage.ConsumeFieldUtils;

/** Header POSTransaction
 \Class Name: POSTransaction
 \Param  :
 \Return :
 \Pre    :
 \Post   :
 \Author	: Mostafa hussiny
 \DT		: 5/10/2020
 \Des    : building customized message for Purchase financial message 1200
 */
public class PackPurchase {
	private static final String TAG = Utils.TAGPUBLIC + PackPurchase.class.getSimpleName();
	private byte[] mPacketMsg = null;

	public PackPurchase(ISO8583 mISO8583 ) {

		POSTransaction postransaction = PosApplication.getApp().oGPosTransaction;

		mISO8583 = new ISO8583();
		mISO8583.ClearFields();

		POSTransaction.CardType cardType;
		byte[] bfield2 = null;
		String sfield3 = null;
		String sfield4 = null;
		String sfield7 = null;
		String sfield11 = null;
		String sfield12 = null;
		String sfield14 = null;  //c2
		String sfield22 = null;
		String sfield23 = null; //c3
		String sfield24 = null;
		String sfield25 = null; //c18
		String sfield26 = null;
		String sfield32 = null;
		byte[] bfield35 = null; //c4
		String sfield37 = null;
		String sfield41 = null;
		String sfield42 = null;
		String sfield47 = null;
		String sfield49 = null;
		byte[] bfield52 = null; //c6
		String sfield53 = null;
		String sfield54 = null; //c8
		byte[] bfield55 = null; //c7
		String sfield56 = null; //c16
		String sfield59 = null; // c_
		String sfield62 = null;
		byte[] bfield64 = null;

		cardType = postransaction.m_enmTrxCardType;
		bfield52 = postransaction.m_sTrxPIN.getBytes();

		mISO8583.SetDataElement(0, PackUtils.MSGTYPEID_Financial_Request.getBytes(), PackUtils.MSGTYPEID_Financial_Request.length());



		String cardNum = postransaction.m_sPAN;
		if (cardNum != null) {
			bfield2 = PackUtils.getTrackField(cardNum, ISO8583.LLVAR_LEN);
			mISO8583.SetDataElement(2, bfield2, bfield2.length);
			Log.i(TAG, "2 = " + BCDASCII.bytesToHexString(bfield2));

		}

		sfield3 = "000000";
		mISO8583.SetDataElement(3, sfield3.getBytes(), sfield3.length());
		Log.i(TAG, "3 = " + sfield3);


		String amount = postransaction.m_sTrxAmount;
		sfield4 = PackUtils.getField4(amount);
		mISO8583.SetDataElement(4, sfield4.getBytes(), sfield4.length());
		Log.i(TAG, "4 = " + sfield4);

		sfield7 = postransaction.m_sLocalTrxDateTime;
		if (sfield7 != null) {
			mISO8583.SetDataElement(7, sfield7.getBytes(), sfield7.length());
			Log.d(TAG, "7 = " + sfield7);

		}

		sfield11 = postransaction.m_sOrigSTAN;
		if (sfield11 != null) {
			mISO8583.SetDataElement(11, sfield11.getBytes(), sfield11.length());
			Log.d(TAG, "11 = " + sfield11);

		}
		sfield12 = postransaction.m_sLocalTrxDateTime;
		if (sfield12 != null) {
			mISO8583.SetDataElement(12, sfield12.getBytes(), sfield12.length());
			Log.d(TAG, "12 = " + sfield12);

		}

		if (cardType == POSTransaction.CardType.MANUAL) {
			sfield14 = postransaction.m_sCardExpDate;
			if (sfield14 != null) {
				mISO8583.SetDataElement(14, sfield14.getBytes(), sfield14.length());
				Log.d(TAG, "14 = " + sfield14);
			}
		}

		if (cardType == POSTransaction.CardType.MAG) {
			if (bfield52 != null) {
				sfield22 = "021";
			} else {
				sfield22 = "022";
			}
		} else if (cardType == POSTransaction.CardType.ICC) {
			if (bfield52 != null) {
				sfield22 = "051";
			} else {
				sfield22 = "052";
			}
		} else if (cardType == POSTransaction.CardType.CTLS) {
			if (bfield52 != null) {
				sfield22 = "071";
			} else {
				sfield22 = "072";
			}
		}
		mISO8583.SetDataElement(22, sfield22.getBytes(), sfield22.length());
		Log.d(TAG, "22 = " + sfield22);

		if (cardType == POSTransaction.CardType.ICC) {
			sfield23 = postransaction.m_sCardSeqNum;
			mISO8583.SetDataElement(23, sfield23.getBytes(), sfield23.length());
			Log.d(TAG, "23 = " + sfield23);
		}

		sfield24 = postransaction.m_sFunctionCode;
		sfield23 = postransaction.m_sCardSeqNum;
		mISO8583.SetDataElement(24, sfield23.getBytes(), sfield24.length());
		Log.d(TAG, "24 = " + sfield24);


		sfield25 = postransaction.m_sFunctionCode;
		mISO8583.SetDataElement(25, sfield25.getBytes(), sfield25.length());

		sfield26 = postransaction.m_sCardAcceptorBusinessCode;
		mISO8583.SetDataElement(26, sfield26.getBytes(), sfield26.length());

		sfield32 = postransaction.m_sAquirerInsIDCode;
		mISO8583.SetDataElement(32, sfield32.getBytes(), sfield32.length());

		if ((cardType == POSTransaction.CardType.MAG) || (cardType == POSTransaction.CardType.ICC)) {
			bfield35 = postransaction.m_sTrack2.getBytes();
			mISO8583.SetDataElement(35, bfield35, bfield35.length);
			Log.i(TAG, "35 = " + BCDASCII.bytesToHexString(bfield35));
		}

		sfield37 = postransaction.m_sRRNumber;
		mISO8583.SetDataElement(37, sfield37.getBytes(), sfield37.length());
		Log.i(TAG, "37 = " + sfield37);


		sfield41 = postransaction.m_sTerminalID;
		mISO8583.SetDataElement(41, sfield41.getBytes(), sfield41.length());
		Log.i(TAG, "41 = " + sfield41);

		sfield42 = postransaction.m_sMerchantID;
		mISO8583.SetDataElement(42, sfield42.getBytes(), sfield42.length());
		Log.i(TAG, "42 = " + sfield42);

		sfield47 = postransaction.m_sCardSchemeSponsorID;
		mISO8583.SetDataElement(47, sfield47.getBytes(), sfield47.length());
		Log.i(TAG, "47 = " + sfield47);

		sfield49 = postransaction.m_sCurrencyCode;
		mISO8583.SetDataElement(41, sfield49.getBytes(), sfield49.length());
		Log.i(TAG, "49 = " + sfield49);

		bfield52 = postransaction.m_sTrxPIN.getBytes();
		if (bfield52 != null) {
			mISO8583.SetDataElement(52, bfield52, bfield52.length);
			Log.d(TAG, "52 = " + BCDASCII.bytesToHexString(bfield52));
		}

		sfield53 = postransaction.m_sTrxSecurityControl;
		if (sfield53 != null) {
			mISO8583.SetDataElement(53, sfield53.getBytes(), sfield53.length());
			Log.d(TAG, "53 = " + sfield53);
		}
		sfield54 = postransaction.m_sAdditionalAmount;
		if (sfield54 != null) {
			mISO8583.SetDataElement(54, sfield53.getBytes(), sfield54.length());
			Log.d(TAG, "54 = " + sfield54);
		}

		if (cardType == POSTransaction.CardType.ICC)
			bfield55 = postransaction.m_sICCRelatedTags.getBytes();
		if (bfield55 != null) {
			mISO8583.SetDataElement(55, bfield55, bfield55.length);
			Log.d(TAG, "55 = " + BCDASCII.bytesToHexString(bfield55));
		}


		sfield56 = postransaction.m_sOriginalTrxData;     // we can check here if refund only
		if (sfield56 != null) {
			mISO8583.SetDataElement(56, sfield56.getBytes(), sfield56.length());
			Log.d(TAG, "56 = " + sfield56);
		}

		sfield59 = postransaction.m_sTransportData;
		if (sfield59 != null) {
			mISO8583.SetDataElement(59, sfield59.getBytes(), sfield59.length());
			Log.d(TAG, "59 = " + sfield59);
		}

		sfield62 = postransaction.m_sTerminalStatus;
		if (sfield62 != null) {
			mISO8583.SetDataElement(62, sfield62.getBytes(), sfield62.length());
			Log.d(TAG, "62 = " + sfield62);
		}
		bfield64 = postransaction.m_sTrxMACBlock.getBytes();
		mISO8583.SetDataElement(64, bfield64, bfield64.length);
		Log.d(TAG, "64 = " + BCDASCII.bytesToHexString(bfield64));

		byte[] isobyte = mISO8583.isotostr();
		mPacketMsg = PackUtils.getPacketHeader(isobyte, "termId", "merId");
	}

	public byte[] get() {
		return mPacketMsg;
	}
}

