package com.mandin.antoine.attestations.generator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;

import androidx.core.content.res.ResourcesCompat;

import com.google.zxing.WriterException;
import com.mandin.antoine.attestations.R;
import com.mandin.antoine.attestations.model.Attestation;
import com.mandin.antoine.attestations.model.Place;
import com.mandin.antoine.attestations.model.Profile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class PdfGenerator {
    private static final int scale = 3;
    private static final int[] reasons_y = {
            553, //travail
            482, //achats
            434, //sante
            410, //famille
            373, //handicap
            349, //sport_animaux
            276, //convocation
            252, //missions
            228 //enfants
    };
    private static final int PDF_RESOURCE = R.raw.certificate3;

    public PdfDocument generate(Context context, Attestation attestation) throws IOException, WriterException {
        File tmp = buildTemporaryFile(context);

        ParcelFileDescriptor mFileDescriptor = ParcelFileDescriptor
                .open(tmp, ParcelFileDescriptor.MODE_READ_ONLY);
        if (mFileDescriptor != null) {
            PdfRenderer pdfRenderer = new PdfRenderer(mFileDescriptor);
            PdfDocument document = new PdfDocument();

            PdfRenderer.Page pageIn = pdfRenderer.openPage(0);

            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo
                    .Builder(pageIn.getWidth()*scale, pageIn.getHeight()*scale, 1).create();
            PdfDocument.Page pageOut = document.startPage(pageInfo);

            Bitmap result = Bitmap.createBitmap(pageIn.getWidth()*scale, pageIn.getHeight()*scale,
                    Bitmap.Config.ARGB_8888);
            pageIn.render(result, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            Rect rectIn = new Rect(0, 0, pageIn.getWidth()*scale, pageIn.getHeight()*scale);
            Rect rectOut = new Rect(rectIn);
            pageOut.getCanvas().drawBitmap(result, rectIn, rectOut, null);
            drawAttestationData(context, pageOut, attestation,
                    pageInfo.getPageWidth(), pageInfo.getPageHeight());

            document.finishPage(pageOut);
            pageIn.close();

            pdfRenderer.close();

            return document;
        }
        return null;

    }

    private File buildTemporaryFile(Context context) throws IOException {
        File tmp = new File(context.getFilesDir(), "tmp.pdf");
        BufferedInputStream in = null;
        FileOutputStream output = null;
        try {
            in = new BufferedInputStream(context.getResources().openRawResource(PDF_RESOURCE));
            output = new FileOutputStream(tmp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        final byte[] buffer = new byte[1024];
        int size;
        while ((size = in.read(buffer)) != -1) {
            output.write(buffer, 0, size);
        }
        in.close();
        output.close();
        return tmp;
    }

    public PdfDocument generate2(Context context, AttestationData data) throws IOException, WriterException {
        File tmp = buildTemporaryFile(context);

        ParcelFileDescriptor mFileDescriptor = ParcelFileDescriptor.open(tmp, ParcelFileDescriptor.MODE_READ_ONLY);
        if (mFileDescriptor != null) {
            PdfRenderer pdfRenderer = new PdfRenderer(mFileDescriptor);
            PdfDocument document = new PdfDocument();


            // Page 1
            PdfRenderer.Page pageIn = pdfRenderer.openPage(0);

            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo
                    .Builder(pageIn.getWidth(), pageIn.getHeight(), 1).create();
            PdfDocument.Page pageOut = document.startPage(pageInfo);

            Bitmap result = Bitmap.createBitmap(pageIn.getWidth(), pageIn.getHeight(), Bitmap.Config.ARGB_8888);
            pageIn.render(result, null, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT);
            Rect rectIn = new Rect(0, 0, pageIn.getWidth(), pageIn.getHeight());
            Rect rectOut = new Rect(rectIn);
            pageOut.getCanvas().drawBitmap(result, rectIn, rectOut, null);
            drawAttestationData(context, pageOut, data, pageInfo.getPageWidth(), pageInfo.getPageHeight());

            document.finishPage(pageOut);
            pageIn.close();

            pdfRenderer.close();

            return document;
        }
        return null;

    }

    private void drawAttestationData(Context context, PdfDocument.Page page,
                                     AttestationData data, int pageWidth, int pageHeight) throws WriterException {
        Canvas canvas = page.getCanvas();

        Typeface plain = ResourcesCompat.getFont(context, R.font.helvetica);
        Paint paint = new Paint();
        paint.setTypeface(plain);
        paint.setTextSize(11);
        // Name + last name
        canvas.drawText(data.getFirstName() + " " + data.getLastName(), 92, pageHeight-702, paint);
        canvas.drawText(data.getBirthdayString(), 92, pageHeight-684, paint);
        canvas.drawText(data.getPlaceOfBirth(), 214, pageHeight-684, paint);
        canvas.drawText(data.getAddress() + " " + data.getZipCode() + " " + data.getCity(),
                104, pageHeight-665, paint);

        paint.setTextSize(12);
        canvas.drawText("x", 47, pageHeight-reasons_y[data.getReason().ordinal()], paint);

        paint.setTextSize(11);
        canvas.drawText(data.getCity(), 78, pageHeight-76, paint);
        canvas.drawText(data.getOutDay(), 63, pageHeight-58, paint);
        canvas.drawText(data.getOutHour(), 227, pageHeight-58, paint);

        // QR code
        final int QRdimension = 92;
        QRGEncoder qrgEncoder = new QRGEncoder(data.buildQRData(), null, QRGContents.Type.TEXT,
                QRdimension);
        Bitmap qrCode = qrgEncoder.encodeAsBitmap();
        canvas.drawBitmap(qrCode, pageWidth - 156, pageHeight-25-QRdimension, null);
    }

    private void drawAttestationData(Context context, PdfDocument.Page page,
                                     Attestation attestation, int pageWidth, int pageHeight)
            throws WriterException {
        Canvas canvas = page.getCanvas();

        Typeface plain = ResourcesCompat.getFont(context, R.font.helvetica);
        Paint paint = new Paint();
        paint.setTypeface(plain);
        paint.setTextSize(11*scale);

        Profile profile = attestation.getProfile();
        canvas.drawText(profile.getFirstName() + " " + profile.getLastName(),
                119*scale, pageHeight-696*scale, paint);
        canvas.drawText(attestation.getBirthdayString(), 119*scale, pageHeight-674*scale, paint);
        canvas.drawText(profile.getPlaceOfBirth(), 297*scale, pageHeight-674*scale, paint);

        Place place = attestation.getPlace();
        canvas.drawText(place.getAddress() + " " + place.getZipCode() + " " + place.getCity(),
                133*scale, pageHeight-652*scale, paint);

        paint.setTextSize(18*scale);
        canvas.drawText("x", 84*scale,
                pageHeight-reasons_y[attestation.getReason().ordinal()]*scale, paint);

        paint.setTextSize(11*scale);
        canvas.drawText(place.getCity(), 105*scale, pageHeight-177*scale, paint);
        canvas.drawText(attestation.getUsingDay(), 91*scale, pageHeight-153*scale, paint);
        canvas.drawText(attestation.getUsingHour(), 264*scale, pageHeight-153*scale, paint);

        // QR code
        final int QRdimension = 92*scale;
        QRGEncoder qrgEncoder = new QRGEncoder(attestation.buildQRData(), null, QRGContents.Type.TEXT,
                QRdimension);
        Bitmap qrCode = qrgEncoder.encodeAsBitmap();
        canvas.drawBitmap(qrCode, pageWidth - 156*scale, pageHeight-100*scale-QRdimension, null);
    }


    public Bitmap test(Context context) throws IOException {

        File tmp = new File(context.getFilesDir(), "tmp.pdf");
        BufferedInputStream in = null;
        FileOutputStream output = null;
        try {
            in = new BufferedInputStream(context.getResources().openRawResource(PDF_RESOURCE));
            output = new FileOutputStream(tmp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        final byte[] buffer = new byte[1024];
        int size;
        while ((size = in.read(buffer)) != -1) {
            output.write(buffer, 0, size);
        }
        in.close();
        output.close();

        ParcelFileDescriptor mFileDescriptor = ParcelFileDescriptor.open(tmp, ParcelFileDescriptor.MODE_READ_ONLY);
        if (mFileDescriptor != null) {
            PdfRenderer pdfRenderer = new PdfRenderer(mFileDescriptor);

            PdfRenderer.Page page = pdfRenderer.openPage(0);

            Bitmap result = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
            page.render(result, null, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT);

            page.close();
            pdfRenderer.close();

            return result;
        }
        return null;
    }

    public void write(Context context, PdfDocument document) throws IOException {
        File result = new File(context.getFilesDir(), "result.pdf");
        document.writeTo(new FileOutputStream(result));
    }

    public Bitmap readResult(Context context) throws IOException {
        File result = new File(context.getFilesDir(), "result.pdf");

        ParcelFileDescriptor mFileDescriptor = ParcelFileDescriptor.open(result, ParcelFileDescriptor.MODE_READ_ONLY);
        if (mFileDescriptor != null) {
            PdfRenderer pdfRenderer = new PdfRenderer(mFileDescriptor);

            PdfRenderer.Page page = pdfRenderer.openPage(0);

            Bitmap res = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
            page.render(res, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

            page.close();
            pdfRenderer.close();

            return res;
        }
        return null;
    }
}
