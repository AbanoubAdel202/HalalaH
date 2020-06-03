package com.example.halalah.cloudpos.data;

/**
 * 密码键盘具名常量
 * 
 * @author Administrator
 * 
 */
public class PinpadConstant {
	/**
	 * 密码键盘类型
	 * @author Administrator
	 *
	 */
	public static class PinpadId {
		public static final int BUILTIN = 0x00;	//内置密码键盘
		public static final int EXTERNAL = 0x01;	//外接密码键盘
	}
	/**
	 * 密钥类型
	 * @author Administrator
	 *
	 */
	public static class PinType {
		/** 联机PIN */
		public static final int INLINE_TYPE = 0x00;
		/** 脱机PIN */
		public static final int OUTLINE_TYPE = 0x01;
	}
	/**
	 * 工作密钥类型
	 * @author Administrator
	 *
	 */
	public static class WKeyType {
		public static final int WKEY_TYPE_PIK = 0x01;
		public static final int WKEY_TYPE_TDK = 0x02;
		public static final int WKEY_TYPE_MAK = 0x03;
	}

	/**
	 * MAC算法类型
	 * @author Tianxiaobo
	 *
	 */
	public static class MacType {
		public static final int TYPE_X919 = 0;
		public static final int TYPE_CUP_ECB = 1;
	}
	/**
	 * 加密模式
	 * @author Tianxiaobo
	 *
	 */
	public static class EncType {
		public static final int TYPE_ECB = 0x00;
		public static final int TYPE_CBC = 0x01;
	}

	/**
	 * 加密模式
	 * @author Tianxiaobo
	 *
	 */
	public static class KeyboardMode {
		public static final int MODE_FIXED = 0x00;
		public static final int MODE_RANDOM = 0x01;
	}

}
