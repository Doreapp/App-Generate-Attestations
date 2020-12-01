package com.mandin.antoine.attestations;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.mandin.antoine.attestations.data_access.Service;
import com.mandin.antoine.attestations.generator.GeneratorAsync;
import com.mandin.antoine.attestations.generator.PdfGenerator;
import com.mandin.antoine.attestations.model.Attestation;
import com.mandin.antoine.attestations.user_interface.DialogQRCode;
import com.mandin.antoine.attestations.user_interface.ZoomableImageView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ViewActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1001;
    private static final int REQUEST_CODE_CREATE_FILE = 1002;
    public static final String EXTRA_ATTESTATION = "attestation";
    private Attestation attestation;
    private PdfDocument document = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Bundle bundle = getIntent().getBundleExtra(EXTRA_ATTESTATION);
        attestation = Attestation.fromBundle(bundle, null);

        final View loadingView = findViewById(R.id.loading_view);

        new GeneratorAsync(attestation, new GeneratorAsync.OnResultListener() {
            @Override
            public void onResult(Bitmap bitmap, PdfDocument document) {
                ZoomableImageView touch = findViewById(R.id.zoomable_iv);
                touch.setImageBitmap(bitmap);
                loadingView.setVisibility(View.GONE);
                ViewActivity.this.document = document;
            }
        }).execute(this);

        findViewById(R.id.btn_qr_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogQRCode(ViewActivity.this, attestation).show();
            }
        });

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btn_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });
    }

    private void download(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
            } else {
                startDownloading();
            }
        } else {
            startDownloading();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)       {
                startDownloading();
            } else {
                Toast.makeText(this, "Il nous faut la permission d'écriture pour télécharger l'attestation sur votre appareil.",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==REQUEST_CODE_CREATE_FILE
                && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                actualDownloadFile(uri);
            }
        }
    }

    private void startDownloading(){
        //TODO
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, buildDocumentTitle(attestation)+".pdf");

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when your app creates the document.
        //intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, REQUEST_CODE_CREATE_FILE);
    }

    private static String buildDocumentTitle(Attestation attestation){
        SimpleDateFormat shortFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
        return "Attestation_"+shortFormat.format(attestation.getUsingDate())+"_"+attestation.getPlace().getZipCode();
    }

    private void actualDownloadFile(Uri uri){
        try {
            ParcelFileDescriptor pfd = getContentResolver().
                    openFileDescriptor(uri, "w");
            FileOutputStream fileOutputStream =
                    new FileOutputStream(pfd.getFileDescriptor());

            //TODO generate another document to download only (with 2 pages)
            document.writeTo(fileOutputStream);

            fileOutputStream.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}