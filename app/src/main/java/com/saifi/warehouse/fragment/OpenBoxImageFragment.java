package com.saifi.warehouse.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.saifi.warehouse.ImageActivity;
import com.saifi.warehouse.MainActivity;
import com.saifi.warehouse.R;
import com.saifi.warehouse.constant.ApiFactory;
import com.saifi.warehouse.constant.ApiInterface;
import com.saifi.warehouse.constant.Url;
import com.saifi.warehouse.constant.Views;
import com.saifi.warehouse.model.ImageModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpenBoxImageFragment extends Fragment {

    public OpenBoxImageFragment() {
        // Required empty public constructor
    }

    Views views;
    View view;
    int PICK_IMAGE_MULTIPLE = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    ArrayList<String> imagePathListInvoice = new ArrayList<>();
    ArrayList<ImageModel> Invoicelist = new ArrayList<>();
    RecyclerView rv_Invoice;
    Button selectInVoiceButton, uploadInVoiceButton;
    Bitmap bitmapInVoice;
    File photoInvoiceFile = null;
    Uri photoUriInvoice;
    String mCurrentPhotoPathInvoice;
    OpenBoxxSubAdapter mIMGAdapter;
    int bookingId;
    String fragmentType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getActivity() fragment
        view = inflater.inflate(R.layout.fragment_open_box_image, container, false);
        requestStoragePermission();
        requestMultiplePermissions();

        views = new Views();
        rv_Invoice = view.findViewById(R.id.rv_Invoice);
        selectInVoiceButton = view.findViewById(R.id.selectInVoiceButton);
        uploadInVoiceButton = view.findViewById(R.id.uploadInVoiceButton);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            bookingId = bundle.getInt("phoneId", 0);
            fragmentType = bundle.getString("type", "");
            Log.d("sapol", String.valueOf(bookingId)+" "+fragmentType);
        }

        selectInVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE_MULTIPLE);

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

                hitApiUploadInvoice();
            }
        });
        return view;
    }

    private void takeInvoiceCameraImg() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            try {
                photoInvoiceFile = createImageFileInVOice();
                Log.d("checkexcesdp", String.valueOf(photoInvoiceFile));
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.d("checkexcep", ex.getMessage());
            }

            photoInvoiceFile = createImageFileInVOice();
            //lk changes here
            photoUriInvoice = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", photoInvoiceFile);
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
            bitmapInVoice = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUriInvoice);
            bitmapInVoice = Bitmap.createScaledBitmap(bitmapInVoice, 800, 800, false);
            ImageModel imageModel = new ImageModel();
            imageModel.setImageMobile(bitmapInVoice);
            if (Invoicelist.size() < 5) {
                Invoicelist.add(imageModel);
            }


            if (Invoicelist.size() > 5) {
                //Toast.makeText(getActivity(), "Max Limit Only 10", Toast.LENGTH_SHORT).show();
            }

            rv_Invoice.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            mIMGAdapter = new OpenBoxxSubAdapter(Invoicelist, getActivity());
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
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to getActivity() block
            //Here you can explain why you need getActivity() permission
            //Explain here why you need getActivity() permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
//                Toast.makeText(getActivity(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getActivity(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(getActivity())
                .withPermissions(

                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
//                            Toast.makeText(getActivity(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void hitApiUploadInvoice() {

        views.showProgress(getActivity());
        HashMap<String, RequestBody> partMap = new HashMap<>();
        partMap.put("key", ApiFactory.getRequestBodyFromString(Url.key));
        partMap.put("booking_id", ApiFactory.getRequestBodyFromString(String.valueOf(bookingId)));

        MultipartBody.Part[] imageArrayInvoice = new MultipartBody.Part[imagePathListInvoice.size()];

        for (int i = 0; i < imageArrayInvoice.length; i++) {
            File file = new File(imagePathListInvoice.get(i));
            try {
                File compressedfile = new Compressor(getActivity()).compressToFile(file);
                RequestBody requestBodyArray = RequestBody.create(MediaType.parse("image/*"), compressedfile);
                imageArrayInvoice[i] = MultipartBody.Part.createFormData("image[]", compressedfile.getName(), requestBodyArray);

                Log.d("astysawe", String.valueOf(imageArrayInvoice[i]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ApiInterface iApiServices = ApiFactory.createRetrofitInstance(Url.BASE_URL).create(ApiInterface.class);
        iApiServices.imageAPi(imageArrayInvoice, partMap)
                .enqueue(new Callback<JsonObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        //Toast.makeText(ClaimAssistanceActivity_form.this, "" + response, Toast.LENGTH_SHORT).show();
                        views.hideProgress();

                        JsonObject jsonObject = null;
                        try {
                            jsonObject = response.body();

                            if (jsonObject.get("code").getAsString().equals("200")) {
                                views.showToast(getActivity(), jsonObject.get("msg").getAsString());
                                if(fragmentType.equalsIgnoreCase("OpenBox")){
                                    Fragment fragment = new OpenBoxFragment();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.popBackStack();
                                    FragmentTransaction  fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.frame,fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }else {
                                    Fragment fragment = new CustomerUsedFragment();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.popBackStack();
                                    FragmentTransaction  fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.frame,fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }
                            } else {
                                views.showToast(getActivity(), jsonObject.get("msg").getAsString());
                            }
                        } catch (Exception e) {

                            Log.d("error_message", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                        Toast.makeText(getActivity(), "fail" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        views.hideProgress();
                        Log.d("data_error", t.getMessage());

                    }
                });
    }

    public class OpenBoxxSubAdapter extends RecyclerView.Adapter<OpenBoxxSubAdapter.ListViewHolder> {

        ArrayList<ImageModel> list;
        Context context;

        public OpenBoxxSubAdapter(ArrayList<ImageModel> list, Context context) {
            this.context = context;
            this.list = list;
        }

        @Override
        public OpenBoxxSubAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.row_image, null);

            return new OpenBoxxSubAdapter.ListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(OpenBoxxSubAdapter.ListViewHolder holder, final int position) {
            final ImageModel imageModel = list.get(position);
            holder.image.setImageBitmap(imageModel.getImageMobile());
            holder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeAt(position);
                }
            });
        }

        public void removeAt(int position) {
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemChanged(position);
            imagePathListInvoice.remove(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ListViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            TextView close;

            public ListViewHolder(View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.ivGallery);
                close = itemView.findViewById(R.id.badge_view);
            }

        }
    }

}
