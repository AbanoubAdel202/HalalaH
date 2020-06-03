package com.example.halalah.cloudpos.data;

/**
 * 错误码常量类，每个设备单独建立一个内部类存放各自设备的错误码
 * 
 * @author Administrator
 * 
 */
public class AidlErrorCode {

	/**
	 * EMV模块错误码
	 * 
	 * @author Administrator
	 * 
	 */
	public static class EMV {
		/** 和EMV内核通讯出错 */
		public static int EMV_KERNEL_COMMUNICATE_ERROR = -0x04;
		/** EMV内核执行出错 */
		public static int EMV_KERNEL_EXCUTE_ERROR = -0x05;
		/** EMV内核参数错误 */
		public static int EMV_ILLIGAL_PARAMETER_ERROR = -0x07;
		/** 读取EMV内核参数错 */
		public static int EMV_READ_KERNEL_DATA_FAIL = -0x08;
		/** 未知的EMV内核事件 */
		public static int EMV_KERNEL_UNKNOWN_EVENTID_ERROR = -0x06;
		/** 磁条卡设备异常 */
		public static int ERROR_CHECK_MAGCARD_ERROR = -0x03;// 磁条卡设备异常
		/** IC卡设备异常 */
		public static int ERROR_CHECK_ICCARD_ERROR = -0x01;// IC卡设备异常
		/** RF卡设备异常 */
		public static int ERROR_CHECK_RFCARD_ERROR = -0x02;// RF卡设备异常
		/** IC卡公钥为空*/
		public static int EMV_PUBLICKEY_NULL = -0x01;// IC卡公钥不存在
		/** AID参数为空*/
		public static int EMV_AID_NULL = -0x02;// AID参数不存在
		/** AID参数、IC卡公钥都为空*/
		public static int EMV_PUBLICKEY_AID_NULL = -0x03;// AID参数、IC卡公钥都为空

	}

	/**
	 * 打印机设备错误码
	 * 
	 * @author Administrator
	 * 
	 */
	public static class Printer {
		/** 缺纸 */
		public static int ERROR_PRINT_NOPAPER = 0x01;
		/*** 高温 */
		public static int ERROR_PRINT_HOT = 0x02;
		/*** 未知错误 */
		public static int ERROR_PRINT_UNKNOWN = 0x03;
		/** 设备未打开 */
		public static int ERROR_DEV_NOT_OPEN = 0x04;
		/** 设备忙 */
		public static int ERROR_DEV_IS_BUSY = 0x05;
		/** 打印位图宽度溢出 */
		public static int ERROR_PRINT_BITMAP_WIDTH_OVERFLOW = 0x06;
		/** 打印位图错误 */
		public static int ERROR_PRINT_BITMAP_OTHER = 0x07;
		/** 打印条码错误 */
		public static int ERROR_PRINT_BARCODE_OTHER = 0x08;
		/** 参数错误 */
		public static int ERROR_PRINT_ILLIGALARGUMENT = 0x09;
		/*** 打印文本错误 */
		public static int ERROR_PRINT_TEXT_OTHER = 0x0A;
        /*** mac校验错误(当要求对打印数据进行防串改校验时) */
        public static int ERROR_PRINT_DATA_MAC = 0x0B;
	}

	/**
	 * 磁条卡设备错误码
	 * 
	 * @author Administrator
	 * 
	 */
	public static class MagCard {
		public static final int DEVICE_IS_BUSY = 0x00;
		public static final int TRACK_DATA_ERR = 0x01;
		public static final int OTHER_ERROR = 0x02;
	}

	/**
	 * rf卡设备错误码
	 * 
	 * @author Administrator
	 * 
	 */
	public static class RFCard {
		public static final int ERROR_IS_BUSY = 0; // 设备繁忙
		public static final int ERROR_NOT_OPEN = 1; // 设备未打开
		public static final int ERROR_KEY_TYPE = -1;// 密钥类型错误
		public static final int ERROR_KEY_LENTH = -2;// 密钥长度错误
		public static final int ERROR_RESET_DATA = -3;// 卡片复位信息错误
		public static final int ERROR_UNKNOWN = -4;// 未知错误
		public static final int ERROR_AUTH_FAIL = -5;// 认证失败
		public static final int ERROR_READ_FAIL = -6;// 读数据失败
		public static final int ERROR_WRITE_FAIL = -7;// 写数据失败
		public static final int ERROR_SELECT_FAIL = -8;// 选择块失败
		public static final int ERROR_ADD_FAIL = -9;// 加值失败
		public static final int ERROR_REDUCE_FAIL = -10;// 减值失败
		public static final int ERROR_DELIVERY_FAIL = -11;// 传值失败
	}

	/**
	 * pinpad设备错误码
	 * 
	 * @author Administrator
	 * 
	 */
	public static class Pinpad {
		public static int ERROR_NODEV = -1;// 无此设备
		public static int ERROR_INPUTTIMES = -2;// pinblock输入次数错误
		public static int ERROR_KEYTYPE = -3;// pinblock类型错误
		public static int ERROR_TIMEOUT = -4;// 操作超时
		public static int ERROR_UNKNOWN = -5;// 未知错误
		public static int ERROR_MAC = -6;// 获取MAC错误
		public static int ERROR_ENCRYPT = -7;// 获取加密错误
	}

	/**
	 * 串口设备
	 * 
	 * @author Administrator
	 * 
	 */
	public static class SerialPort {
		public static int ERROR_NODEV = -1;// 无此设备
		public static int DEVICE_IS_BUSY = -2;// 设备忙
		public static int DEVICE_NOT_OPEN = -3;// 设备未打开
		public static int READ_ERROR = -4;// 读数据出错
	}

	/**
	 * 系统设备错误码
	 * 
	 * @author Administrator
	 * 
	 */
	public static class System {
		public static final int INSTALL_SUCCESS = 0x00;
		public static final int INSTALL_FAILED_ALREADY_EXISTS = -1;
		public static final int INSTALL_FAILED_INVALID_APK = -2;
		public static final int INSTALL_FAILED_INVALID_URI = -3;
		public static final int INSTALL_FAILED_INSUFFICIENT_STORAGE = -4;
		public static final int INSTALL_FAILED_DUPLICATE_PACKAGE = -5;
		public static final int INSTALL_FAILED_NO_SHARED_USER = -6;
		public static final int INSTALL_FAILED_UPDATE_INCOMPATIBLE = -7;
		public static final int INSTALL_FAILED_SHARED_USER_INCOMPATIBLE = -8;
		public static final int INSTALL_FAILED_MISSING_SHARED_LIBRARY = -9;
		public static final int INSTALL_FAILED_REPLACE_COULDNT_DELETE = -10;
		public static final int INSTALL_FAILED_DEXOPT = -11;
		public static final int INSTALL_FAILED_OLDER_SDK = -12;
		public static final int INSTALL_FAILED_CONFLICTING_PROVIDER = -13;
		public static final int INSTALL_FAILED_NEWER_SDK = -14;
		public static final int INSTALL_FAILED_TEST_ONLY = -15;
		public static final int INSTALL_FAILED_CPU_ABI_INCOMPATIBLE = -16;
		public static final int INSTALL_FAILED_MISSING_FEATURE = -17;
		public static final int INSTALL_FAILED_CONTAINER_ERROR = -18;
		public static final int INSTALL_FAILED_INVALID_INSTALL_LOCATION = -19;
		public static final int INSTALL_FAILED_MEDIA_UNAVAILABLE = -20;
		public static final int INSTALL_FAILED_VERIFICATION_TIMEOUT = -21;
		public static final int INSTALL_FAILED_VERIFICATION_FAILURE = -22;
		public static final int INSTALL_FAILED_PACKAGE_CHANGED = -23;
		public static final int INSTALL_FAILED_UID_CHANGED = -24;
		public static final int INSTALL_FAILED_VERSION_DOWNGRADE = -25;
		public static final int INSTALL_PARSE_FAILED_NOT_APK = -100;
		public static final int INSTALL_PARSE_FAILED_BAD_MANIFEST = -101;
		public static final int INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION = -102;
		public static final int INSTALL_PARSE_FAILED_NO_CERTIFICATES = -103;
		public static final int INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES = -104;
		public static final int INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING = -105;
		public static final int INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME = -106;
		public static final int INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID = -107;
		public static final int INSTALL_PARSE_FAILED_MANIFEST_MALFORMED = -108;
		public static final int INSTALL_PARSE_FAILED_MANIFEST_EMPTY = -109;
		public static final int INSTALL_FAILED_INTERNAL_ERROR = -110;
	}

	public static class CPUCard {
		public static final int OPEN_SUCCEED = 0;
		public static final int CARD_NOT_SUPPORTED = 1;
		public static final int OPEN_FAILED = 2;
	}

	public static class Camera {
		public static final int SCANCODE_HARDWARE_ERROR = 1; //硬件故障
		public static final int SCANCODE_UNKNOWN_CODE = 2; //解码失败
	}
}
