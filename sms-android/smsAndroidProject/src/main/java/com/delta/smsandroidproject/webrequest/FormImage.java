package com.delta.smsandroidproject.webrequest;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;

/**
 * 
 */
public class FormImage {

    private String mName ;

    private String mFileName ;

    private String mValue ;

    private String mMime ;

    private Bitmap mBitmap ;

    public FormImage(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public String getName() {
//        return mName;
        return "Image" ;
    }

    public String getFileName() {
        return "test.png";
    }

    public byte[] getValue() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        mBitmap.compress(Bitmap.CompressFormat.JPEG,80,bos) ;
        return bos.toByteArray();
    }

    public String getMime() {
        return "image/png";
    }
}
