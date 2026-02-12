package com.example.musee.classes;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;
public class UtilsClass {
    private static UtilsClass instance;

    private FirebaseServices fbs;
    private String imageStr;

    public UtilsClass()
    {
        fbs = FirebaseServices.getInstance();
    }

    public static UtilsClass getInstance()
    {
        if (instance == null)
            instance = new UtilsClass();

        return instance;
    }
    public void showMessageDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(message);
        //builder.setMessage(message);

        // Add a button to dismiss the dialog box
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // You can perform additional actions here if needed
                dialog.dismiss();
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // 🌟 تعريف الواجهة
    public interface OnUploadCompleteListener {
        void onUploadComplete(Uri uri); // سيرسل رابط الصورة بعد الرفع
    }

    public void uploadImage(Context context, Uri selectedImageUri, OnUploadCompleteListener listener) {
        StorageReference imageRef = fbs.getStorage().getReference()
                .child("images/" + UUID.randomUUID().toString() + ".jpg");

        imageRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot ->
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            fbs.setSelectedImageURL(uri);
                            Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                            // استدعاء الـ listener بعد انتهاء الرفع
                            if(listener != null) {
                                listener.onUploadComplete(uri);
                            }
                        })
                )
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

}
