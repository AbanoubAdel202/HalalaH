package com.example.halalah.TMS;

/**********************************************************************/
   public class Conn_ٍSecondary
    {
        Dialup dialup;
        Tcp_IP tcp_ip;
        Gprs gprs;
        Wifi wifi;
        Gsm gsm;

        Conn_ٍSecondary()
        {
            dialup=new Dialup();
            tcp_ip=new Tcp_IP();
            gprs=new Gprs();
            wifi=new Wifi();
            gsm=new Gsm();
        }


    }
