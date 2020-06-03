package com.example.halalah.TMS;

/**********************************************************************/
    public class Conn_Primary
    {

        public Dialup dialup;
        public Tcp_IP tcp_ip;
        public Gprs gprs;
        public Wifi wifi;
        public Gsm gsm;
        Conn_Primary()
        {
            dialup=new Dialup();
            tcp_ip=new Tcp_IP();
            gprs=new Gprs();
            wifi=new Wifi();
            gsm=new Gsm();
        }
    }
