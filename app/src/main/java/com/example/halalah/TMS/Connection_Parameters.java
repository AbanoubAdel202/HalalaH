package com.example.halalah.TMS;

import com.example.halalah.TMS.Conn_Primary;
import com.example.halalah.TMS.Conn_ٍSecondary;

/**********************************************************************/
     public class Connection_Parameters
    {
        public Conn_Primary conn_primary;
        public Conn_ٍSecondary conn_secondary;
        Connection_Parameters()
        {
            conn_primary = new Conn_Primary();
            conn_secondary = new Conn_ٍSecondary();
        }

    }
