package com.example.halalah.secure;

import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.halalah.DeviceTopUsdkServiceManager;
import com.example.halalah.POSTransaction;
import com.example.halalah.PosApplication;
import com.example.halalah.R;
import com.example.halalah.util.HexUtil;
import com.topwise.cloudpos.aidl.pinpad.AidlPinpad;
import com.topwise.cloudpos.aidl.system.AidlSystem;
import com.topwise.cloudpos.data.PinpadConstant;

public class DUKPT_KEY {

    private static final String TAG = "DUKPT";

    private static AidlPinpad pinpad = null; // 密码键盘接口


/**
	\Class Name   : KSN
	\Param        :
	\Return       :
	\Pre          :
	\Post         :
	\Author	      : MoamenAhmed
	\DT		      : 04/06/2020
	\Des: Used to Store Terminal KSN Data :
                                           - KEY_SET_ID
										       .VendorID           [8 Bits]
											   .Key_Issuer_Number  [16 Bits]
										   - TRSMID                [32 Bits] + Padding 3 Bits
                                           - Transaction Counter   [21 Bits]
***/

    public class KSN{

        // KSN
        public  byte   KEY_SET_ID_Vendor_ID;        	            // 1 Byte
        public  byte[] KEY_SET_ID_Key_Issuer_Number=new byte[2]; 	// 2 Bytes
        public  byte[] TRSMID=new byte[3];  						// 3 Bytes
        public  byte   Padding; 						            // 3 Bits
        public  int    Transaction_Counter; 			            // 21 Bits

    KSN(){

    }



    }




/*
		As per SAMA Secuirty specification document 6.0 , DUKPT Terminal Key Processing / Derivation

		1- Drive IK (IPEK)
		   IPEK = TDE(KSN_KEY_SET_ID , TRSMID) under BDK
		2- Inject IPEK  into terminal
        3- Drive Transaction PIN and MAC keys


*/

    /*Terminal Start Up*/

    KSN     m_KSN=new KSN();
    String  m_BDK ;            // Load terminal BDK value using secure way
   static int     m_WorkKey = 0x01;  // Towpise Key index

    // KSN Descriptor
    public byte[] KSN_Descriptor= new byte[3];       //[3 bytes] should be Hex format


    DUKPT_KEY(){
        /**  Default Values **/

        // Set Hala Vendor ID

        m_KSN.KEY_SET_ID_Vendor_ID = 0x50; // Hala Vendor ID

        // Set Hala Key Issuer number
        m_KSN.KEY_SET_ID_Key_Issuer_Number[0] = 0x01;
        m_KSN.KEY_SET_ID_Key_Issuer_Number[1] = 0x02;
        m_KSN.KEY_SET_ID_Key_Issuer_Number[2] = 0x03;


        // Set TRSMID
        m_KSN.TRSMID[0] = 0x11;
        m_KSN.TRSMID[1] = 0x22;
        m_KSN.TRSMID[2] = 0x33;

        // Set Padding to Zero
        m_KSN.Padding = 0;

        // Set Transaction counter
        m_KSN.Transaction_Counter = 0;

        KSN_Descriptor[0] =0x36;
        KSN_Descriptor[1] =0x30;
        KSN_Descriptor[2] =0x39;

        /*******************************************/

        // Load and inject DUKPT
        InitilizeDUKPT(m_BDK, m_KSN.toString());   // todo check ksn to string is correct
    }

/*
														>>>>>>>>>>> NOTE <<<<<<<<<<<<<
Terminal application will save last generated KSN value and transaction counter , so it would be used when terminal is powered on
Load Last Terminal Data from Terminal Operation Data Table
*/




/********************************************
	\Function Name: InitilizeDUKPT
	\Param        : string szBDK , string szKSN
	\Return       : int
	\Pre          :
	\Post         :
	\Author	      : MoamenAhmed
	\DT		      : 05/06/2020
	\Des: Used to initilize DUKPT Module
**************************************************/


    public static boolean InitilizeDUKPT(String szBDK , String szKSN)
    {


        String IPEK ;
        boolean iRetRes = false;
        String strBuffer;


        Log.i(TAG,"InitilizeDUKPT() STARTED with szBDK [ "+szBDK+" ]and szKSN [ "+szKSN+" ]");
        PosApplication.getApp().getDeviceManager();
        pinpad = DeviceTopUsdkServiceManager.getInstance().getPinpadManager(0);
	/*
	// Drive IPEK
	// Arrange Encryption Data
	strBuffer= szKSN.KEY_SET_ID_Vendor_ID + szKSN.KEY_SET_ID_Key_Issuer_Number+szKSN.TRSMID ;

	//1- 3DES Encrption
	//             in       KEY   Out
	iRetRes = TDE(strBuffer,szBDK,IPEK);

	Log.i(TAG," TDE returned [ "+iRetRes+ " ]and IPEK [ "+IPEK+" ]");
	*/
        // 2- inject IPEK or BDK to kernel
        try {
            iRetRes = pinpad.loadDuKPTkey(PosApplication.DUKPT_BDK, m_WorkKey, HexUtil.hexStringToByte(szBDK.toUpperCase()), HexUtil.hexStringToByte(szKSN.toUpperCase()));
            //iRetRes = pinpad.loadDuKPTkey(DukptKeyType.DUKPT_IPEK, m_WorkKey ,HexUtil.hexStringToByte(IPEK),HexUtil.hexStringToByte(szKSN));

            Log.i(TAG, " loadDuKPTkey returned [ " + iRetRes + " ]");

            Log.i(TAG, " InitilizeDUKPT Ended with [ " + iRetRes + " ]");
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }

        return iRetRes;
    }

    /*
        \Function Name: GetUserPIN
        \Param        : string strTrxAmount , string strCardPAN
        \Return       : int
        \Pre          :
        \Post         :
        \Author	      : MoamenAhmed
        \DT		      : 07/06/2020
        \Des: Dislay PIN Pad and calculate User PIN Block
    */
    public int GetUserPIN(String strTrxAmount , String strCardPAN)
    {
        int    iRetRes = -1;
        Bundle bundle = new Bundle();
        byte   byteNewKSN;

        Log.i(TAG," GetUserPIN STarted Amount [ "+strTrxAmount+ " ] and Card PAN [ "+strCardPAN+" ]");

        // Set Key Info
        bundle.putInt("wkeyid", m_WorkKey);
        bundle.putInt("key_type", PosApplication.DUKPT_PEK);
        bundle.putByteArray("random", null);
        bundle.putInt("inputtimes", 1);
        bundle.putInt("minlength", 4);
        bundle.putInt("maxlength", 12);
        bundle.putString("pan", strCardPAN);
        bundle.putString("tips", strTrxAmount);
        bundle.putBoolean("is_lkl", false);


       /* try {
            //showMessage(getString(R.string.pinpad_get_ksn) + HexUtil.bcd2str(pinpad.getDUKPTKsn(mWorkKey, false)));

            byteNewKSN = pinpad.getDUKPTKsn(m_WorkKey, true)
            Log.i(TAG,"getDUKPTKsn with  KSN [ "+HexUtil.bcd2str(byteNewKSN)+" ]  and m_WorkKey ["+m_WorkKey+" ]");

            iRetRes = pinpad.getPin(bundle, new MyGetPinListener());

            Log.i(TAG,"getPin with Bundle input [ "+bundle.Tostring()+" ] Returned [ "+iRetRes+"]");

        } catch (RemoteException e) {

            e.printStackTrace();
        }

        Log.i(TAG," GetUserPIN Ended With [ "+iRetRes.tostring()+"]");*/

        return iRetRes;
    }


/************************
	\Function Name: CaluclateMACBlock
	\Param        : string strMACInputData
	\Return       : string
	\Pre          :
	\Post         :
	\Author	      : MoamenAhmed
	\DT		      : 07/06/2020
	\Des: Used to generate MAC block for  passing input data
**************************************/


    public static String CaluclateMACBlock(String strMACInputData)
    {

        int    iRetRes         = -1;
        Bundle macBundle    = new Bundle();
        byte[] byteMACBlock = new byte[8];
        String strMACBLOCK ;
        byte[] byteCurrentKSN;

        Log.i(TAG," CaluclateMACBlock STarted Input Data  [ "+strMACInputData+ " ]");

        macBundle.putInt("wkeyid", m_WorkKey);
        macBundle.putInt("key_type", PosApplication.DUKPT_MAK);
        macBundle.putByteArray("data",HexUtil.hexStringToByte(strMACInputData));
        macBundle.putByteArray("random", null);
        macBundle.putInt("type", 0x00);


        //showMessage(getString(R.string.pinpad_get_ksn) + HexUtil.bcd2str(pinpad.getDUKPTKsn(mWorkKey, true)));
        try {
            byteCurrentKSN = pinpad.getDUKPTKsn(m_WorkKey, false);
            Log.i(TAG,"getDUKPTKsn for MAC with KSN Value [ "+HexUtil.bcd2str(byteCurrentKSN)+" ] and m_WorkingKey  ["+m_WorkKey+" ]");

        }
            catch (RemoteException e) {
            e.printStackTrace();
        }
       try {
           iRetRes = pinpad.getMac(macBundle, byteMACBlock);
           // MAC block should be padded with FF after firt four bytes A8451D92FFFFFFFF
           Log.i(TAG,"getMac  Returned ["+iRetRes+" ]");
       }catch (RemoteException e) {
           e.printStackTrace();
       }





        if (iRetRes!=0) {

            Log.i(TAG,"getMac Error Code [ "+ R.string.Errorcode + iRetRes +" ]");

            strMACBLOCK = null;

            //showMessage(getResources().getString(R.string.pin_mac_error_code) + retCode);
        }
        else
        {
            //showMessage(getResources().getString(R.string.pin_mac_919_mac_success1) + HexUtil.bcd2str(mac));

            strMACBLOCK = HexUtil.bcd2str(byteMACBlock);
            Log.i(TAG,"getMac Succeed with return [ "+R.string.MAC_Success+"] and Value ["+strMACBLOCK+"]");
        }

        Log.i(TAG,"CaluclateMACBlock Ended with [ "+iRetRes+" ]");

        return strMACBLOCK;
    }

    public static String getKSN() {

        byte[] byteCurrentKSN ;
        boolean bNewksnflag=false;
        if(PosApplication.getApp().oGPosTransaction.m_enmTrxCVM== POSTransaction.CVM.NO_CVM||PosApplication.getApp().oGPosTransaction.m_enmTrxCVM== POSTransaction.CVM.SIGNATURE)
            bNewksnflag=true;

        try {
            byteCurrentKSN = pinpad.getDUKPTKsn(m_WorkKey, bNewksnflag);
            return byteCurrentKSN.toString();
        }catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    return null;

    }



/*



	Test 2
	-------------------------------------

	KSN       : 47FFF00111100000016D
	BDK       : 0123456789ABCDEF0123456789ABCDEF
	IK        : ACA08E773EA612867EF8D5F5B31AEF53
	IPEK      : 519BBC57BD3A2E57EFF598FCF1F07C0A
	PEK       : 519BBC57BD3A2EA8EFF598FCF1F07CF5
	Plain PIN : 1234
	PAN       : 5892068642097536
	PINBlock  : 2101B0F829898521
	MAC KEY   : 519BBC57BD3AD157EFF598FCF1F0830A
	MACK DAta : 31313030373233303037433132384332394130353538393230363836343230393735333630303030303030303030303030393939323030303033323531323032313331313031343847FFF00111100000016D36303980
	MACKBlock : A8451D92FFFFFFFF

 */

    //0C1EE600E533F21CA81277F0462DE471
    //0C1EE600E533F21CA81277F0462DE4710C1EE600E533F21C

    //47 FFF0 011110 0000016D
}
