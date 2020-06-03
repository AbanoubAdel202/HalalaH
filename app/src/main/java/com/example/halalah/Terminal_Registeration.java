package com.example.halalah;
import com.example.halalah.PosApplication;

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
public class Terminal_Registeration {


     public boolean  bRegistered = false;







/*
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
    {  int iStatus = 0;
        int iCounter = 0;
        /* Fill required values*/

        // Transmission Date and Time
        // Systems Trace Audit Number
        // Local Transaction Date & Time
        // Function Code -> 814
        // Private – Additional Data (DE 48)
        // Private – Terminal Status (DE62)

        POSTrx.BuildISO8583Message(POSTransaction.TranscationType.TERMINAL_REGISTRATION);
        do{

            //iStatus = StartHostConnection(POSTrx); // Connect , Send/Receive
            //iCounter++;
        }while( iStatus != 0 && iCounter < 3);

        if (iStatus != 0)
            return iStatus; // It might be any connection error or response error

        //todo ValidateHostResponse
        //iStatus = ValidateHostResponse(POSTrx); // Approved Or Rejected

        if(iStatus != 0)
            return iStatus;
        else
        {
            /*     Get Host Terminal Registration Data */
            // DE32 Acquirer Institution Identification Code
            // DE41 Card Acceptor Terminal Identification(Terminal ID)
            // DE42 Card Acceptor Identification Code (Merchant ID)
            // DE48 Private – Additional Data

            iStatus = ValidateHostRegistrationData();

        }
        return iStatus;
    }




    /*
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

    /*
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



    public int ValidateHostRegistrationData()
    {
        return 0;
    }
}
