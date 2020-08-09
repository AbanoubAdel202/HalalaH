package com.example.halalah.registration;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class AssetsHelper {

    public static byte[] loadKeyFile(Context context, String inFile) throws IOException {
        byte[] buffer = null;
        InputStream stream = context.getAssets().open(inFile);

        int size = stream.available();
        buffer = new byte[size];
        stream.read(buffer);
        stream.close();

        return buffer;

    }

}
