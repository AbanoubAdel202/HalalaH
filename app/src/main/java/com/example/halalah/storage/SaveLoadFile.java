package com.example.halalah.storage;

import android.content.Context;
import android.util.Log;

import com.example.halalah.POSTransaction;
import com.example.halalah.PosApplication;
import com.example.halalah.Terminal_Operation_Data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveLoadFile {
    public Context context = null;

    public static int Savetransactions(POSTransaction[] postrxs)
    {
        Context context = null;
        FileOutputStream fos = null;
        ObjectOutputStream os = null;
        context=PosApplication.getApp().getApplicationContext();
        try {


             fos = context.openFileOutput("POSTRX", Context.MODE_PRIVATE);
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

    public static int SAVETeminal_operation_Data(Terminal_Operation_Data oGTerminal_operation_data) {
        FileOutputStream fos = null;
        ObjectOutputStream os = null;

        try {


            fos = PosApplication.getApp().getApplicationContext().openFileOutput("Terminal_operation_data", Context.MODE_PRIVATE);
            os = new ObjectOutputStream(fos);
            os.writeObject(oGTerminal_operation_data);
            os.close();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("SaveLoadFile", "Savetransaction: File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Terminal_Operation_Data loadTeminal_operation_Data() {
        Log.d("load terminal operation data", "loadTeminal_operation_Data: started");
        Terminal_Operation_Data terminal_operation_data = new Terminal_Operation_Data();

        ObjectInputStream isr = null;
        try {
            FileInputStream fIn = PosApplication.getApp().getApplicationContext().openFileInput("Terminal_operation_data");
            isr = new ObjectInputStream(fIn);
        } catch (FileNotFoundException e) {
            Log.d("SaveLoadFile", "Savetransaction: File not found");
            return terminal_operation_data;
        } catch (Exception e) {

            e.printStackTrace();
        }

        // Fill the Buffer with data from the file
        try {
            terminal_operation_data = (Terminal_Operation_Data) isr.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return terminal_operation_data;

    }

    public int Savetransaction(POSTransaction postrxs) {

        FileOutputStream fos = null;
        ObjectOutputStream os = null;

        try {


            fos = PosApplication.getApp().getApplicationContext().openFileOutput("POSTRX", Context.MODE_PRIVATE);
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

    public static int Savelasttransaction(POSTransaction Postrx){
        int iret = -1;
        FileOutputStream fos = null;
        ObjectOutputStream os = null;

        try {


            fos = PosApplication.getApp().getApplicationContext().openFileOutput("revtrx", Context.MODE_PRIVATE);
            os = new ObjectOutputStream(fos);
            os.writeObject(Postrx);
            os.close();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("SaveLoadFile", "Savetransaction: File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return iret;
    }
   public static POSTransaction Loadlasttransaction(){

        POSTransaction transaction = new POSTransaction();

        ObjectInputStream isr = null;
        try {
            FileInputStream fIn = PosApplication.getApp().getApplicationContext().openFileInput("revtrx");
            isr = new ObjectInputStream(fIn);
        } catch (FileNotFoundException e) {
            Log.d("SaveLoadFile", "Savetransaction: File not found");
        } catch (Exception e) {

        }

        // Fill the Buffer with data from the file
        try {
            transaction = (POSTransaction) isr.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return transaction;


    }
    public static POSTransaction[] LoadAllTransaction() {
        POSTransaction transactions[] = new POSTransaction[PosApplication.getApp().oGTerminal_Operation_Data.m_iTransactionCounter];
//Reading the file back...

        /* We have to use the openFileInput()-method
         * the ActivityContext provides.
         * Again for security reasons with
         * openFileInput(...) */
        ObjectInputStream isr = null;
        try {
            FileInputStream fIn = PosApplication.getApp().getApplicationContext().openFileInput("POSTRX");
            isr = new ObjectInputStream(fIn);
        } catch (FileNotFoundException e) {
            Log.d("SaveLoadFile", "Savetransaction: File not found");
        } catch (Exception e) {

        }

        // Fill the Buffer with data from the file
        try {
            transactions = (POSTransaction[]) isr.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return transactions;

    }
}
