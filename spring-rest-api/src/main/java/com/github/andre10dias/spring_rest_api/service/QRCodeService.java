package com.github.andre10dias.spring_rest_api.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class QRCodeService {

    public InputStream generateQRCode(String url, int width, int height) throws IOException {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty for QR code generation");
        }

        // Ensure minimum dimensions and make them square
        int size = Math.max(150, Math.max(width, height));

        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 2);  // Add some margin
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);  // Medium error correction

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, size, size, hints);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            return new ByteArrayInputStream(pngOutputStream.toByteArray());
        } catch (WriterException we) {
            throw new IOException("Failed to generate QR code for URL: " + url + ". " +
                    "URL length: " + url.length() + " characters. " +
                    "Try using a shorter URL or increase QR code size.", we);
        }
    }

}
