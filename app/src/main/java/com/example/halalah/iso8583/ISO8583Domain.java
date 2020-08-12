package com.example.halalah.iso8583;

import org.parceler.Parcel;

@Parcel
public class ISO8583Domain { /*8583每个域的定义*/
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
