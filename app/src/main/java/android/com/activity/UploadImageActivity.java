package android.com.activity;

import android.com.garytransportnew.R;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mindorks.paracamera.Camera;

import java.io.ByteArrayOutputStream;

public class UploadImageActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView image_Camera , test ;
    private TextView tv_SelectFromPhotos , tv_transport_docFile , tv_MBTextview;
    private static int RESULT_LOAD_IMAGE = 1;
    ProgressBar Progressbar;
    Handler handler = new Handler();

    private TextView showingProgressiveValue;

    // Create global camera reference in an activity or fragment
    Camera camera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image_layout_contraint);


        findingIdsHere();
        listener();
        buildingTheCamera();
//        progressing();



        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

//    private void progressing() {
//
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                while (progressBarValue < 100) {
//                    progressBarValue++;
//
//                    handler.post(new Runnable() {
//
//                        @Override
//                        public void run() {
//
//                            Progressbar.setProgress(progressBarValue);
////                            showingProgressiveValue.setText(progressBarValue + "/" + Progressbar.getMax()); // Shwoing Progressive Value
//
//                        }
//                    });
//                    try {
//                        Thread.sleep(300);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
//    }



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
                .setName("ali_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
    }


    // Get the bitmap and image path onActivityResult of an activity or fragment
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
            Bitmap bitmap = camera.getCameraBitmap();
            if (bitmap != null) {

//                test.setImageBitmap(bitmap); // Getting The Bitmap here to save it and send it to server

            } else {
                Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            System.out.println("UploadImageActivity.onActivityResult Testing ");
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            System.out.println("UploadImageActivity.onActivityResult 0 - - -  picture path " + picturePath);
            cursor.close();

//            ImageView imageView = (ImageView) findViewById(R.id.imgView);
//            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }



    private void listener() {
        image_Camera.setOnClickListener(this);
        tv_SelectFromPhotos.setOnClickListener(this);
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

        image_Camera = findViewById(R.id.image_Camera);
        tv_SelectFromPhotos = findViewById(R.id.tv_SelectFromPhotos);
        Progressbar = (ProgressBar) findViewById(R.id.progressBar1);
        tv_MBTextview = findViewById(R.id.tv_MBTextview);
        tv_transport_docFile = findViewById(R.id.tv_transport_docFile);
        test = findViewById(R.id.test);

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.image_Camera: {
                clickingPicturesFromCamera();
            }

            case R.id.tv_SelectFromPhotos: {
//                takingPicturesFromGaleery();
                buildingTheGallery();
            }
        }


    }

    // The bitmap is saved in the app's folder
//  If the saved bitmap is not required use following code
    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.deleteImage();
    }


}
