package com.example.halalah.cloudpos.data;

/**
 * 打印机具名常量
 * 
 * @author Administrator
 * 
 */
public class PrinterConstant {
	/**
	 * 字体大小
	 */
	public static class FontSize{
		public static int SMALL = 4;
		public static int NORMAL = 8;
		public static int LARGE = 16;
		public static int XLARGE = 24;
	}
	/**
	 * 打印机状态
	 * @author Administrator
	 *
	 */
	public static class PrinterState {
		/** 正常 */
		public static int PRINTER_STATE_NORMAL = 0x00;
		/** 缺纸 */
		public static int PRINTER_STATE_NOPAPER = 0x01;
		/** 高温 */
		public static int PRINTER_STATE_HIGHTEMP = 0x02;
		/** 未知异常 */
		public static int PRINTER_STATE_UNKNOWN = 0x03;
		/** 设备未打开 */
		public static int PRINTER_STATE_NOT_OPEN = 0x04;
		/** 设备通讯异常 */
		public static int PRINTER_STATE_DEV_ERROR = 0x05;

	}
	/**
	 * 条码类型定义
	 */
	public static class BarCodeType {
		
		public final static int BARCODE_TYPE_UPCA = 65;

		public final static int BARCODE_TYPE_UPCE = 66;

		public final static int BARCODE_TYPE_JAN13 = 67;

		public final static int BARCODE_TYPE_JAN8 = 68;

		public final static int BARCODE_TYPE_CODE39 = 69;

		public final static int BARCODE_TYPE_ITF = 70;

		public final static int BARCODE_TYPE_CODEBAR = 71;

		public final static int BARCODE_TYPE_CODE93 = 72;

		public final static int BARCODE_TYPE_CODE128 = 73;
	}
}
