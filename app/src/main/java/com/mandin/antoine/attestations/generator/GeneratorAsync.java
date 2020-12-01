package com.mandin.antoine.attestations.generator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.util.Pair;

import com.google.zxing.WriterException;
import com.mandin.antoine.attestations.model.Attestation;

import java.io.IOException;

public class GeneratorAsync extends AsyncTask<Context, Void, Pair<Bitmap, PdfDocument>> {
    private Attestation attestation;
    private OnResultListener onResultListener;

    public interface OnResultListener {
        void onResult(Bitmap bitmap, PdfDocument document);
    }

    public GeneratorAsync(Attestation attestation, OnResultListener onResultListener) {
        this.attestation = attestation;
        this.onResultListener = onResultListener;
    }

    @Override
    protected Pair<Bitmap, PdfDocument> doInBackground(Context... contexts) {
        PdfGenerator generator = new PdfGenerator();
        PdfDocument document = null;
        try {
            document = generator.generate(contexts[0],attestation);
            generator.write(contexts[0], document);
            return new Pair<Bitmap, PdfDocument>(generator.readResult(contexts[0]), document);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Pair<Bitmap, PdfDocument> result) {
        super.onPostExecute(result);
        onResultListener.onResult(result.first, result.second);
    }
}
