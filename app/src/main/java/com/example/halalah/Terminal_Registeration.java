package com.example.halalah;

import com.example.halalah.connect.CommunicationsHandler;
import com.example.halalah.connect.SendReceiveListener;
import com.example.halalah.registration.view.ITransaction;
import com.example.halalah.storage.CommunicationInfo;
import com.example.halalah.util.PacketProcessUtils;

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


    public boolean bRegistered = false;
    byte[] mSendPacket = null;
    byte[] mRecePacket = null;

    int cont = -1;
    private ITransaction.View mView;
    private CommunicationsHandler mCommunicationsHandler;


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

        this.mView = transactionView;

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
        return 0;
    }

    @Override
    public void showConnectionStatus(int connectionStatus) {
        if (mView != null) {
            mView.showConnectionStatus(connectionStatus);
        }
    }

    @Override
    public void onSuccess(byte[] receivedPacket) {
        mCommunicationsHandler.closeConnection();
        mRecePacket = receivedPacket;
        cont = 0;
        if (ValidateHostRegistrationData(mRecePacket) != 0) {

            PosApplication.getApp().oGTerminal_Registeration.bRegistered = true;
            PosApplication.getApp().oGPOS_MAIN.Start_Transaction(PosApplication.getApp().oGPosTransaction, POSTransaction.TranscationType.TMS_FILE_DOWNLOAD,null);
        } else {
            onFailure(R.string.registration_error);

        }

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
}
