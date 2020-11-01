package com.example.halalah;

import android.util.Log;
import com.example.halalah.connect.CommunicationsHandler;
import com.example.halalah.connect.SendReceiveListener;
import com.example.halalah.connect.TCPCommunicator;
import com.example.halalah.registration.view.ITransaction;
import com.example.halalah.secure.DUKPT_KEY;
import com.example.halalah.storage.CommunicationInfo;
import com.example.halalah.util.PacketProcessUtils;
import com.example.halalah.iso8583.BCDASCII;

/**
 * Header Terminal registeration
 * \Class Name: Terminal_Registeration
 * \Param  :
 * \Return :
 * \Pre    :
 * \Post   :
 * \Author	: mostafa hussiny
 * \DT		: 5/30/2020
 * \Des    : Container for Terminal registeration methods
 */
public class Terminal_Registeration implements SendReceiveListener {

    private static final String TAG = Utils.TAGPUBLIC + CommunicationsHandler.class.getSimpleName();
    public boolean bRegistered = false;
    byte[] mSendPacket = null;
    byte[] mRecePacket = null;

    int cont = -1;
    private ITransaction.View mView;
    private CommunicationsHandler mCommunicationsHandler;
    private TCPCommunicator tcpClient;


    /**
     * \Function Name: StartRegistrationProcess
     * \Param  : POSTransaction POSTrans
     * \Return : int
     * \Pre    :
     * \Post   :
     * \Author	: Moamen Ahmed
     * \DT		: 05/04/2020
     * \Des    : Perform Termial Registration process
     */

    public void StartRegistrationProcess(POSTransaction POSTrx, ITransaction.View transactionView) {


        /* Fill required values*/

        // Transmission Date and Time
        // Systems Trace Audit Number
        // Local Transaction Date & Time
        // Function Code -> 814
        // Private – Additional Data (DE 48)
        // Private – Terminal Status (DE62)

        mView = transactionView;

        mCommunicationsHandler = CommunicationsHandler.getInstance(new CommunicationInfo(PosApplication.getApp().getApplicationContext()));
        CommunicationsHandler communicationsHandler = CommunicationsHandler.getInstance(new CommunicationInfo(PosApplication.getApp().getApplicationContext()));
        communicationsHandler.connect();

        PosApplication.getApp().oGPosTransaction.Reset();
        String de48Str = POSTrx.ComposeTerminalRegistrationData(POSTrx.getTerminalRegistrationData());
        POSTrx.m_sHostData_DE48 = de48Str;
        PosApplication.getApp().oGPosTransaction.m_enmTrxType = POSTransaction.TranscationType.TERMINAL_REGISTRATION;
        POSTrx.ComposeNetworkMessage();

        mSendPacket = POSTrx.m_RequestISOMsg.isotostr();

        communicationsHandler.setSendReceiveListener(this);
        communicationsHandler.sendReceive(mSendPacket);
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


    public int ValidateHostRegistrationData(byte[] byte_recived) {
        //todo validate terminal Registeration data from DE
        // DE32 Acquirer Institution Identification Code
        // DE41 Card Acceptor Terminal Identification(Terminal ID)
        // DE42 Card Acceptor Identification Code (Merchant ID)
        // DE48 Private – Additional Data
        String sAcquirerInsCode = null;
        String sTerminalID= null;
        String sMerchantID= null;
        String sHostSignture= null;

        // Extract Host response
        sAcquirerInsCode = BCDASCII.asciiByteArray2String(PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(32));
        Log.d(TAG, "Acquirer Institution Identification Code ["+sAcquirerInsCode+"]");

        sTerminalID =BCDASCII.asciiByteArray2String(PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(41));
        Log.d(TAG, "Terminal ID ["+sTerminalID+"]");

        sMerchantID =BCDASCII.asciiByteArray2String(PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(42));
        Log.d(TAG, "Merchant ID ["+sMerchantID+"]");

        sHostSignture =BCDASCII.asciiByteArray2String(PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(48));
        Log.d(TAG, "Host Signature DE48  ["+sHostSignture+"]");

        if (sAcquirerInsCode == null ||
                sTerminalID == null||
        sMerchantID == null||
        sHostSignture == null) {

            Log.d(TAG, "!!! Invalid Host Signature Resonse !!!");
            return -1; // Failed
        }

        //todo validate Host Signature DE 48;

        PosApplication.getApp().oGTerminal_Operation_Data.sAcquirer_ID = sAcquirerInsCode;
        PosApplication.getApp().oGTerminal_Operation_Data.m_sTerminalID = sTerminalID;
        PosApplication.getApp().oGTerminal_Operation_Data.m_sMerchantID = sMerchantID;

        return 1; // Success
    }

    @Override
    public void showConnectionStatus(int connectionStatus) {
        if (mView != null) {
            mView.showConnectionStatus(connectionStatus);
        }
    }

    @Override
    public void onSuccess(byte[] receivedPacket) {
        String sActionCode;
        mCommunicationsHandler.closeConnection();
        mRecePacket = receivedPacket;
        cont = 0;

        // Parse host response
       POS_MAIN.Process_Rece_Packet( mRecePacket);

        // Get Trx Action code
        sActionCode=BCDASCII.asciiByteArray2String(PosApplication.getApp().oGPosTransaction.m_ResponseISOMsg.getDataElement(39));

        if( POS_MAIN.CheckHostActionCode(sActionCode) == true )
        {
            // Validate Host Registration response
            if (ValidateHostRegistrationData(mRecePacket) == 1) {

                PosApplication.getApp().oGTerminal_Operation_Data.m_sTRMSID= PosApplication.getApp().oGPosTransaction.getTerminalRegistrationData().getTrsmid();
                PosApplication.getApp().oGTerminal_Operation_Data.m_CurrentKSN = BCDASCII.hexStringToBytes(PosApplication.getApp().halaVendorid+PosApplication.getApp().oGTerminal_Operation_Data.m_sTRMSID+"00000000");
               // PosApplication.getApp().oGPOS_MAIN.Start_Transaction(PosApplication.getApp().oGPosTransaction, POSTransaction.TranscationType.TMS_FILE_DOWNLOAD,null);
               // DUKPT_KEY.InitilizeDUKPT(PosApplication.getApp().oGTerminal_Operation_Data.m_szBDK, BCDASCII.bytesToHexString(PosApplication.getApp().oGTerminal_Operation_Data.m_CurrentKSN));
                preConnect();
                PosApplication.getApp().oGPosTransaction.m_enmTrxType= POSTransaction.TranscationType.TMS_FILE_DOWNLOAD;
                PosApplication.getApp().oGPOS_MAIN.StartTMSDownload(false,mView);
                PosApplication.getApp().oGTerminal_Registeration.bRegistered = true;
                PosApplication.getApp().oGTerminal_Operation_Data.m_bregistered = true;

            } else
                onFailure(R.string.invalid_registration_data);
        }
        else
            onFailure(R.string.registration_error);
    }

    @Override
    public void onFailure(int communicationErrorReason) {
        mCommunicationsHandler.closeConnection();
        if (mView != null) {
            switch (communicationErrorReason) {
                case PacketProcessUtils.SOCKET_PROC_ERROR_REASON_SEND:
                    mView.showError(R.string.sending_error);
                    break;
                case PacketProcessUtils.SOCKET_PROC_ERROR_REASON_RECE:
                    mView.showError(R.string.receiving_error);
                    break;
                case PacketProcessUtils.SOCKET_PROC_ERROR_REASON_RECE_TIME_OUT:
                    mView.showError(R.string.timeout_error);
                    break;
                default:
                case PacketProcessUtils.SOCKET_PROC_ERROR_REASON_CONNE:
                    mView.showError(R.string.connection_error);
                    break;
            }

        }
    }
    private void preConnect() {
        // open socket to be ready to sending/receiving financial messages
      /*  CommunicationInfo communicationInfo = new CommunicationInfo(this);
        InputStream caInputStream = getResources().openRawResource(R.raw.bks);
        CommunicationsHandler.getInstance(communicationInfo, caInputStream).connect();*/

        tcpClient = TCPCommunicator.getInstance();
        tcpClient.init( PosApplication.getApp().oGTerminal_Operation_Data.Hostip, PosApplication.getApp().oGTerminal_Operation_Data.Hostport);
      //  TCPCommunicator.closeStreams();
    }
}
