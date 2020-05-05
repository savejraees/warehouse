package com.saifi.warehouse;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.saifi.warehouse.adapter.OpenBoxSubAdapter;
import com.saifi.warehouse.fragment.OpenBoxFragment;
import com.saifi.warehouse.model.ImageModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity {

    int PICK_IMAGE_MULTIPLE = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    ArrayList<String> imagePathListInvoice = new ArrayList<>();
    ArrayList<ImageModel> Invoicelist = new ArrayList<>();
    RecyclerView rv_Invoice;
    Button selectInVoiceButton,uploadInVoiceButton;
    Bitmap bitmapInVoice;
    File photoInvoiceFile = null;
    Uri photoUriInvoice;
    String mCurrentPhotoPathInvoice;
    OpenBoxSubAdapter mIMGAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        getSupportActionBar().hide();
        requestStoragePermission();
        requestMultiplePermissions();

        rv_Invoice = findViewById(R.id.rv_Invoice);
        selectInVoiceButton = findViewById(R.id.selectInVoiceButton);
        uploadInVoiceButton = findViewById(R.id.uploadInVoiceButton);


        selectInVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ImageActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE_MULTIPLE);

                } else {
                    try {
                        takeInvoiceCameraImg();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        uploadInVoiceButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ImageActivity.this,MainActivity.class));
                finishAffinity();

            }
        });

    }

    private void takeInvoiceCameraImg() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                photoInvoiceFile = createImageFileInVOice();
                Log.d("checkexcesdp", String.valueOf(photoInvoiceFile));
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.d("checkexcep", ex.getMessage());
            }

            photoInvoiceFile = createImageFileInVOice();
            //lk changes here
            photoUriInvoice = FileProvider.getUriForFile(ImageActivity.this, getPackageName() + ".provider", photoInvoiceFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUriInvoice);
            startActivityForResult(takePictureIntent, 2);
        }
    }

    private File createImageFileInVOice() throws IOException {
        String imageFileName = "GOOGLES" + System.currentTimeMillis();
        String storageDir = Environment.getExternalStorageDirectory() + "/skImages";
        Log.d("storagepath===", storageDir);
        File dir = new File(storageDir);
        if (!dir.exists())
            dir.mkdir();

        File image = new File(storageDir + "/" + imageFileName + ".jpg");
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPathInvoice = image.getAbsolutePath();
        return image;
    }

    public void rotateImageInvoice() throws IOException {

        try {
            String photoPath = photoInvoiceFile.getAbsolutePath();
            imagePathListInvoice.add(photoPath);
            bitmapInVoice = MediaStore.Images.Media.getBitmap(ImageActivity.this.getContentResolver(), photoUriInvoice);
            bitmapInVoice = Bitmap.createScaledBitmap(bitmapInVoice, 800, 800, false);
            ImageModel imageModel = new ImageModel();
            imageModel.setImageMobile(bitmapInVoice);
            if (Invoicelist.size() < 5) {
                Invoicelist.add(imageModel);
            }


            if (Invoicelist.size() > 5) {
                //Toast.makeText(getApplicationContext(), "Max Limit Only 10", Toast.LENGTH_SHORT).show();
            }

            rv_Invoice.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            mIMGAdapter = new OpenBoxSubAdapter(Invoicelist, ImageActivity.this);
            rv_Invoice.setAdapter(mIMGAdapter);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == 2) {
                rotateImageInvoice();
            }

        } catch (Exception e) {
        }
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
//                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(

                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
//                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

}
