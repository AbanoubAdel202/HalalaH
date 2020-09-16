package com.example.halalah;
import com.example.halalah.PosApplication;
import com.example.halalah.connect.CommunicationsHandler;
import com.example.halalah.connect.SendReceiveListener;
import com.example.halalah.packet.PackPacket;
import com.example.halalah.storage.CommunicationInfo;

/** Header Terminal registeration
 \Class Name: Terminal_Registeration
 \Param  :
 \Return :
 \Pre    :
 \Post   :
 \Author	: mostafa hussiny
 \DT		: 5/30/2020
 \Des    : Container for Terminal registeration methods
 */
public class Terminal_Registeration implements SendReceiveListener {


     public boolean  bRegistered = false;

    PackPacket mPackPacket = null;
    byte[] mSendPacket = null;
    byte[] mRecePacket = null;

    int cont=-1;



/**
	\Function Name: StartRegistrationProcess
	\Param  : POSTransaction POSTrans
	\Return : int
	\Pre    :
	\Post   :
	\Author	: Moamen Ahmed
	\DT		: 05/04/2020
	\Des    : Perform Termial Registration process
*/

    public int StartRegistrationProcess(POSTransaction POSTrx)
    {  int iStatus = -1;
        int iCounter = 0;


        /* Fill required values*/

        // Transmission Date and Time
        // Systems Trace Audit Number
        // Local Transaction Date & Time
        // Function Code -> 814
        // Private – Additional Data (DE 48)
        // Private – Terminal Status (DE62)

        POSTrx.ComposeTerminalRegistrationData();
        mSendPacket=POSTrx.m_RequestISOMsg.isotostr();
        do{


            CommunicationsHandler.getInstance(new CommunicationInfo(PosApplication.getApp().getApplicationContext())).connect();

            mSendPacket=mPackPacket.getSendPacket();
            CommunicationsHandler communicationsHandler = CommunicationsHandler.getInstance(new CommunicationInfo(PosApplication.getApp().getApplicationContext()));
            communicationsHandler.setSendReceiveListener(this);
            communicationsHandler.sendReceive(mSendPacket);
            while(cont==-1) {
                //TEST FUNCTIONALITY
            }

            iCounter++;

        }while( iStatus != 0 && iCounter < 3);

        if (iStatus != 0)
            return iStatus; // It might be any connection error or response error







            /*     Get Host Terminal Registration Data */
            // DE32 Acquirer Institution Identification Code
            // DE41 Card Acceptor Terminal Identification(Terminal ID)
            // DE42 Card Acceptor Identification Code (Merchant ID)
            // DE48 Private – Additional Data

            iStatus = ValidateHostRegistrationData(mRecePacket);


        return iStatus;
    }




    /**
        \Function Name: LoadTerminalRegistrationData
        \Param  :
        \Return :int
        \Pre    :
        \Post   :
        \Author	: Moamen Ahmed
        \DT		: 05/04/2020
        \Des    : Load Terminal Registration Data as
                    - SAMA Public Key
                    - SAMA Public Key Index
                    - Hala Private Keys
                    - Hala Public Key index
                    - Vendor ID
                    - Terminal Type
                    - TRMSID
                    - Key Issuer Number
    */
    public int LoadTerminalRegistrationData()
    {
        return 0;
    }

    /**
        \Function Name: PromptRegisterationSetting
        \Param  :
        \Return :int
        \Pre    :
        \Post   :
        \Author	: Moamen Ahmed
        \DT		: 05/04/2020
        \Des    : Get Terminal Registration Data & Communucation details for UL as
                    - SAMA Public Key
                    - SAMA Public Key Index
                    - Hala Private Keys
                    - Hala Public Key index
                    - Vendor ID
                    - Terminal Type
                    - TRMSID
                    - Key Issuer Number
                    - Connection Details (TPDU, IP , PORT, APN ,SSL ,...)
    */
    public int PromptRegisterationSetting()
    {
        //todo user interface for getting all terminal registeration required data for messaging
        return 0;
    }


    /*
        \Function Name: ValidatePKIFiles
        \Param  :int iType ,int iIndex
        \Return :int
        \Pre    : Files exist
        \Post   : Keys loaded and ready for Use
        \Author	: Moamen Ahmed
        \DT		: 05/04/2020
        \Des    : Validate PKI files for both Vendor or Hala
    */
    public int ValidatePKIFiles(int iType ,int iIndex)
    {
        return 0;
    }


/*
	\Function Name: ValidateHostRegistrationData
	\Param  :
	\Return : int
	\Pre    :
	\Post   :
	\Author	: Moamen Ahmed
	\DT		: 05/04/2020
	\Des    : Validate Host Terminal registration Data element 48
*/



    public int ValidateHostRegistrationData(byte[] byte_recived)
    {
        //todo validate terminal Registeration data from DE
        // DE32 Acquirer Institution Identification Code
        // DE41 Card Acceptor Terminal Identification(Terminal ID)
        // DE42 Card Acceptor Identification Code (Merchant ID)
        // DE48 Private – Additional Data
        return 0;
    }

    @Override
    public void showConnectionStatus(int connectionStatus) {

    }

    @Override
    public void onSuccess(byte[] receivedPacket) {

        mRecePacket=receivedPacket;
        cont=0;
        ValidateHostRegistrationData(mRecePacket);


    }

    @Override
    public void onFailure(int errReason) {
        cont=0;

    }
}
