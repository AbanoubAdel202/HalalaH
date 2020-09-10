package com.example.halalah;

import android.content.Context;
import android.util.Log;
import android.widget.Switch;

import com.example.halalah.storage.SaveLoadFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SAF_Info {


    public static void Load_TRX_from_Reversal() {


    }

    public static POSTransaction Load_trx_from_SAF() {

        POSTransaction SAFTRX = new POSTransaction();

        POSTransaction[] AllSAF =Load_from_SAF();

        SAFTRX=AllSAF[0];




        return SAFTRX;
    }

    enum DESAFtype{
        PARTIAL,
        FULL
    }

    int m_iMax_SAF_Depth;  //Max number of transaction
    int m_iMax_SAF_Cumulative_Amount;
    int       m_iSAFTrxNumber;              // Number of saved transactions in SAF file
    double    m_dSAFCumulativeAmount;       // Total amount of approved transaction (calculation based on reconciliation as debit++ , credit-- , reversal(refund ++) -- )
    int    DeSAF_partial_count=2;
    int    DeSAF_count;


    POSTransaction[] m_posTransactions_SAF;

    SAF_Info(){

        m_posTransactions_SAF= new POSTransaction[m_iMax_SAF_Depth];
        m_iMax_SAF_Cumulative_Amount=0;

        m_dSAFCumulativeAmount=0;
        m_iSAFTrxNumber=0;
    }
    public static POSTransaction BuildSAFOriginals(POSTransaction SAFTransaction, POSTransaction originalTransaction) {
        SAFTransaction=originalTransaction;
        SAFTransaction.m_enum_OrigTRxtype=originalTransaction.m_enmTrxType;
        SAFTransaction.m_sOrigAmount=originalTransaction.m_sTrxAmount;
        SAFTransaction.m_sOrigMTI=originalTransaction.m_sMTI;
        SAFTransaction.m_sOrigSTAN=originalTransaction.m_sSTAN;
        SAFTransaction.m_sOrigTrxDateTime=originalTransaction.m_sTrxDateTime;
        SAFTransaction.m_sOrigLocalTrxDateTime=originalTransaction.m_sLocalTrxDateTime;
        SAFTransaction.m_sOrigAquirerInsIDCode=originalTransaction.m_sAquirerInsIDCode;
        SAFTransaction.m_sOrigFWAquirerInsIDCode="00"; //based on moamen advice it should be 00
        SAFTransaction.m_sOrigRRNumber=originalTransaction.m_sRRNumber;
        SAFTransaction.m_sOrigLocalTrxDate=originalTransaction.m_sLocalTrxDateTime.substring(0,6);//check with moamen

        return SAFTransaction;

    }
    public static void SAVE_IN_SAF(POSTransaction oSAFPostrx)
    {
        Context context = null;
        FileOutputStream fos = null;
        ObjectOutputStream os = null;
        context=PosApplication.getApp().getApplicationContext();
        POSTransaction temp[]=null;
        POSTransaction SAFarray[]=null;
        if (PosApplication.getApp().oGTerminal_Operation_Data.saf_info.m_iSAFTrxNumber>0) {
            temp = SAF_Info.Load_from_SAF();
            SAFarray = new POSTransaction[temp.length + 1];
            System.arraycopy(temp, 0, SAFarray, 0, temp.length);
            SAFarray[temp.length] = oSAFPostrx;
        }
        else {
            SAFarray = new POSTransaction[1];
            SAFarray[0] = oSAFPostrx;
        }
        try {


            fos = context.openFileOutput("SAF", Context.MODE_PRIVATE);
            os = new ObjectOutputStream(fos);
            os.writeObject(SAFarray);

            os.close();
            fos.close();
        }
        catch(FileNotFoundException e)
        {
            Log.d("SaveLoadFile", "Savetransaction: File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }




    }





    public static void SAVE_IN_REV(POSTransaction oPostrax)
    {
        Context context = null;
        FileOutputStream fos = null;
        ObjectOutputStream os = null;
        context=PosApplication.getApp().getApplicationContext();
        POSTransaction temp[]=null;
        POSTransaction SAFarray[]=null;
        if (PosApplication.getApp().oGTerminal_Operation_Data.saf_info.m_iSAFTrxNumber>0) {
            temp = SAF_Info.Load_from_SAF();
            SAFarray = new POSTransaction[temp.length + 1];
            System.arraycopy(temp, 0, SAFarray, 0, temp.length);
            SAFarray[temp.length] = oPostrax;
        }
        else {
            SAFarray = new POSTransaction[1];
            SAFarray[0] = oPostrax;
        }
        try {


            fos = context.openFileOutput("REVERSAL", Context.MODE_PRIVATE);
            os = new ObjectOutputStream(fos);
            os.writeObject(SAFarray);

            os.close();
            fos.close();
        }
        catch(FileNotFoundException e)
        {
            Log.d("SaveLoadFile", "Savetransaction: File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    public static POSTransaction[] Load_from_SAF()
    {      POSTransaction SAFarray[] = new POSTransaction[PosApplication.getApp().oGTerminal_Operation_Data.saf_info.m_iSAFTrxNumber];

        ObjectInputStream isr = null;
        try {
            FileInputStream fIn = PosApplication.getApp().getApplicationContext().openFileInput("SAF");
            isr = new ObjectInputStream(fIn);
        } catch (FileNotFoundException e) {
            Log.d("SaveLoadFile", "Savetransaction: File not found");
        } catch (Exception e) {

        }

        // Fill the Buffer with data from the file
        try {
            SAFarray = (POSTransaction[]) isr.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return SAFarray;

    }




    public static POSTransaction Load_from_Reversal()
    {        POSTransaction RevTRX = new POSTransaction();

        ObjectInputStream isr = null;
        try {
            FileInputStream fIn = PosApplication.getApp().getApplicationContext().openFileInput("SAF");
            isr = new ObjectInputStream(fIn);
        } catch (FileNotFoundException e) {
            Log.d("SaveLoadFile", "Savetransaction: File not found");
        } catch (Exception e) {

        }

        // Fill the Buffer with data from the file
        try {
            RevTRX = (POSTransaction) isr.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return RevTRX;
    }

    /**
	\Function Name: CheckSAFLimits
	\Param  :
	\Return : boolean
	\Pre    :
	\Post   :
	\Author	: Moamen Ahmed
	\DT		: 13/07/2020
	\Des    : Check SAF cumulative limits for DESAFing processing ( Partail ) , TMS limits should be loaded for checking () , Specification document part b Page 4
*/

    public boolean CheckSAFLimits()
    {
        boolean bRetRes = false;

        // Rule #1 Default case

        // Rule #2 Check MAX Number SAF Depth
        if(m_iSAFTrxNumber >= m_iMax_SAF_Depth )
        {
            //Todo Log message

            bRetRes = true ;

            return bRetRes;
        }

        if (m_dSAFCumulativeAmount >= m_iMax_SAF_Cumulative_Amount)
        {
            //Todo Log message
            bRetRes = true  ;
            return bRetRes;
        }

        // Rule #3 Idle Time
	/*
		POS terminal is idle , Start  DeSAF processing
	*/




        //Todo Log message  not SAF Limits fired
        return bRetRes;
    }






}
