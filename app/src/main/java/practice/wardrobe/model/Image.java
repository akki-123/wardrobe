package practice.wardrobe.model;

import android.graphics.Bitmap;

/**
 * Created by akhilhanda on 25/06/16.
 */
public class Image {

    Bitmap bitmap;
    String fileName;

    public Image(Bitmap bitmap, String fileName) {
        this.bitmap = bitmap;
        this.fileName = fileName;
    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
