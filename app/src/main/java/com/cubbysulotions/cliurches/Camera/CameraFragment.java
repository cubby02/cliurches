package com.cubbysulotions.cliurches.Camera;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cubbysulotions.cliurches.Home.HomeActivity;
import com.cubbysulotions.cliurches.R;
import com.cubbysulotions.cliurches.Utilities.BackpressedListener;
import com.cubbysulotions.cliurches.Utilities.ImageResizer;
import com.cubbysulotions.cliurches.Utilities.LoadingDialog;
import com.cubbysulotions.cliurches.Utilities.SessionManagement;
import com.cubbysulotions.cliurches.Utilities.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

//import org.tensorflow.lite.DataType;
//import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class CameraFragment<CliurchesMlModelV1> extends Fragment implements BackpressedListener {


    public static final String IMG_URL = "img_url";
    public static final String CHURCH_NAME = "church_name";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }
    private LoadingDialog loadingDialog;
    private NavController navController;
    private ImageView imgSearch;
    private Button btnCamera, btnGallery, btnIdentify;
    private Bitmap imageBitmap;
    private final int REQUEST_IMAGE_CAPTURE = 1;
    private final int IMAGE_PICK_CODE = 1000;
    private final int REQUEST_WRITE_STORAGE = 2;
    private String u_plantID; // unique plant id

    private Uri contentUri; // final image URI; for identification purposes
    private String currentPhotoPath;
    private Uri photoURI;

    private Handler handler;
    private Runnable cancelHandler, cancelHandler2, cancelHandler3;

    private Uri imageURI;
    private Bitmap bitmap, bitmapReduced;

    private boolean isAnalyzing;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        imgSearch = view.findViewById(R.id.imgSearchChurch);
        btnCamera = view.findViewById(R.id.btnSnap);
        btnGallery = view.findViewById(R.id.btnGallery);
        btnIdentify = view.findViewById(R.id.btnIdentify);
        handler = new Handler();


        //isAnalyzing = true;
        snapPhoto();
        openGallery();
        identify();
    }

    private void identify() {
        btnIdentify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap == null){
                    toast("Please upload or snap a photo");

                } else {
                    loadingDialog = new LoadingDialog(getActivity());
                    handler = new Handler();
                    loadingDialog.startLoading("Please wait");

                    identifyChurch();

                }

            }
        });
    }

    //random string generator
    protected String set_image_filename() {
        String charList = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8) {
            int index = (int) (rnd.nextFloat() * charList.length());
            salt.append(charList.charAt(index));
        }
        return salt.toString();

    }

    private void customLoading(){
        try{

            cancelHandler = new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.changeLabel("Analyzing Image");
                        }
                    }, 2000);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.changeLabel("Fetching Data");
                        }
                    }, 4000);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.changeLabel("Matching Results");
                        }
                    }, 6000);
                }
            };



            cancelHandler.run();

        }catch(Exception e){
            Log.e(TAG,"loading",e);
        }


    }

    private void analyzeImage(String img_url){
        try {
            customLoading();
            String url = "http://171.22.124.181/cliurches-api/api/media/identify?url="+ img_url +"";
            StringRequest stringRequest = new StringRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loadingDialog.stopLoading();
                            handler.removeCallbacks(cancelHandler); //cancel the custom loading part

                            Bundle bundle = new Bundle();
                            bundle.putString(IMG_URL, img_url);
                            bundle.putString(CHURCH_NAME, response.trim());

                            navController.navigate(R.id.action_cameraFragment_to_matchResultFragment, bundle);
                            ((HomeActivity)getActivity()).hideNavigationBar(true);
                            ((HomeActivity)getActivity()).hideTopBarPanel(true);
                        }
                    }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingDialog.stopLoading();
                        handler.removeCallbacks(cancelHandler); //cancel the custom loading part
                        if (error.networkResponse == null) {
                            if (error.getClass().equals(TimeoutError.class)) {
                                // Show timeout error message
                                Toast.makeText(getContext(),
                                        "Oops. Timeout error!",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                        Log.e(TAG, "onErrorResponse: ", error);
                    }
                }
            );
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue_ML(stringRequest);
        } catch (Exception e) {
            toast("Something went wrong, please try again");
            Log.e(TAG, "onClick: ", e);
        }
    }

    private void identifyChurch() {
        try {
            String URL = "http://171.22.124.181/cliurches-api/api/media/upload/";
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                String img_url = object.getString("img_path");
                                Log.e(TAG, "onResponse: " + img_url);
                                analyzeImage(img_url);

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loadingDialog.stopLoading();
                    handler.removeCallbacks(cancelHandler); //cancel the custom loading part

                    Log.e(TAG, "onErrorResponse: ", error);
                }
            }
            ){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    String filename = set_image_filename();
                    SessionManagement sessionManagement = new SessionManagement(getActivity());


                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmapReduced.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                    byte[] imgBytes = byteArrayOutputStream.toByteArray();
                    String encode = Base64.encodeToString(imgBytes, Base64.DEFAULT);

                    Map<String, String> params = new HashMap<>();
                    params.put("name", filename);
                    params.put("image", encode);
                    params.put("api_key", sessionManagement.getSession2());


                    return  params;
                }
            };
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);

        } catch (Exception e){
            toast("Something went wrong, please try again");
            Log.e(TAG, "onClick: ", e);
        }

    }

    private void openGallery() {
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // TODO: add open gallery here
                    if(checkGalleryPermissions()){
                        openingGallery();
                    }else{
                        requestGalleryPermission();
                    }

                } catch (Exception e){
                    toast("Something went wrong, please try again");
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });
    }

    private void snapPhoto() {
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(checkCamPermissions()){
                        takePicture();
                    }else{
                        requestCamPermission();
                    }

                    //navController.navigate(R.id.action_cameraFragment_to_matchResultFragment);
                    //((HomeActivity)getActivity()).hideNavigationBar(true);
                    //((HomeActivity)getActivity()).hideTopBarPanel(true);
                } catch (Exception e){
                    toast("Something went wrong, please try again");
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });
    }


    private void openingGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }
    // when allowed app permissions
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
//            File f = new File(currentPhotoPath);
            contentUri = photoURI;
            imgSearch.setImageBitmap(imgBitmap());
//            saveImage(setPic());
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "IMAGE_" + timeStamp + "." + getFileExt(contentUri);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentUri);
                bitmapReduced = ImageResizer.reduceBitmapSize(bitmap, 240000);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
            contentUri = data.getData();
            imgSearch.setImageURI(contentUri);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "IMAGE_" + timeStamp + "." + getFileExt(contentUri);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentUri);
                bitmapReduced = ImageResizer.reduceBitmapSize(bitmap, 240000);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if(requestCode == REQUEST_WRITE_STORAGE && resultCode == Activity.RESULT_OK){
            //saveImage(imageBitmap);
        }
    }

    //for file extension
    private String getFileExt(Uri contentUri){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(contentUri));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case REQUEST_IMAGE_CAPTURE: {
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(cameraPermission){
                        toast("Permissions Granted..");
                        takePicture();
                    }else{
                        toast("Permissions denied..");
                    }
                }
            }
            case IMAGE_PICK_CODE: {
                if (grantResults.length > 0) {
                    boolean galleryPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(galleryPermission){
                        toast("Permissions Granted..");
                        openGallery();
                    }else{
                        toast("Permissions denied..");
                    }
                }
            }
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0) {
                    boolean storagePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storagePermission){
                        toast("Permissions Granted..");
                    }else{
                        toast("Permissions denied..");
                    }
                }
            }
        }
    }


    private void takePicture() {
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if(checkStoragePermissions()){
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    photoURI = FileProvider.getUriForFile(getActivity(),
                            "com.example.CLIURCHES",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }
        else{
            requestStoragePermission();
        }
    }

    // creating root img file  (only for the application and cannot be seen in gallery)
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGES_" + timeStamp + "_";
//        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private boolean checkStoragePermissions(){
        int camPermission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return camPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission(){
        int PERMISSION_CODE = 201;
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
    }
    private boolean checkGalleryPermissions(){
        int camPermission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return camPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestGalleryPermission(){
        int PERMISSION_CODE = 1001;
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
    }
    
    private boolean checkCamPermissions(){
        int camPermission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
        return camPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCamPermission(){
        int PERMISSION_CODE = 200;
        requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_CODE);
    }


    // the bitmap displayed in the imageView
    private Bitmap imgBitmap() {
        // Get the dimensions of the View
        int targetW = imgSearch.getWidth();
        int targetH = imgSearch.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        btnIdentify.setVisibility(View.VISIBLE);
        return bitmap;
    }


    private void toast(String msg ){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        ((HomeActivity) getActivity()).home();
    }

    public static BackpressedListener backpressedlistener;

    @Override
    public void onPause() {
        backpressedlistener = null;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        backpressedlistener = this;
    }
}