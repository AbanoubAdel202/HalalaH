package com.example.halalah.storage;

import android.content.Context;
import android.util.Log;

import com.example.halalah.POSTransaction;
import com.example.halalah.PosApplication;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SaveLoadFile {


    public static int Savetransactions(POSTransaction[] postrxs)
    {
        Context context = null;
        FileOutputStream fos = null;
        ObjectOutputStream os = null;
        try {


             fos = context.openFileOutput("POS_Transactions", Context.MODE_PRIVATE);
             os = new ObjectOutputStream(fos);
            os.writeObject(postrxs);
            os.close();
            fos.close();
        }
        catch(FileNotFoundException e)
        {
            Log.d("SaveLoadFile", "Savetransaction: File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }



        return 0;
    }

    public static int Savetransaction(POSTransaction postrxs) {
        Context context = null;
        FileOutputStream fos = null;
        ObjectOutputStream os = null;
        try {


            fos = context.openFileOutput("POS_Transactions", Context.MODE_PRIVATE);
            os = new ObjectOutputStream(fos);
            os.writeObject(postrxs);
            os.close();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("SaveLoadFile", "Savetransaction: File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static POSTransaction LoadTransaction(int trxno)
    {   POSTransaction postrx=new POSTransaction();

        return postrx;
    }

    public static POSTransaction[] LoadAllTransaction()
    {   POSTransaction transactions[]=new POSTransaction [PosApplication.getApp().oGTerminal_Operation_Data.m_iTransactionCounter];

        return transactions;
    }
}
