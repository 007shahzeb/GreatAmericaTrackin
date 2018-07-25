package android.com.activity;
// https://stackoverflow.com/questions/22874735/upload-large-file-with-progress-bar-and-without-outofmemory-error-in-android

import android.Manifest;
import android.com.garytransportnew.R;
import android.com.net.HttpModule;
import android.com.responseModel.ResponseUploadDocumnets;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.mindorks.paracamera.Camera;
import com.sdsmdg.tastytoast.TastyToast;
import com.vistrav.ask.Ask;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import dmax.dialog.SpotsDialog;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadImageActivity extends AppCompatActivity implements View.OnClickListener {


    // https://github.com/androidmads/Retrofit2MediaUpload/blob/master/app/src/main/java/com/androidmads/retrofit2mediaupload/MainActivity.java

    private ImageView image_SelectFromCamera;
    private TextView tv_SelectFromPhotos, tv_transport_docFile, tv_MBTextview;

    private static int RESULT_LOAD_IMAGE = 2;
    Handler handler = new Handler();

    private TextView showingProgressiveValue, imageUploadingButton, tv_Cancel;
    public Camera camera;

    public Uri selectedImage;
    public Bitmap bitmap;

    String filePath = "";
    int file_size, file_size_gallery;

    String filename_gallery;


    // New Implementations

    private final String filename = "path.substring(path.lastIndexOf(\"/\") + 1)";
    private String urlString = "http://34.234.186.44:4000/driverapp/sendDocument";

    private TextView tv;
    long totalSize = 0;

    private long lastClickTime = 0;
    private Context mContext;

    private SpotsDialog dialog = null;
    private boolean isClickable = false;

    LinearLayout parent_layout;
    ProgressBar mProgressBar;

    private TextView filepathName;
    private TextView filesizeName;

    private ImageView image;

    String orderID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image_layout_contraint);

        if (getIntent() != null) {
            if (getIntent().hasExtra("orderId")) {
                orderID = getIntent().getStringExtra("orderId");
            }
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        findingIdsHere();
        listener();
        buildingTheCamera();
        mContext = this;


        Ask.on(this)
                .id(5) // in case you are invoking multiple time Ask from same activity or fragment
                .forPermissions(Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withRationales("Location permission need for map to work properly",
                        "In order to save file you will need to grant storage permission") //optional
                .go();

    }

    private void buildingTheGallery() {

        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }


    private void buildingTheCamera() {

        // Build the camera
        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // CAMERA CASE
        if (requestCode == Camera.REQUEST_TAKE_PHOTO) {


            createLayout();
            bitmap = camera.getCameraBitmap();
            if (bitmap != null) {
                filePath = camera.getCameraBitmapPath();
                File file = new File(filePath);
                file_size = Integer.parseInt(String.valueOf(file.length() / 1024)); // Getting the file size here in KB

                String path = filePath;//it contain your path of image..im using a temp string..
                String filename = path.substring(path.lastIndexOf("/") + 1); // Getting the file name here

                filepathName.setText(filename);
                filesizeName.setText(String.valueOf(file_size) + " KB");


            } else {
//                Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
            }

            // GALLERY CASE

        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            createLayout();

            selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            filePath = cursor.getString(columnIndex);
            File file = new File(filePath);
            file_size_gallery = Integer.parseInt(String.valueOf(file.length() / 1024));

            String path = filePath;//it contain your path of image..im using a temp string..
            filename_gallery = path.substring(path.lastIndexOf("/") + 1);

            filepathName.setText(filename_gallery);
            filesizeName.setText(String.valueOf(file_size_gallery) + " KB");
            cursor.close();

        }
    }


    private void listener() {
        image_SelectFromCamera.setOnClickListener(this);
        tv_SelectFromPhotos.setOnClickListener(this);
        imageUploadingButton.setOnClickListener(this);
        tv_Cancel.setOnClickListener(this);
        imageUploadingButton.setClickable(isClickable);
    }


    private void clickingPicturesFromCamera() {
        // Call the camera takePicture method to open the existing camera
        try {
            camera.takePicture();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void findingIdsHere() {

        image_SelectFromCamera = findViewById(R.id.image_SelectFromCamera);
        tv_SelectFromPhotos = findViewById(R.id.tv_SelectFromPhotos);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        tv_MBTextview = findViewById(R.id.tv_MBTextview);
        tv_transport_docFile = findViewById(R.id.tv_transport_docFile);
        imageUploadingButton = findViewById(R.id.imageUploadingButton);
        tv_Cancel = findViewById(R.id.tv_Cancel);
        parent_layout = findViewById(R.id.linear_parent);


    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.image_SelectFromCamera:

                imageUploadingButton.setClickable(true);
                if (imageUploadingButton.getText().toString().equalsIgnoreCase("UPLOADED"))
                    imageUploadingButton.setText("UPLOAD");
                clickingPicturesFromCamera();
                break;


            case R.id.tv_SelectFromPhotos:

                imageUploadingButton.setClickable(true);
                if (imageUploadingButton.getText().toString().equalsIgnoreCase("UPLOADED"))
                    imageUploadingButton.setText("UPLOAD");
                buildingTheGallery();
                break;


            case R.id.imageUploadingButton:


                if (Build.VERSION.SDK_INT >= 23) {

                    if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Log.v("", "Permission is granted");
                    } else {

                        Log.v("", "Permission is revoked");
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                } else { //permission is automatically granted on sdk<23 upon installation
                    Log.v("", "Permission is granted");
                }


                if (dialog == null) {
                    dialog = new SpotsDialog(mContext);
                    dialog.setCancelable(false);
                    dialog.show();


                } else dialog.show();


                if (imageUploadingButton.getText().toString().equalsIgnoreCase("UPLOAD")) {
                    imageUploadingButton.setText("UPLOADING...");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                newWay();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

                break;

            case R.id.tv_Cancel:
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.finish();
                startActivity(intent);
                finish();
                break;

        }
    }

    private void newWay() {


        File file = new File(filePath);

        RequestBody oId = RequestBody.create(MediaType.parse("text/plain"), orderID);
        //RequestBody rq6 = RequestBody.create(MediaType.parse("image/*"), file.getName());
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("myDocs", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
//        MultipartBody.Part filePart = MultipartBody.Part.createFormData(orderID,)

        HttpModule.provideRepositoryService().UpdateImageDocs(filePart, oId).enqueue(new Callback<ResponseUploadDocumnets>() {
            @Override
            public void onResponse(Call<ResponseUploadDocumnets> call, Response<ResponseUploadDocumnets> response) {

                if (response.toString() != null) {

                    if (response.body().getSuccess()) {

                        isClickable = true;
                        imageUploadingButton.setClickable(isClickable);
                        mProgressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                        dialog.dismiss();
                        if (imageUploadingButton.getText().toString().equalsIgnoreCase("UPLOADING..."))
                            imageUploadingButton.setText("UPLOADED");
                        imageUploadingButton.setClickable(false);

                        TastyToast.makeText(getApplicationContext(), response.body().getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                    } else {

                        TastyToast.makeText(getApplicationContext(), response.body().getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUploadDocumnets> call, Throwable t) {
                System.out.println("UploadImageActivity.onFailure - - " + t);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.deleteImage();
    }


    public void createLayout() {

        filepathName = new TextView(this);


        LinearLayout layout2 = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParamsmain = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout2.setLayoutParams(layoutParamsmain);
        layout2.setOrientation(LinearLayout.HORIZONTAL);

        filesizeName = new TextView(this);


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.START;
        layoutParams.weight = 1.0f;

        filesizeName.setLayoutParams(layoutParams);
        layout2.addView(filesizeName);


        image = new ImageView(this);
        image.setBackground(getResources().getDrawable(R.drawable.ic_action_delete));

        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.gravity = Gravity.END;
        layoutParams.weight = 1.0f;
        image.setLayoutParams(layoutParams2);
        layout2.addView(image);


        mProgressBar = new ProgressBar(UploadImageActivity.this, null, android.R.attr.progressBarStyleHorizontal);


        parent_layout.addView(filepathName);
        parent_layout.addView(layout2);
        parent_layout.addView(mProgressBar);


        parent_layout.refreshDrawableState();


    }

    @Override
    public void onBackPressed() {

        TastyToast.makeText(getApplicationContext(), "Please Upload your Documnets", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();


    }
}
