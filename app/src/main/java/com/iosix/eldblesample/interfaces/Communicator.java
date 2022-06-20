package com.iosix.eldblesample.interfaces;

import android.graphics.Bitmap;

public interface Communicator {

    void sendBitmap(Bitmap bitmap);

    void sendMechanicBitmap(Bitmap bitmap);

    void sendHasDefect(Boolean b);
}
