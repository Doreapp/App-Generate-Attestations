package com.mandin.antoine.attestations.user_interface;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.zxing.WriterException;
import com.mandin.antoine.attestations.R;
import com.mandin.antoine.attestations.model.Attestation;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class DialogQRCode extends Dialog {
    private Attestation attestation;

    public DialogQRCode(@NonNull Context context, Attestation attestation) {
        super(context);
        setContentView(R.layout.dialog_qr_code);
        this.attestation = attestation;
        ImageView iv = findViewById(R.id.iv_main);

        QRGEncoder qrgEncoder = new QRGEncoder(attestation.buildQRData(), null,
                QRGContents.Type.TEXT,
                500);
        try {
            Bitmap res = qrgEncoder.encodeAsBitmap();
            iv.setImageBitmap(res);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


}
