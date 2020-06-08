package com.example.halalah.iso8583;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Switch;

import com.example.halalah.POS_MAIN;
import com.example.halalah.PosApplication;
import com.example.halalah.packet.PackUtils;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Locale;

/** Header ISO8583:1993 modified for mada
 \Class Name: ISO8583
 \Param  :
 \Return :
 \Pre    :
 \Post   :
 \Author	: Mostafa Hussiny
 \DT		: 4/00/2020
 \Des    : create Iso8583 Engine that create buffer for sending and recviving and allocating data fields.
 */

public class ISO8583 {
    private static final String TAG = ISO8583.class.getSimpleName();

    public final static int ISO8583_MAX_LENGTH = 128; /*Maximum number of fields in ISO858 package*/
    public final static int MAXBUFFERLEN = 2048; /*Define the longest buffer in the ISO_data structure*/
    public final static int MSGIDLEN = 4; /*Message type length MTI*/

    public final static byte FIX_LEN = 0; /*(LENgth fix Refers to the length of the field determined by the length in ISO_8583)*/
    public final static byte LLVAR_LEN	= 1; /*(LENgth 00~99)*/
    public final static byte LLLVAR_LEN	= 2; /*(LENgth 00~999)*/

    public final static byte L_BCD = 0; /*Left aligned BCD code*/
    public final static byte L_ASC = 1; /*Left aligned ASCII code*/
    public final static byte R_BCD = 2; /*Right aligned BCD code*/
    public final static byte R_ASC = 3; /*Right aligned ASCII*/
	public final static byte D_BIN = 4; /*ֱBCD*/

    public ISO8583Domain[] mISO8583Domain;

    private int mOffset;
    private byte[] mDataBuffer;
    private byte[] mMessageId;

    private class ISO8583Domain { /*8583每个域的定义*/
        public int mMaxLength; /* data element max length */
        public byte mType; /* bit0,bit1暂留，bit2:为0时左对齐,为1时右对齐，bit3:为0时BCD码,为1时ASC码，type:0,1,2,3三种*//*bit0, bit1 stay, bit2: left aligned when 0, right aligned when 1, bit3: BCD code when 0, ASC code when 1, type: 0, 1, 2, 3*/
		public byte mFlag; /* length field length: 0--FIX_LEN型 1--LLVAR_LEN型 2--LLLVAR_LEN型*/
		public String mDomainName; /* 域名*/

        public int mBitf; /*field map if 1 true  0 false*/
		public int mLength; /*field length*/
		public int mStartAddr; /*field data's start address*/

        public void setDomainProperty(int length, byte type, byte flag, String domainName) {
            this.mMaxLength = length;
            this.mType = type;
            this.mFlag = flag;
            this.mDomainName = domainName;
        }
    }

    public ISO8583() {
        mISO8583Domain = new ISO8583Domain[ISO8583_MAX_LENGTH];
        for (int i = 0; i < ISO8583_MAX_LENGTH; i++) {
            mISO8583Domain[i] = new ISO8583Domain();
        }

       // initCupISO8583Domain();
        initISO8583Domain();

        mOffset = 0;
        mDataBuffer = new byte[MAXBUFFERLEN];
        mMessageId  = new byte[MSGIDLEN];
    }



    /*=================================================================
    * Function ID :  isotostr
    * Input       :  byte[] src
    * Output      :  
    * Return	  :  success 0 fail -1
    * Description :  ISO583包解包成为一个数据BCD码串
    * Notice	  :  成功:返回该数据串,失败:返回null 
    *=================================================================*/
    public byte[] isotostr() {
        int dataOffset = 0;
        int bytenum=16;
        int bitnum = 8;
        String sbitmap;
        int n = 0; 
        byte[] data = new byte[MAXBUFFERLEN];    
       // BCDASCII.fromASCIIToBCD(mMessageId, 0, MSGIDLEN, data, 0, false);
        System.arraycopy(mMessageId, 0, data, 0, MSGIDLEN);
        if(Arrays.equals(mMessageId, PosApplication.MTI_Terminal_Reconciliation_Advice.getBytes())) {
            bytenum = 32;
        }
        dataOffset = MSGIDLEN + bytenum; //Point to the data field after the message type and bitmap   // todo: mostafa : we have to consider 16 byte and 32 byte mapping

        for (int i = 0; i < bytenum/2; i++) {
            byte bitmap = 0; //Represents 8 bitmap fields in a byte in a 64-bit bitmap
            int bitmask = 0x80;

            for (int j = 0; j < bitnum; j++, bitmask>>=1) {
                n = (i<<3) + j;
                if((i==0 & bitmask==0x80)& bytenum==32)
                {
                    mISO8583Domain[n].mBitf = 1;
                }
                if (mISO8583Domain[n].mBitf == 0) {//This field has no value
                    continue;
                }
                bitmap |= bitmask;
                int len = mISO8583Domain[n].mLength;
                String srclen   = String.valueOf(len);
                //byte[] arraylen = null;

                Log.i(TAG, "idx="+(n+1)+", "+mISO8583Domain[n].mDomainName+", len="+srclen );
                
                if (mISO8583Domain[n].mFlag == LLVAR_LEN) {

                    // error Temporary comment for F35 length
				    if (mISO8583Domain[n].mType == D_BIN) {
                        len = (len+1)/2;
				    }

                	String lenstr = String.format(Locale.ENGLISH,"%02d",len);

                   // arraylen = BCDASCII.fromASCIIToBCD(lenstr, 0, 2, false);	//rclen, 0, 2, false);
                    System.arraycopy(lenstr.getBytes(), 0, data, dataOffset, 2);
                    dataOffset += 2;
                } else if (mISO8583Domain[n].mFlag == LLLVAR_LEN) {
				    if (mISO8583Domain[n].mType == D_BIN) {
                        len = (len+1)/2;
				    }
                	String lenstr = String.format(Locale.ENGLISH,"%03d",len);
                   // arraylen = BCDASCII.fromASCIIToBCD(lenstr, 0, 4, false);   //srclen, 0, 4, false);
                    System.arraycopy(lenstr.getBytes(), 0, data, dataOffset, 3);
                    dataOffset += 3;
                }
                if (mISO8583Domain[n].mType == L_BCD) {
                    len = (len+1)/2;
                } else if (mISO8583Domain[n].mType == L_ASC) {

                } else if (mISO8583Domain[n].mType == R_BCD) {
                    len = (len+1)/2;
                } else if (mISO8583Domain[n].mType == R_ASC) {
                    
                }
                System.arraycopy(mDataBuffer, mISO8583Domain[n].mStartAddr, data, dataOffset, len);
                dataOffset += len;
            }


            data[i + 4] = bitmap;
        }
        /*if (mISO8583Domain[127].mBitf == 1 && bytenum == 16) {
            data[2 + 7] |= 0x01; //Set the last position in the bitmap to 1, that is, set the 64 field to 1.
            System.arraycopy(mDataBuffer, mISO8583Domain[127].mStartAddr, data, dataOffset, 8);
            dataOffset += 8;
        }*/
       /* if (bytenum == 32) {
            data[2] |= 0x80; //0x80 is 1000 0000 in binary, and the first position of the bitmap is 1, indicating that 128 fields are used
        }*/
        if (dataOffset != 0) {

            //mostafa 6/5/2020  here we are converting bitmap from BCD to ascii and check if it's 64 or 128 bitmap
            if(bytenum==32) {
                byte[] bBitmap = BCDASCII.fromBCDToASCII(data, 4, 32, false);
                System.arraycopy(bBitmap,0,data,4,32);
            }
            else
            {
                byte[] bBitmap = BCDASCII.fromBCDToASCII(data, 4, 16, false);
                System.arraycopy(bBitmap,0,data,4,16);
            }
            byte[] bOutdata = new byte[dataOffset];
            System.arraycopy(data, 0, bOutdata, 0, dataOffset);
            return bOutdata;
        }
        return null;
    }

    public int  SetMTI(String MTI){
        if(TextUtils.isEmpty(MTI) || MTI.length()!=4){
            return  -1;
        }
        SetDataElement(0, MTI.getBytes(), PackUtils.MSGTYPEID_Financial_Request.length());
        System.arraycopy(MTI.getBytes(),0,mMessageId,0,4);
        return 0;
    }

    public String   getMTI(){

        return new  String(mMessageId);
    }
    /*=================================================================
    * Function ID :  strtoiso
    * Input       :  byte[] src
    * Output      :  
    * Return	  :  success 0 fail -1
    * Description :  将按8583包格式的数据串装载到8583包中
    * Notice	  :  
    *=================================================================*/
    public int strtoiso(byte[] src) {
        int bytenum = 32;//The bitmap is 128-bit binary, 16 bytes.  (16 BCD , 32 ASCII)
        int bitnum = 8;
        int bitmask = 0x80;//0x80FOR128，1000 0000，Use bitmask shift to check whether each of the 8 bits in a byte is 1.
    ClearFields();//清除8583包中的数据。

        //BCDASCII.fromBCDToASCII(src, 0, mMessageId, 0, MSGIDLEN, false);
        System.arraycopy(src, 0, mMessageId, 0, MSGIDLEN);  //getting MTI from Src
        int srcLen = src.length;                                          // length of srcrdc
        byte[] bSrcData = new byte[srcLen - ((MSGIDLEN))];        //Remove the message type and bitmap data in src // Mostafa: it should also remove secondry bit map if exist
        //System.arraycopy(src, (MSGIDLEN) + bitnum, srcData, 0, srcData.length);  // removed due to change in Bitmap size
        byte[] bcdsrc = new byte[(src.length+1)/2];
        BCDASCII.fromASCIIToBCD(src,0,src.length,bcdsrc,0,false);
        byte[] varLen = new byte[10];//字串中表示可变长度的长度值，如长度为104，则BCD码为 01 04，2个字节表示
        int n = 0;
        int len = 0;//每个域的数据长度
        int srcDataOffset = 0;
        int offset = 0;
        String sSrcData=null;
        String lenSrc;
        StringBuilder sp= new StringBuilder();
        for (int i = 0; i < (bytenum/2); i++) {//Bitmap 64 bits, 8 bytes, each byte 8 bits in order to check whether it is 1

            bitmask = 0x80;

            for (int j = 0; j < bitnum; j++, bitmask>>=1) {//bitmask = 0x80，10000000，Each time it shifts to the right one bit, it means to judge whether the next bit in this byte is 1.
                /*if (i == 0 && bitmask == 0x80) {//The first bit of the first byte represents whether it is a 64 field or a 128 field，
                    continue;
                }*/
                
               if (i==0 && ((bcdsrc[i+2] & bitmask)==0x80))    //mostafa 4/5/2020:added to be dynamically bitmap 8 or 16 checking first bit in bitmap if 0 or 1
               {
                   System.arraycopy(src, (MSGIDLEN) + bytenum, bSrcData, 0, src.length-((MSGIDLEN) + bytenum));

                   continue;
               }
               else if (i==0 && ((bcdsrc[i+2] & bitmask)==0x00)&&j==0)
               {
                   bytenum=16;   // mostafa :(8 byte BCD , 16 byte ascii)
                   System.arraycopy(src, (MSGIDLEN) + bytenum, bSrcData, 0, src.length-((MSGIDLEN) + bytenum));

                   continue;
               }

               if ((bcdsrc[i+2] & bitmask)==0) {//From the 2nd place，i=0，j=1，bitmask=0x40 for 01000000，Determine the second bit in the bitmap
                    continue;
                }
                n = (i<<3) + j;//for i=1，It means starting from the ninth domain，then 8+j，

                if (mISO8583Domain[n].mFlag == LLVAR_LEN) {
                   // String lenSrc = BCDASCII.fromBCDToASCIIString(srcData, srcDataOffset, 2, false);  //mostafa:4/5/2020 removed as LLVAR_LEN is ascii not BCD
                    // lenSrc = sSrcData.substring(srcDataOffset,srcDataOffset+2);

                    lenSrc=sp.append((char)bSrcData[srcDataOffset]).append((char)bSrcData[srcDataOffset+1]).toString();
                    sp.setLength(0);

                    Log.i(TAG, "LLVAR_LEN ascii_len=" + lenSrc);
                    len = Integer.parseInt(lenSrc, 10); // .valueOf(lenSrc);
                    srcDataOffset += 2;                                             // changed to 2 by mostafa
                } else if (mISO8583Domain[n].mFlag == LLLVAR_LEN) {
                   // String lenSrc = BCDASCII.fromBCDToASCIIString(srcData, srcDataOffset, 4, false);
                    // lenSrc = sSrcData.substring(srcDataOffset,srcDataOffset+3);

                    lenSrc=sp.append((char)bSrcData[srcDataOffset]).append((char)bSrcData[srcDataOffset+1]).append((char)bSrcData[srcDataOffset+2]).toString();
                    sp.setLength(0);
                    Log.i(TAG, "LLLVAR_LEN ascii_len=" + lenSrc);
					len = Integer.parseInt(lenSrc, 10); // .valueOf(lenSrc);
                    srcDataOffset += 3;                                             // changed to 3 by mostafa
                } else if (mISO8583Domain[n].mFlag == FIX_LEN) {
                    len = mISO8583Domain[n].mMaxLength;
                }
                mISO8583Domain[n].mLength = len;        /*保存该域的长度*/             
                mISO8583Domain[n].mStartAddr = offset;  /*该域值在mDataBuffer中的起始位置*/
                if (len + offset >= MAXBUFFERLEN) {
                    return -1;
                }
				// for debug
				byte[] buf = new byte[len + 1];
                if (mISO8583Domain[n].mType == L_BCD) {
                    len = (len+1)/2;
                    System.arraycopy(bSrcData, srcDataOffset, mDataBuffer, offset, len);
					// buf=BCDASCII.fromBCDToASCII(mDataBuffer, offset, len,
					// false);
					System.arraycopy(bSrcData, srcDataOffset, buf, 0, len);
				} else if (mISO8583Domain[n].mType == L_ASC || mISO8583Domain[n].mType == D_BIN) {
					System.arraycopy(bSrcData, srcDataOffset, mDataBuffer,
							offset, len);
					System.arraycopy(bSrcData, srcDataOffset, buf, 0, len);
                } else if (mISO8583Domain[n].mType == R_BCD) {
                    len = (len+1)/2;
                    System.arraycopy(bSrcData, srcDataOffset, mDataBuffer, offset, len);
					// buf=BCDASCII.fromBCDToASCII(mDataBuffer, offset, len,
					// false);
					System.arraycopy(bSrcData, srcDataOffset, buf, 0, len);
                } else if (mISO8583Domain[n].mType == R_ASC) {
                    System.arraycopy(bSrcData, srcDataOffset, mDataBuffer, offset, len);
					System.arraycopy(bSrcData, srcDataOffset, buf, 0, len);
				}
				// if (mISO8583Domain[n].mType == R_ASC ||
				// mISO8583Domain[n].mType == L_ASC)
				// Log.i(TAG,
				// "idx="+(n+1)+", "+mISO8583Domain[n].mDomainName+", Len="+len+",[ASCII] "+new
				// String(buf));
				// else
                Log.i(TAG, "idx=" + (n + 1) + ", " + mISO8583Domain[n].mDomainName + ", Len=" + len + ",[BCD] " + BCDASCII.bytesToHexString(buf));

				mISO8583Domain[n].mBitf = 1;
				offset += len;
				srcDataOffset += len;
			}
		}
		mOffset = offset;
		return 0;
	}

    /*=================================================================
    * Function ID :  setbit
    * Input       :  int n,byte[] str,int len
    * Return	  :  success 0 fail -1
    * Description :  将第n域加载到8583包(mDataBuffer)中.
    * Notice	  :  n->第几个域 str->需要打包的字符串 len->字符串的长度
    *=================================================================*/
    public int  SetDataElement(int n, byte[] src, int len) {
        int  i=0, l=0;
		byte[] pt  = null;//new byte[MAXBUFFERLEN];
		byte[] tmp = new byte[MAXBUFFERLEN];

        Log.i(TAG, "comein "+"setBit, n: " + n + ",len: " + len + ",src: " + BCDASCII.bytesToHexString(src) + ", ascii(" + new String(src) + ")");

        if (len == 0 || len > MAXBUFFERLEN) {
            return 0; //str为空或超长,不需要打包
        }
        if (n == 0) {
            System.arraycopy(src, 0, mMessageId, 0, MSGIDLEN);
            return 0;
        }      
		if (n <= 1 || n > ISO8583_MAX_LENGTH) {//第1域为位图域,不能用
			return -1;	
		}
		n--;//域从1开始，数组从0开始

        Log.i(TAG, "setBit, mMaxLength: " + mISO8583Domain[n].mMaxLength+" n= "+n);
		if( len > mISO8583Domain[n].mMaxLength ) {//最大长度不能超过8583包规定的长度
			len = mISO8583Domain[n].mMaxLength;
		}
        
        l = len;
		if( mISO8583Domain[n].mFlag == FIX_LEN ) {//该域为固定长度
			len = mISO8583Domain[n].mMaxLength;
		}
        //如果固定长度为8，实际传入只有6，那么需要补2位，len >= l.

        Log.i(TAG, "setBit, l: " + l + ",len: " + len);
        mISO8583Domain[n].mBitf = 1;            /*置该域为有数据 */  
        mISO8583Domain[n].mLength = len;        /*保存该域的长度*/
        mISO8583Domain[n].mStartAddr = mOffset;  /*该域值在mDataBuffer中的起始位置*/
        if ((mOffset + len) >=  MAXBUFFERLEN) {//mDataBuffer空间已满
            return -1;
        }
        if (mISO8583Domain[n].mType == L_BCD) {
            System.arraycopy(src, 0, tmp, 0, l);
            Arrays.fill(tmp, l, len, (byte)' ');
            pt = BCDASCII.fromASCIIToBCD(tmp, 0, len, false);
            System.arraycopy(pt, 0, mDataBuffer, mOffset, (len+1)/2);
            mOffset += (len+1)/2;
        } else if (mISO8583Domain[n].mType == L_ASC) {
            System.arraycopy(src, 0, tmp, 0, l);
            Arrays.fill(tmp, l, len, (byte)' ');
            System.arraycopy(tmp, 0, mDataBuffer, mOffset, len);
            mOffset += len;
        } else if (mISO8583Domain[n].mType == R_BCD) {
            Arrays.fill(tmp, 0, len-l, (byte)'0');
            System.arraycopy(src, 0, tmp, len-l, l);
            pt = BCDASCII.fromASCIIToBCD(tmp, 0, len, true);
            System.arraycopy(pt, 0, mDataBuffer, mOffset, (len+1)/2);
            mOffset += (len+1)/2;
        } else if (mISO8583Domain[n].mType == R_ASC) {
            Arrays.fill(tmp, 0, len-l, (byte)' ');
            System.arraycopy(src, 0, tmp, len-l, l);
            System.arraycopy(tmp, 0, mDataBuffer, mOffset, len);
            mOffset += len;
		} else if (mISO8583Domain[n].mType == D_BIN) {
            Log.i(TAG, "  l = " + l);
			System.arraycopy(src, 0, tmp, 0, l);
			Arrays.fill(tmp, l, len, (byte) ' ');
			pt = BCDASCII.fromASCIIToBCD(tmp, 0, len, false);
			System.arraycopy(pt, 0, mDataBuffer, mOffset, (len + 1) / 2);
			mOffset += (len + 1) / 2;
        }
        return 0;
    }

    /*=================================================================
    * Function ID :  getbit
    * Input       :  int n,Number of fields
    * Return	  :  成功返回这个域的字符串，失败返回空。
    * Description :  从8583包(mDataBuffer)中取出第n域的值。
    * Notice	  :  n-> Number of fields
    *=================================================================*/
    public byte[] getDataElement(int n) {
        // Log.i(TAG, "getBit, n: " + n);
        byte[] domainValue = null;
        if (n == 0) {
            domainValue = new byte[MSGIDLEN];
            System.arraycopy(mMessageId, 0, domainValue, 0, MSGIDLEN);
            Log.i(TAG, "getBit, n=0," + new String(domainValue));
            return domainValue;            
        }
        if (n <= 1 || n > ISO8583_MAX_LENGTH) {//第1域为位图域,不能用
            Log.i(TAG, "getBit, n is not normal.");
			return null;	
		}
		n--;//Field starts at 1, array starts at 0
        if (mISO8583Domain[n].mBitf == 0) {
            Log.i(TAG, "getBit, n domainValue is null.");
            return null;
        }
        int len = mISO8583Domain[n].mLength;
        int startAddr = mISO8583Domain[n].mStartAddr;
        byte[] data   = null;
        if (mISO8583Domain[n].mType == L_BCD) {
            data = new byte[(len+1)/2];
            System.arraycopy(mDataBuffer, startAddr, data, 0, (len+1)/2);
            domainValue = BCDASCII.fromBCDToASCII(data, 0, len, false);
        } else if (mISO8583Domain[n].mType == L_ASC || mISO8583Domain[n].mType == D_BIN) {
            data = new byte[len];
            System.arraycopy(mDataBuffer, startAddr, data, 0, len);
            domainValue = data;
        } else if (mISO8583Domain[n].mType == R_BCD) {
            data = new byte[(len+1)/2];
            System.arraycopy(mDataBuffer, startAddr, data, 0, (len+1)/2);
            domainValue = BCDASCII.fromBCDToASCII(data, 0, len, true);           
        } else if (mISO8583Domain[n].mType == R_ASC) {
            data = new byte[len];
            System.arraycopy(mDataBuffer, startAddr, data, 0, len);
            domainValue = data;
        }
		Log.i(TAG, "getBit, n=" + (n + 1) + ", " + BCDASCII.bytesToHexString(domainValue));
        return domainValue;
    }

    /**
     * \Function Name: Getbitmap
     * \Param  : void
     * \Return : Error codes
     * \Author : Mostafa Hussiny
     * \DT		: 5/21/2020
     * \Des    : for getting primary bitmap to be used in MAC calculation
     */
    public byte[] Getbitmap()
    {
        int dataOffset = 0;
        int bytenum=16;
        int bitnum = 8;
        String sbitmap;
        int n = 0;
        byte[] data = new byte[MAXBUFFERLEN];

        for (int i = 0; i < bytenum/2; i++) {
            byte bitmap = 0; //Represents 8 bitmap fields in a byte in a 64-bit bitmap
            int bitmask = 0x80;
            for (int j = 0; j < bitnum; j++, bitmask>>=1) {
                n = (i<<3) + j;
                if((i==0 & bitmask==0x80)& bytenum==32)
                {
                    mISO8583Domain[n].mBitf = 1;
                }
                if (mISO8583Domain[n].mBitf == 0) {//This field has no value
                    continue;
                }
                bitmap |= bitmask;


            }



        }
                byte[] bBitmap = BCDASCII.fromBCDToASCII(data, 4, 16, false);



            return bBitmap;
        }





    public void ClearFields() {
        for (int i = 0; i < ISO8583_MAX_LENGTH; i++) {
            mISO8583Domain[i].mBitf = 0; 
            mISO8583Domain[i].mLength = 0;
            mISO8583Domain[i].mStartAddr = 0;
        }
        mOffset = 0;
        Arrays.fill(mDataBuffer, (byte)0);
    }

    /** for reference
     * \Function Name: GetDE22
     * \Param  : void
     * \Return : Error codes
     * \Author : Mostafa Hussiny
     * \DT		: 5/28/2020
     * \Des    : for getting primary bitmap to be used in MAC calculation
     */

    //Position 1 – Card data input capability (Indicates the primary means of getting the information on the card into the terminal)
    enum Card_data_input_capability{PAN_Entry_Mode_Unknown ,
        Manual_no_terminal  ,
        Magnetic_stripe_read ,
        Bar_Code    ,
        OCR     ,
        ICC ,
        Key_Entered ,
        Contactless }
    //Position 2 – Cardholder authentication capability (Indicates the primary means of verifying the cardholder at this terminal.)
    enum Cardholder_authentication_capability{No_electronic_authentication,
        PIN,
        Electronic_signature_analysis,
        Biometrics,
        Biographic,
        Electronic_authentication_inoperative,
        other,

    }
    //Position 3 – Card capture capability (Indicates whether or not the terminal has the ability to capture a card) which always 0

    // Position 4 – Operating environment  (Indicates if the terminal is attended by the card acceptor at its location)
    enum Operating_environment {
        On_premises_of_card_acceptor_attended,
        On_premises_of_card_acceptor_unattended,
        Off_premises_of_card_acceptor_attended,
        Off_premises_of_card_acceptor_unattended
    }
    //Position 5 – Cardholder present
    enum Cardholder_present{
        Cardholder_present,
        Cardholder_not_present_unspecified
    }
    //Position 6 – Card present
    enum Card_present{
        Card_not_present,
        Card_present
    }

    // Position 7 – Card data input mode (Indicates method used to input the information from the card to the terminal)
    enum Card_data_input_mode {
        Unspecified,
        Manual_no_terminal,
        Magnetic_stripe_read,
        Bar_Code,
        OCR,
        ICC,
        Key_entered,
        Contactless
    }
    //Position 8 – Cardholder authentication method (Indicates the method for verifying the cardholder identity)
    enum Cardholder_authentication_method {
        Not_authenticated,
        PIN,
        Electronic_signature_analysis,
        Biometrics,
        Biographic,
        Manual_signature_analysis,
        Other_manual_verifications
    }
    //Position 9 – Cardholder authentication entity  (Indicates the entity verifying the cardholder identity)
    enum Cardholder_authentication_entity{
        Not_authenticated,
        ICC,
        CAD,
        Authorising_agent,
        By_merchant,
        other
    }
    //Position 10 – Card data output capability (Indicates the ability of the terminal to update the card)
    enum Card_data_output_capability
    {
        Unknown,
        None,
        Magnetic_Stripe_write,
        ICC
    }
    //Position 11 – Terminal output capability (Indicates the ability of the terminal to print/display messages)
    enum Terminal_output_capability{
        Unknown,
        None,
        Printing,
        Display,
        Printing_and_display
    }
    //Position 12 – PIN capture capability (Indicates the length of PIN which the terminal is capable of capturing)
    enum PIN_capture_capability{
        No_PIN_capture_capability,
        Device_PIN_capture_capability_unknown,
        Four_characters,
        Five_characters,
        Six_characters,
        Seven_characters,
        Eight_characters,
        Nine_characters,
        Ten_characters,
        Eleven_characters,
        Twelve_characters
    }
    public byte[] GetDE22(Card_data_input_capability cdic,Cardholder_authentication_capability cac,Operating_environment oe,Cardholder_present Cardholder_p,Card_present Card_p,Card_data_input_mode cdim,Cardholder_authentication_method cam)
    {
        byte[] bDE22=new byte[12];

        switch(cdic){

            case PAN_Entry_Mode_Unknown:
                bDE22[0]=0;
                break;
            case Manual_no_terminal:
                bDE22[0]=1;
                break;
            case Magnetic_stripe_read:
                bDE22[0]=2;
                break;
            case Bar_Code:
                bDE22[0]=3;
                break;
            case OCR:
                bDE22[0]=4;
                break;
            case ICC:
                bDE22[0]=5;
                break;
            case Key_Entered:
                bDE22[0]=6;
                break;
            case Contactless:
                bDE22[0]=7;
                break;

        }

        switch(cac){
            case No_electronic_authentication:
                bDE22[1]=0;
                break;
            case PIN:
                bDE22[1]=1;
                break;
            case Electronic_signature_analysis:
                bDE22[1]=2;
                break;
            case Biometrics:
                bDE22[1]=3;
                break;
            case Biographic:
                bDE22[1]=4;
                break;
            case Electronic_authentication_inoperative:
                bDE22[1]=5;
                break;
            case other:
                bDE22[1]=6;
                break;
        }

        bDE22[2]=0;   // for position 3 always 0

        switch (oe)
        {
            case On_premises_of_card_acceptor_attended:
                bDE22[3]=1;
                break;
            case On_premises_of_card_acceptor_unattended:
                bDE22[3]=2;
                break;
            case Off_premises_of_card_acceptor_attended:
                bDE22[3]=3;
                break;
            case Off_premises_of_card_acceptor_unattended:
                bDE22[3]=4;
                break;
        }
        switch (Cardholder_p)
        {
            case Cardholder_present:
                bDE22[4]=0;
                break;
            case Cardholder_not_present_unspecified:
                bDE22[4]=1;
                break;
        }
        switch (Card_p)
        {
            case Card_not_present:
                bDE22[5]=0;
                break;
            case Card_present:
                bDE22[5]=1;
                break;
        }
        switch (cdim)
        {
            case Unspecified:
                bDE22[6]=0;
                break;
            case Manual_no_terminal:
                bDE22[6]=1;
                break;
            case  Magnetic_stripe_read:
                bDE22[6]=2;
                break;
            case Bar_Code:
                bDE22[6]=3;
                break;
            case OCR:
                bDE22[6]=4;
                break;
            case ICC:
                bDE22[6]=5;
                break;
            case Key_entered:
                bDE22[6]=6;
                break;
            case Contactless:
                bDE22[6]=7;
                break;
        }

        switch (cam)
        {
            case Not_authenticated:
                bDE22[7]=0;
                break;
            case PIN:
                bDE22[7]=1;
                break;
            case Electronic_signature_analysis:
                bDE22[7]=2;
                break;
            case Biometrics:
                bDE22[7]=3;
                break;
            case Biographic:
                bDE22[7]=4;
                break;
            case Manual_signature_analysis:
                bDE22[7]=5;
                break;
            case Other_manual_verifications:
                bDE22[7]=6;
                break;

        }





        return bDE22;
    }
    /**
     * \Function Name: GetDE24
     * \Param  : void
     * \Return : Error codes
     * \Author : Mostafa Hussiny
     * \DT		: 5/28/2020
     * \Des    : for getting primary bitmap to be used in MAC calculation
     */

    enum  Function_Code{
        Original_authorisation_amount_accurate,
        Original_authorisation_amount_estimated,
        Original_authorisation_Bill_Payment,
        Original_authorization_Fee_Payment,
        Notification_of_pre_authorisation_initial_completion,
        Notification_of_a_pre_authorisation_expiry_extension,
        Original_financial_request_advice,
        Original_financial_request_advice_Bill_Payment,
        Original_financial_request_advice_Fee_Payment,
        Previously_approved_authorisation_amount_same,
        Previously_approved_authorisation_amount_differs,
        Previously_approved_authorisation_Bill_Payment,
        Previously_approved_authorisation_Fee_Payment,
        Replace_fields_within_record_partial_download,
        Replace_entire_record_partial_download,
        Replace_file_full_download,
        Full_reversal_transaction_did_not_complete_as_approved,
        Full_reversal_transaction_did_not_complete_as_approved_Bill_Payment,
        Full_reversal_transaction_did_not_complete_as_approved_Fee_Payment,
        Terminal_reconciliation,
        Force_reconciliation,
        Unable_to_parse_message,
        MAC_Error,
        Device_authentication
        }
        public String GetDE24FunctionCode(Function_Code fc){
                String sde24=null;
                switch (fc) {
                    case Original_authorisation_amount_accurate:
                        sde24="100";
                        break;
                    case Original_authorisation_amount_estimated:
                        sde24="101";
                        break;
                    case Original_authorisation_Bill_Payment:
                        sde24="160";
                        break;
                    case Original_authorization_Fee_Payment:
                        sde24="161";
                        break;
                    case Notification_of_pre_authorisation_initial_completion:
                        sde24="182";
                        break;
                    case Notification_of_a_pre_authorisation_expiry_extension:
                        sde24="183";
                        break;
                    case Original_financial_request_advice:
                        sde24="200";
                        break;
                    case Original_financial_request_advice_Bill_Payment:
                        sde24="260";
                        break;
                    case Original_financial_request_advice_Fee_Payment:
                        sde24="261";
                        break;
                    case Previously_approved_authorisation_amount_same:
                        sde24="201";
                        break;
                    case Previously_approved_authorisation_amount_differs:
                        sde24="202";
                        break;
                    case Previously_approved_authorisation_Bill_Payment:
                        sde24="262";
                        break;
                    case Previously_approved_authorisation_Fee_Payment:
                        sde24="263";
                        break;
                    case Replace_fields_within_record_partial_download:
                        sde24="302";
                        break;
                    case Replace_entire_record_partial_download:
                        sde24="304";
                        break;
                    case Replace_file_full_download:
                        sde24="306";
                        break;
                    case Full_reversal_transaction_did_not_complete_as_approved:
                        sde24="400";
                        break;
                    case Full_reversal_transaction_did_not_complete_as_approved_Bill_Payment:
                        sde24="420";
                        break;
                    case Full_reversal_transaction_did_not_complete_as_approved_Fee_Payment:
                        sde24="421";
                        break;
                    case Terminal_reconciliation:
                        sde24="570";
                        break;
                    case Force_reconciliation:
                        sde24="571";
                        break;
                    case Unable_to_parse_message:
                        sde24="650";
                        break;
                    case MAC_Error:
                        sde24="691";
                        break;
                    case Device_authentication:
                        sde24="814";
                        break;
                }
                            return sde24;
        }
    private void initISO8583Domain() {
        if (mISO8583Domain == null) {
            return;
        }

        mISO8583Domain[0 ].setDomainProperty(  16, L_ASC, FIX_LEN,       		"Secondary Bitmap");			//1
        mISO8583Domain[1 ].setDomainProperty( 19, R_ASC, LLVAR_LEN,     		"Primary Account Number (PAN)");		//2 + L_ASC->L_BCD
        mISO8583Domain[2 ].setDomainProperty(  6, L_ASC, FIX_LEN,       		"Processing Code");		//3 +
        mISO8583Domain[3 ].setDomainProperty( 12, R_ASC, FIX_LEN,       		"Transaction Amount");	    //4 + Henry 4->12
        mISO8583Domain[4 ].setDomainProperty(  8, L_BCD, FIX_LEN,       		"unused");	    //5
        mISO8583Domain[5 ].setDomainProperty(  8, L_BCD, FIX_LEN,       		"unused");	    //6
        mISO8583Domain[6 ].setDomainProperty( 10, L_ASC, FIX_LEN,     		" Transmission Date and Time");      	//7
        mISO8583Domain[7 ].setDomainProperty(  10, L_ASC, FIX_LEN,       	"unused");	    //8
        mISO8583Domain[8 ].setDomainProperty(  8, R_BCD, FIX_LEN,       		"unused");      	//9
        mISO8583Domain[9 ].setDomainProperty(  8, R_BCD, FIX_LEN,       		"unused");      	//10
        mISO8583Domain[10].setDomainProperty(  6, R_ASC, FIX_LEN,       		"STAN");	    //11 +
        mISO8583Domain[11].setDomainProperty(  12, L_ASC, FIX_LEN,       	"Date and Time, Local Transaction");	//12 +
        mISO8583Domain[12].setDomainProperty(  4, R_BCD, FIX_LEN,       		"本地交易日期");	//13 + Henry 8->4 ->hw: 8
        mISO8583Domain[13].setDomainProperty(  4, L_ASC, FIX_LEN,       		" Date, Expiration");		//14 + Henry 8->4
        mISO8583Domain[14].setDomainProperty(  4, R_BCD, FIX_LEN,       		"结算日期");	    //15 +
        mISO8583Domain[15].setDomainProperty(  4, R_BCD, FIX_LEN,       		"未使用");	    //16
        mISO8583Domain[16].setDomainProperty(  4, R_BCD, FIX_LEN,       		"未使用");	    //17
        mISO8583Domain[17].setDomainProperty(  4, R_BCD, FIX_LEN,       		"未使用");	    //18
        mISO8583Domain[18].setDomainProperty(  4, R_BCD, FIX_LEN,       		"未使用");	    //19
        mISO8583Domain[19].setDomainProperty(  4, R_BCD, FIX_LEN,       		"未使用");	    //20
        mISO8583Domain[20].setDomainProperty(  4, R_BCD, FIX_LEN,       		"未使用");	    //21
        mISO8583Domain[21].setDomainProperty(  12, L_ASC, FIX_LEN,       	"Point of Service Data Code"); 	//22 + Henry 4->3 R_BCD->L_BCD
        mISO8583Domain[22].setDomainProperty(  3, L_ASC, FIX_LEN,       		"Card Sequence Number ");	    //23 + Henry 4->3
        mISO8583Domain[23].setDomainProperty(  3, L_ASC, FIX_LEN,       		"Function Code ");	    //24
        mISO8583Domain[24].setDomainProperty(  4, L_ASC, FIX_LEN,       		"Message Reason Code");	//25 +
        mISO8583Domain[25].setDomainProperty(  4, L_ASC, FIX_LEN,       		" Card Acceptor Business Code");	//26 +
        mISO8583Domain[26].setDomainProperty(  99, L_BCD, LLVAR_LEN,			"未使用");	    //27
        mISO8583Domain[27].setDomainProperty(  6, R_ASC, FIX_LEN,			" Reconciliation Date ");	    //28
        mISO8583Domain[28].setDomainProperty(  99, L_BCD, LLVAR_LEN,			"未使用");	    //29
        mISO8583Domain[29].setDomainProperty(  12, L_ASC, FIX_LEN,			"Original Amount ");	    //30
        mISO8583Domain[30].setDomainProperty(  99, L_BCD, LLVAR_LEN,			"未使用");	    //31
        mISO8583Domain[31].setDomainProperty(  11, L_ASC, LLVAR_LEN,    		"Acquirer Institution Identification Code ");	//32 +
        mISO8583Domain[32].setDomainProperty(  99, L_BCD, LLVAR_LEN,	   		"未使用");	    //33
        mISO8583Domain[33].setDomainProperty( 999, L_BCD, LLLVAR_LEN,   		"未使用");	    //34
        mISO8583Domain[34].setDomainProperty( 37, D_BIN, LLVAR_LEN,    		"Track-2 Data ");	    //35 +
        mISO8583Domain[35].setDomainProperty(112,  D_BIN, LLLVAR_LEN,   		"三磁道数据");	    //36 +
        mISO8583Domain[36].setDomainProperty( 12,  L_ASC, FIX_LEN,      		" Retrieval Reference Number ");		//37 +  Henry 16->12
        mISO8583Domain[37].setDomainProperty(  6,  L_ASC, FIX_LEN,      		" Approval Code ");		//38 +
        mISO8583Domain[38].setDomainProperty(  3,  L_ASC, FIX_LEN,      		"Action Code");		//39 +  Henry 4->2
        mISO8583Domain[39].setDomainProperty( 16,  L_ASC, FIX_LEN,			"终端序列号");	    //40 +  Hw:
        mISO8583Domain[40].setDomainProperty(  16,  R_ASC, FIX_LEN,      	"Card Acceptor Terminal Identification  ");	//41 +
        mISO8583Domain[41].setDomainProperty( 15,  L_ASC, FIX_LEN,      		"Card Acceptor Identification Code ");	//42 +
        mISO8583Domain[42].setDomainProperty( 99,  L_BCD, LLVAR_LEN,			"未使用");		//43
        mISO8583Domain[43].setDomainProperty( 99,  L_ASC, LLVAR_LEN,    		" Additional Response Data ");	//44 +
        mISO8583Domain[44].setDomainProperty( 99,  L_BCD, LLVAR_LEN,			"未使用");	    //45
        mISO8583Domain[45].setDomainProperty(999, R_ASC, LLLVAR_LEN,			"未使用");	    //46
        mISO8583Domain[46].setDomainProperty(999, L_ASC, LLLVAR_LEN,			" Private - Card Scheme Sponsor ID & Additional Scheme Data ");	    //47
        mISO8583Domain[47].setDomainProperty(999, R_BCD, LLLVAR_LEN,    		" Private – Additional Data ");	//48 +
        mISO8583Domain[48].setDomainProperty(  3, R_ASC, FIX_LEN,       		"Currency Code, Transaction ");	//49 +
        mISO8583Domain[49].setDomainProperty(  3, R_ASC, FIX_LEN,			" Currency Code, Reconciliation ");		//50
        mISO8583Domain[50].setDomainProperty(  3, R_BCD, FIX_LEN,           	"未使用");		//51
        mISO8583Domain[51].setDomainProperty(  8, L_BCD, FIX_LEN,       		"Personal Identification Number(PIN)");	//52 +
        mISO8583Domain[52].setDomainProperty( 48, L_ASC, LLVAR_LEN,       		"Security Related Control Information");		//53 +
        mISO8583Domain[53].setDomainProperty( 120, R_ASC, LLLVAR_LEN,    		"Additional Amounts ");	    //54 +
        mISO8583Domain[54].setDomainProperty(255, L_ASC, LLLVAR_LEN,    		" ICC Related Data ");	    //55
        mISO8583Domain[55].setDomainProperty(58, L_ASC, LLVAR_LEN,       	"Original Data Elements ");	    //56
        mISO8583Domain[56].setDomainProperty(999, R_BCD, LLLVAR_LEN,       	"未使用");	    //57
        mISO8583Domain[57].setDomainProperty(100, R_BCD, LLLVAR_LEN,    		"PBOC电子钱包标准交易信息");	    //58
        mISO8583Domain[58].setDomainProperty(999, L_ASC, LLLVAR_LEN,       	"Transport Data ");	    //59
        mISO8583Domain[59].setDomainProperty(999, L_BCD, LLLVAR_LEN,    		"自定义域");	    //60 +  Henry R_ASC->R_BCD
        mISO8583Domain[60].setDomainProperty(999, L_BCD, LLLVAR_LEN,    		"自定义域");	    //61 +  Henry L_ASC->R_BCD LLVAR_LEN->LLLVAR_LEN; Hw
        mISO8583Domain[61].setDomainProperty(999, L_ASC, LLLVAR_LEN,    		" Private – Terminal Status ");	    //62 +  Henry R_BCD->L_ASC
        mISO8583Domain[62].setDomainProperty(999, L_ASC, LLLVAR_LEN,    		"自定义域");	    //63 +	Hw
        mISO8583Domain[63].setDomainProperty(  8, L_ASC, FIX_LEN,       		"Message Authentication Code (MAC) ");	//64 +
        mISO8583Domain[64].setDomainProperty(  8, L_BCD, FIX_LEN,           	"未使用");      	//65
        mISO8583Domain[65].setDomainProperty(  1, R_ASC, FIX_LEN,           	"未使用");      	//66
        mISO8583Domain[66].setDomainProperty(  2, R_ASC, FIX_LEN,           	"未使用");      	//67
        mISO8583Domain[67].setDomainProperty(  3, R_ASC, FIX_LEN,           	"未使用");      	//68
        mISO8583Domain[68].setDomainProperty(  3, R_ASC, FIX_LEN,           	"未使用");      	//69
        mISO8583Domain[69].setDomainProperty(  3, R_ASC, FIX_LEN,       		"管理信息码");      //70
        mISO8583Domain[70].setDomainProperty(  4, R_ASC, FIX_LEN,           	"未使用");      	//71
        mISO8583Domain[71].setDomainProperty(  999, L_ASC, LLLVAR_LEN,       "Data Record ");      	//72
        mISO8583Domain[72].setDomainProperty(  6, R_ASC, FIX_LEN,           	"未使用");      	//73
        mISO8583Domain[73].setDomainProperty( 10, R_ASC, FIX_LEN,       		"贷记交易笔数");    //74
        mISO8583Domain[74].setDomainProperty( 10, R_ASC, FIX_LEN,       		"贷记自动冲正交易笔数");      //75
        mISO8583Domain[75].setDomainProperty( 10, R_ASC, FIX_LEN,       		"借记交易笔数");    		//76
        mISO8583Domain[76].setDomainProperty( 10, R_ASC, FIX_LEN,       		"借记自动冲正交易笔数");      //77
        mISO8583Domain[77].setDomainProperty( 10, R_ASC, FIX_LEN,       		"转帐交易笔数");    		//78
        mISO8583Domain[78].setDomainProperty( 10, R_ASC, FIX_LEN,       		"转帐自动冲正交易笔数");      //79
        mISO8583Domain[79].setDomainProperty( 10, R_ASC, FIX_LEN,       		"查询交易笔数");    //80
        mISO8583Domain[80].setDomainProperty( 10, R_ASC, FIX_LEN,       		"授权交易笔数");    //81
        mISO8583Domain[81].setDomainProperty( 12, R_ASC, FIX_LEN,           	"未使用");      	//82
        mISO8583Domain[82].setDomainProperty( 12, R_ASC, FIX_LEN,       		"贷记交易费金额");  	//83
        mISO8583Domain[83].setDomainProperty( 12, R_ASC, FIX_LEN,           	"未使用");     	//84
        mISO8583Domain[84].setDomainProperty( 12, R_ASC, FIX_LEN,       		"借记交易费金额");  	//85
        mISO8583Domain[85].setDomainProperty( 16, R_ASC, FIX_LEN,       		"贷记交易金额");    //86
        mISO8583Domain[86].setDomainProperty( 16, R_ASC, FIX_LEN,       		"贷记自动冲正金额");	//87
        mISO8583Domain[87].setDomainProperty( 16, R_ASC, FIX_LEN,       		"借记交易金额");    //88
        mISO8583Domain[88].setDomainProperty( 16, R_ASC, FIX_LEN,       		"借记自动冲正交易金额");      	//89
        mISO8583Domain[89].setDomainProperty( 42, R_ASC, FIX_LEN,       		"原交易的数据元素");			//90
        mISO8583Domain[90 ].setDomainProperty(  1, R_BCD, FIX_LEN,       	"文件修改编码");    //91
        mISO8583Domain[91 ].setDomainProperty(  2, R_BCD, FIX_LEN,           "未使用");      	//92
        mISO8583Domain[92 ].setDomainProperty(  5, R_BCD, FIX_LEN,           "未使用");      	//93
        mISO8583Domain[93 ].setDomainProperty(  7, R_BCD, FIX_LEN,       	"服务指示码");     //94
        mISO8583Domain[94 ].setDomainProperty( 42, R_BCD, FIX_LEN,       	"代替金额");		//95
        mISO8583Domain[95 ].setDomainProperty(  8, R_BCD, FIX_LEN,           "未使用");      	//96
        mISO8583Domain[96 ].setDomainProperty( 16, R_BCD, FIX_LEN,       	"净结算金额");     //97
        mISO8583Domain[97 ].setDomainProperty( 25, R_BCD, FIX_LEN,           "未使用"); 		//98
        mISO8583Domain[98 ].setDomainProperty( 11, R_ASC, LLVAR_LEN,     	"结算机构码");		//99
        mISO8583Domain[99 ].setDomainProperty( 11, R_ASC, LLVAR_LEN,     	"接收机构码");		//100
        mISO8583Domain[100].setDomainProperty( 17, R_BCD, LLVAR_LEN,     	"文件名"); 		//101
        mISO8583Domain[101].setDomainProperty( 28, R_BCD, LLVAR_LEN,     	"帐号1");  		//102
        mISO8583Domain[102].setDomainProperty( 28, R_BCD, LLVAR_LEN,     	"帐号2");   		//103
        mISO8583Domain[103].setDomainProperty(10,  R_BCD, LLLVAR_LEN,		"");       	//104
        mISO8583Domain[104].setDomainProperty(999, R_BCD, LLLVAR_LEN,		"");       	//105
        mISO8583Domain[105].setDomainProperty(999, R_BCD, LLLVAR_LEN,		"");       	//106
        mISO8583Domain[106].setDomainProperty(999, R_BCD, LLLVAR_LEN,		"");       	//107
        mISO8583Domain[107].setDomainProperty(999, R_BCD, LLLVAR_LEN,		"");       	//108
        mISO8583Domain[108].setDomainProperty(999, R_BCD, LLLVAR_LEN,		"");       	//109
        mISO8583Domain[109].setDomainProperty(999, R_BCD, LLLVAR_LEN,		"");       	//110
        mISO8583Domain[110].setDomainProperty(999, R_BCD, LLLVAR_LEN,		"");       	//111
        mISO8583Domain[111].setDomainProperty(999, R_BCD, LLLVAR_LEN,		"");       	//112
        mISO8583Domain[112].setDomainProperty(999, R_BCD, LLLVAR_LEN,		"");       	//113
        mISO8583Domain[113].setDomainProperty(999, R_BCD, LLLVAR_LEN,		"");       	//114
        mISO8583Domain[114].setDomainProperty(999, R_BCD, LLLVAR_LEN,		"");       	//115
        mISO8583Domain[115].setDomainProperty(999, R_BCD, LLLVAR_LEN,		"");       	//116
        mISO8583Domain[116].setDomainProperty(999, R_BCD, LLLVAR_LEN,		"");       	//117
        mISO8583Domain[117].setDomainProperty(999, R_BCD, LLLVAR_LEN,		"");       	//118
        mISO8583Domain[118].setDomainProperty(999, R_BCD, LLLVAR_LEN,		"");       	//119
        mISO8583Domain[119].setDomainProperty(999, R_BCD, LLLVAR_LEN,		"");       	//120
        mISO8583Domain[120].setDomainProperty(999, R_BCD, LLLVAR_LEN,		"");       	//121
        mISO8583Domain[121].setDomainProperty(999, R_BCD, LLLVAR_LEN,		"");       	//122
        mISO8583Domain[122].setDomainProperty(16, R_BCD, FIX_LEN,    	"新密码数据");     //123
        mISO8583Domain[123].setDomainProperty(999, L_ASC, LLLVAR_LEN,       	"Private – mada POS Terminal Reconciliation Totals");       	//124
        mISO8583Domain[124].setDomainProperty(999, R_BCD, LLLVAR_LEN,       	"未使用");       	//125
        mISO8583Domain[125].setDomainProperty(999, R_BCD, LLLVAR_LEN,       	"未使用");       	//126
        mISO8583Domain[126].setDomainProperty(999, R_BCD, LLLVAR_LEN,       	"未使用");       	//127
        mISO8583Domain[127].setDomainProperty(  8, R_ASC, FIX_LEN,       	"Message Authentication Code (MAC) ");		//128
	}



}
