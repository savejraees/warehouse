package com.saifi.warehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.saifi.warehouse.adapter.MainAdapter;
import com.saifi.warehouse.constant.NoScanResultException;
import com.saifi.warehouse.constant.ScanResultReceiver;
import com.saifi.warehouse.constant.SessonManager;
import com.saifi.warehouse.model.MainCatogryModel;

import java.util.ArrayList;
import java.util.List;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class MainActivity extends AppCompatActivity implements ScanResultReceiver {

    RecyclerView rv_main;

    String[] textMain = {"Total Purchase", "Request", "QC", "OpenBox", "Customer Used", "Refurbised", "Stores", "warehouse", "Return","Logout"};
    int[] imgMain = {R.drawable.request_icon, R.drawable.request_icon, R.drawable.request_icon, R.drawable.request_icon,
            R.drawable.request_icon, R.drawable.request_icon, R.drawable.request_icon, R.drawable.request_icon, R.drawable.request_icon,R.drawable.request_icon};

    ArrayList<MainCatogryModel> list = new ArrayList<>();
    MainAdapter mainAdapter;
    LinearLayout linearCateogry;
    Boolean mainCategory = true;
    private static final int STORAGE_PERMISSION_CODE = 123;
    public String barcode = "";
   // ImageView imgLogout;
    long back_pressed = 0;
    RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        askForPermissioncamera(Manifest.permission.CAMERA, CAMERA);
        requestStoragePermission();
        requestMultiplePermissions();

        rv_main = findViewById(R.id.rv_main);
        linearCateogry = findViewById(R.id.linearCateogry);
       // imgLogout = findViewById(R.id.imgLogout);
        mainLayout = findViewById(R.id.mainLayout);
        rvSet();

    }

    private void rvSet() {

        for (int i = 0; i < textMain.length; i++) {
            MainCatogryModel mainCatogryModel = new MainCatogryModel();
            mainCatogryModel.setCategoryNAme(textMain[i]);
            mainCatogryModel.setImg(imgMain[i]);

            list.add(mainCatogryModel);
        }
        LinearLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 1);
        rv_main.setLayoutManager(layoutManager);
        mainAdapter = new MainAdapter(MainActivity.this, list);
        rv_main.setAdapter(mainAdapter);
    }

    public void ButtonShow(View view) {
        if (mainCategory == true) {
            mainCategory = false;
            linearCateogry.setVisibility(View.GONE);
        } else {
            linearCateogry.setVisibility(View.VISIBLE);
            mainCategory = true;
        }

    }

    @Override
    public void scanResultData(String codeFormat, String codeContent) {
        barcode = codeContent;
    }

    @Override
    public void scanResultData(NoScanResultException noScanData) {

    }

    private void askForPermissioncamera(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {
//            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
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

            } else {
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

    @Override
    public void onBackPressed() {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        if(fragmentManager.getBackStackEntryCount()>1){
//            for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
//                fragmentManager.popBackStack();
//            }
//        }else {

            if (back_pressed + 2000 > System.currentTimeMillis())
                super.onBackPressed();
            else {
                Snackbar snackbar = Snackbar.make(mainLayout, "Double Tap to Exit!", Snackbar.LENGTH_SHORT);
                View view = snackbar.getView();
                view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                snackbar.show();
                back_pressed = System.currentTimeMillis();
            }
//        }

    }

}
