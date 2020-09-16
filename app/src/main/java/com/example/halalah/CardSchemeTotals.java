package com.example.halalah;


/**
	Description 	 :

			This file descripe how terminal totals are saved and processed for all transaction and card schemes

			1- Reconciliation   >> Terminal connect to host and all totals are cleared.
			2- Running Totals   >> Get a printout of the terminal's cumulative totals since the last running totals transaction was performed , Reset Running totals buffer
			3- Snapshot Totals  >> Get The snapshot totals transaction is run to get a printout of the terminal's cumulative totals since the last running totals transaction was performed , DO NOT Reset any buffer


	Date        	 : 25/06/2020
	Author      	 : Moamen Ahmed
	Version     	 : 1.0.0
	Last Update Date : 29/6/2020

	Initial Draft

	Note:
	     - Need to be tested
	     - Reveral with Original 1100 totals need to be handled
		 - Auth count need testing


*/


import java.io.Serializable;

/**
	\class Name: CardSchemeTotals
	\Param  :
	\Return :
	\Pre    :
	\Post   :
	\Author	: Moamen Ahmed
	\DT		: 05/04/2020
	\Des    : Card Scheme totals buffer
*/





public class CardSchemeTotals implements Serializable {
        // Card Scheme ID
        public String m_szCardSchmID;        /* Card scheme ID*/
        public String m_szCardSchemeAcqID;        /* Card Scheme Acquirer ID*/

        // Trx Totals
        public  long m_lDebitCount;        /* Debit Count*/
        public  double m_dDebitAmount;        /* Debit Amount*/
        public  long m_lCreditCount;        /* Credit Count*/
        public  double m_dCreditAmount;        /* Credit Amount*/
        public  double m_dCashBackAmount;        /* Cash Back Amount*/
        public  double m_dCashAdvanceAmount;        /* Cash Advance Amount*/
        public  long m_lAuthorisationCount;        /* Authorisation Count*/

        // Card Scheme Trx totals Details
        public   long m_lOfflinePurchaseCount;        /* Off Purchase Count*/
        public   double m_dOfflinePurchaseAmount;        /* Off Purchase Amount*/
        public   long m_lOnlinePurchaseCount;            /* Online Purchase Count*/
        public   double m_dOnlinePurchaseAmount;        /* Online Purchase Amount*/
        public   long m_lReversalCount;                /* Reversal Count*/
        public   double m_dReversalAmount;                /* Reversal Amount*/
        public   long m_lRefundCount;                    /* Refund Count*/
        public   double m_dRefundAmount;                /* Refund Amount*/
        public   long m_lPurcAdvCompCount;            /* Purchase Advice Complition Count*/
        public   double m_dPurAdvCompAmount;            /* Purchase Advice Ccomplition Amount*/
        public   long m_lPurWCashBackCount;            /* Purchase With cash back Count*/
        public   double m_dPurWCashBackAmount;            /* Purchase With cash back Amount*/

        public CardSchemeTotals()
        {
                m_szCardSchmID="ID";
                m_szCardSchemeAcqID="ACID";
        }
        /*
            \Function Name: UpdateTerminalTotals
            \Param  : POSTransaction POSTrx
            \Return : int
            \Pre    :
            \Post   :
            \Author	: Moamen Ahmed
            \DT		: 25/06/2020
            \Des    : PUpdate Terminal Totals
        */
        public static int UpdateTerminalTotals(POSTransaction POSTrx) {
                int iRetRes = -1;
                int iSchemeIndex = -1;

                // Get Transaction Card Scheme total buffer;
                for (int i = 0; i < PosApplication.getApp().oGTerminal_Operation_Data.g_NumberOfCardSchemes; i++) {
                        if (POSTrx.m_card_scheme.m_sCard_Scheme_ID.equals(PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[i].m_szCardSchmID)) {
                                iSchemeIndex = i;
                                break;
                        }


                        if (iSchemeIndex == -1) {
                                //Todo Log message "Can not get Card Scheme"

                                return iRetRes;
                        }

                        // Start Update Totals as per Specification document rules
                        // Todo Log Message with Matched Trx Card scheme ID


                        // 1- Update Transaction totals
                        switch (POSTrx.m_enmTrxType) {

                                case PURCHASE:
                                case PURCHASE_WITH_NAQD:
                                case PURCHASE_ADVICE:
                                case CASH_ADVANCE:
                                case REFUND: {
                                        // Update debit Amount and count
                                        if (POSTrx.m_sProcessCode.startsWith("00") ||
                                                POSTrx.m_sProcessCode.startsWith("01") ||
                                                POSTrx.m_sProcessCode.startsWith("09")) {
                                                // Todo Log Message Current value and updated debit amount  and count values
                                                // Debit Amount
                                                PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_dDebitAmount += Double.parseDouble(POSTrx.m_sTrxAmount);

                                                // Debit Count
                                                PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_lDebitCount++;
                                        }
                                        // Todo Log Message Current value and updated debit amount  and count values

                                        // Update Credit amount and count
                                        if (POSTrx.m_sProcessCode.startsWith("20")) {
                                                // Debit Amount
                                                PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_dCreditAmount += Double.parseDouble(POSTrx.m_sTrxAmount);

                                                // Debit Count
                                                PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_lCreditCount++;
                                        }


                                        // Update Cash Advance amount (no Cash Advance count)
                                        if (POSTrx.m_sProcessCode.startsWith("01")) {
                                                // Todo Log Message Current value and updated debit amount  and count values
                                                PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_dCashAdvanceAmount += Double.parseDouble(POSTrx.m_sTrxAmount);
                                        }

                                        // Cash Back Amount (no count for Cash back)
                                        if (POSTrx.m_sProcessCode.startsWith("09")) {
                                                // Todo Log Message Current value and updated debit amount  and count values
                                                PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_dCashBackAmount += Double.parseDouble(POSTrx.m_sAdditionalAmount);
                                        }


                                }
                                break;
                                case AUTHORISATION_ADVICE:
                                case AUTHORISATION:
                                case AUTHORISATION_EXTENSION:
                                case AUTHORISATION_VOID: {
                                        // Update Auth count
                                        if (POSTrx.m_sProcessCode.startsWith("00") ||
                                                POSTrx.m_sProcessCode.startsWith("09") ||
                                                POSTrx.m_sProcessCode.startsWith("20") ||
                                                POSTrx.m_sProcessCode.startsWith("90") ||
                                                POSTrx.m_sProcessCode.startsWith("22")) {

                                                // Todo Log Message Current value and updated debit amount  and count values

                                                // Auth count
                                                PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_lAuthorisationCount++;
                                        }


                                }
                                break;

                                case REVERSAL: {
                                        // Update Credit amount,check against orignal MTI
                                        if ((POSTrx.m_sProcessCode.startsWith("00") || POSTrx.m_sProcessCode.startsWith("01") || POSTrx.m_sProcessCode.startsWith("09")) &&
                                                (POSTrx.m_sOrigMTI.equals("1200") || POSTrx.m_sOrigMTI.equals("1220"))
                                        ) {
                                                // Todo Log Message Current value and updated debit amount  and count values

                                                // Debit Amount
                                                PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_dCreditAmount += Double.parseDouble(POSTrx.m_sTrxAmount);

                                                // Debit Count
                                                PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_lCreditCount++;
                                        }


                                        // Update Debit amount, check against orignal MTI
                                        if (POSTrx.m_sProcessCode.equals("20") &&
                                                (POSTrx.m_sOrigMTI.equals("1200") || POSTrx.m_sOrigMTI.equals("1220"))
                                        ) {
                                                // Todo Log Message Current value and updated debit amount  and count values
                                                // Debit Amount
                                                PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_dDebitAmount += Double.parseDouble(POSTrx.m_sTrxAmount);

                                                // Debit Count
                                                PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_lDebitCount++;

                                        }

                                        // Substract Additional amount (Cash Advance or Cash Back) note#5
                                        if (POSTrx.m_sProcessCode.equals("01") &&
                                                (POSTrx.m_sOrigMTI.equals("1200") || POSTrx.m_sOrigMTI.equals("1220"))
                                        ) {
                                                // Debit Amount
                                                PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_dCashAdvanceAmount -= Double.parseDouble(POSTrx.m_sTrxAmount);

                                                // Todo Log Message Current value and updated debit amount  and count values
                                        }

                                        // Substract Additional amount (Cash Advance or Cash Back) note#5
                                        if (POSTrx.m_sProcessCode.equals("09") &&
                                                (POSTrx.m_sOrigMTI.equals("1200") || POSTrx.m_sOrigMTI.equals("1220"))
                                        ) {

                                                // Debit Amount
                                                PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_dCashBackAmount -= Double.parseDouble(POSTrx.m_sAdditionalAmount);

                                                // Todo Log Message Current value and updated debit amount  and count values
                                        }


                                }
                                break;
                                default: {
                                        // Todo Log error message
                                        return iRetRes;
                                }

                        }

                        // 2- Update Transaction details totals
                        switch (POSTrx.m_enmTrxType) {
                                case PURCHASE: {
                                        // Updating Card Scheme  Trx details
                                        if (POSTrx.m_bIsOfflineTrx == true) /*New is offline paramter should be added to POSTransaction class*/ {
                                                PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_dOfflinePurchaseAmount += Double.parseDouble(POSTrx.m_sTrxAmount);
                                                PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_lOfflinePurchaseCount++;

                                        } else {
                                                PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_dOnlinePurchaseAmount += Double.parseDouble(POSTrx.m_sTrxAmount);
                                                PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_lOnlinePurchaseCount++;
                                        }
                                }
                                break;
                                case REFUND: {
                                        // Log Current and Update Values
                                        PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_dRefundAmount += Double.parseDouble(POSTrx.m_sTrxAmount);
                                        PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_lRefundCount++;

                                }
                                break;
                                case PURCHASE_WITH_NAQD: {
                                        // Log Current and Update Values
                                        PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_dPurWCashBackAmount += Double.parseDouble(POSTrx.m_sAdditionalAmount);
                                        PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_lPurWCashBackCount++;

                                }
                                break;
                                case REVERSAL: {
                                        // Log Current and Update Values
                                        PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_dReversalAmount += Double.parseDouble(POSTrx.m_sTrxAmount);
                                        PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_lReversalCount++;

                                }
                                break;
                                case PURCHASE_ADVICE: {
                                        // Log Current and Update Values
                                        PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_dPurAdvCompAmount += Double.parseDouble(POSTrx.m_sTrxAmount);
                                        PosApplication.getApp().oGTerminal_Operation_Data.g_TerminalTotals[iSchemeIndex].m_lPurcAdvCompCount++;
                                }
                                break;
                        }

                        // Save Operation Data
                        //todo mohamed to SaveTerminalOperationData
                       //todo iRetRes = SaveTerminalOperationData();


                }
                return iRetRes;
        }




}