package tcd.sweng.barcodetracker.generation;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

/**
 * Created by cal on 17/11/16.
 */

public class Barcode
{
    public static Bitmap generateBarcode(String codeData, BarcodeFormat barcodeFormat, int codeWidth, int codeHeight)
    {

        try
        {
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            BitMatrix byteMatrix = null;
            try
            {
                byteMatrix = new MultiFormatWriter().encode(codeData,
                        barcodeFormat, codeWidth, codeHeight, null);
            } catch (IllegalArgumentException iae)
            {
                iae.printStackTrace();
            }

            int width = byteMatrix.getWidth();
            int height = byteMatrix.getHeight();

            Bitmap imageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            for (int i = 0; i < width; i++)
            {
                for (int j = 0; j < height; j++)
                {
                    imageBitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }

            return imageBitmap;

        } catch (WriterException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap generateAutoBarcode(String data, String format, int width, int height)
    {
        return generateBarcode(data, BarcodeFormat.valueOf(format), width, height);
    }
}
