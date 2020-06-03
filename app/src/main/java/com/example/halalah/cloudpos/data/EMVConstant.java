package com.example.halalah.cloudpos.data;

public class EMVConstant {

	public static class AidCapkOptFlag {
		/**
		 * 增加或者更新
		 */
		public static final int AID_CAPK_OPT_ADDORUPDATE_FLAG = 0x01;

		/**
		 * 删除一条
		 */
		public static final int AID_CAPK_OPT_REMOVE_FLAG = 0x02;

		/**
		 * 删除全部
		 */
		public static final int AID_CAPK_OPT_REMOVEALL_FLAG = 0x03;
	}

	/** 账户类型 **/
	public static class AccountType {
		/**
		 * 默认账户
		 */
		public static final int ACCOUNT_TYPE_DEFAULT = 0x01;

		public static final int ACCOUNT_TYPE_SAVING = 0x02;

		public static final int ACCOUNT_TYPE_CHEQUEORDEBIT = 0X03;

		public static final int ACCOUNT_TYPE_CREDIT = 0x04;
	}

	/** 交易界面类型 **/
	public static class SlotType {
		/**
		 * 接触式
		 */
		public static final int SLOT_TYPE_IC = 0x00;

		/**
		 * 非接触式
		 */
		public static final int SLOT_TYPE_RF = 0x01;

	}

	/** 证件类型 **/
	public static class CertType {
		public static final int CERT_TYPE_ID = 0x00;
		public static final int CERT_TYPE_OFFICER = 0x01;
		public static final int CERT_TYPE_PASSPORT = 0x02;
		public static final int CERT_TYPE_ENTER_COUNTRY = 0x03;
		public static final int CERT_TYPE_TEMP_ID = 0x04;
		public static final int CERT_TYPE_OTHER = 0x05;
	}

	/** PIN类型 **/
	public static class PinType {
		public static final int PINTYPE_OFFLINE = 0x01;
		public static final int PINTYPE_OFFLINE_LASTTIME = 0x02;
		public static final int PINTYPE_ONLINE = 0x03;

	}

	/** 读取卡片圈存日志类型 **/
	public static class ReadCardLoadLogType {
		public static final int ONE_BY_ONE_TYPE = 0x00;
		public static final int ONE_OFF_TYPE = 0x01;
	}
}
