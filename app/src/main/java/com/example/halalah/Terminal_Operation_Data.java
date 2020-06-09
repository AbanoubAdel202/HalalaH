package com.example.halalah;


/** Header Terminal operation Data
 \Class Name: Terminal_Operation_Data
 \Param  :
 \Return :
 \Pre    :
 \Post   :
 \Author	: mostafa hussiny
 \DT		: 5/31/2020
 \Des    : Container for Terminal operation DATA
 */
public class Terminal_Operation_Data {

        public  boolean m_bregistered;
        public  int m_iSTAN;
        public int m_iTransactionCounter;
        public  String m_sTerminalID;
        public  String m_sMerchantID;
        public  String sAcquirer_ins_Code;
        public  int Mada_Auto_Selection;
        public  int m_iPinKeyboardMode; // 1 for out of order 0 for inorder

        public Terminal_Operation_Data(){
                m_iPinKeyboardMode=1;
        }
}
