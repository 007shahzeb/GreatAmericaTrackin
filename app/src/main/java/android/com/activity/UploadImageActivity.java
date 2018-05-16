package android.com.activity;

import android.com.ProgressRequestBody;
import android.com.garytransportnew.R;
import android.com.net.HttpModule;
import android.com.responseModel.ResponseUploadDocumnets;
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

import com.mindorks.paracamera.Camera;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadImageActivity extends AppCompatActivity implements View.OnClickListener, ProgressRequestBody.UploadCallbacks {

    private ImageView image_Camera, test;
    private TextView tv_SelectFromPhotos, tv_transport_docFile, tv_MBTextview;
    private static int RESULT_LOAD_IMAGE = 2;
    ProgressBar mProgressBar;
    Handler handler = new Handler();

    private TextView showingProgressiveValue, imageUploadingButton;
    public Camera camera;
    public Uri selectedImage;
    public Bitmap bitmap;
    public RequestBody requestFile;
    String filePath = "";
    int file_size, file_size_gallery;

    MultipartBody.Part filePart;
    ProgressRequestBody fileBody;
//    int size;

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
//                            mProgressBar.setProgress(progressBarValue);
////                            showingProgressiveValue.setText(progressBarValue + "/" + mProgressBar.getMax()); // Shwoing Progressive Value
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
            bitmap = camera.getCameraBitmap();
            if (bitmap != null) {
                filePath = camera.getCameraBitmapPath();
                File file = new File(filePath);
                file_size = Integer.parseInt(String.valueOf(file.length() / 1024)); // Getting the file size here in MB

                String path = filePath;//it contain your path of image..im using a temp string..
                String filename = path.substring(path.lastIndexOf("/") + 1); // Getting the file name here

                tv_transport_docFile.setText(filename);
                tv_MBTextview.setText(String.valueOf(file_size) + " KB");

                fileBody = new ProgressRequestBody(file, this);
                filePart = MultipartBody.Part.createFormData("image", file.getName(), fileBody);
                System.out.println("UploadImageActivity.onActivityResult - - -" + filePart);
//                mProgressBar.setMax(100);
//                size = file_size;

            } else {
//                Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            System.out.println("UploadImageActivity.onActivityResult = = = else case ");
            selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            File file = new File(filePath);
            file_size_gallery = Integer.parseInt(String.valueOf(file.length() / 1024));
//            mProgressBar.setIndeterminate(true);

            String path = filePath;//it contain your path of image..im using a temp string..
            String filename_gallery = path.substring(path.lastIndexOf("/") + 1);
            System.out.println("UploadImageActivity.onActivityResult - - " + filename_gallery);
            tv_transport_docFile.setText(filename_gallery);
            tv_MBTextview.setText(String.valueOf(file_size_gallery) + " KB");
            cursor.close();

            fileBody = new ProgressRequestBody(file, this);
            filePart = MultipartBody.Part.createFormData("image", file.getName(), fileBody);
            System.out.println("UploadImageActivity.onActivityResult - - -" + filePart);

//            size = file_size_gallery;
        }
    }


    private void listener() {
        image_Camera.setOnClickListener(this);
        tv_SelectFromPhotos.setOnClickListener(this);
        imageUploadingButton.setOnClickListener(this);
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
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        tv_MBTextview = findViewById(R.id.tv_MBTextview);
        tv_transport_docFile = findViewById(R.id.tv_transport_docFile);
        test = findViewById(R.id.test);
        imageUploadingButton = findViewById(R.id.imageUploadingButton);

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.image_Camera: {
                clickingPicturesFromCamera();
                break;
            }

            case R.id.tv_SelectFromPhotos: {
                buildingTheGallery();
                break;
            }

            case R.id.imageUploadingButton: {
                mProgressBar.setIndeterminate(true);
                TastyToast.makeText(getApplicationContext(), "Document Uploaded Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                finish();
//                uploadingDocumentsAPICalling();
                break;
            }
        }


    }

    private void uploadingDocumentsAPICalling() {
        System.out.println("UploadImageActivity.uploadingDocumentsAPICalling");
        HttpModule.provideRepositoryService().getUploadDocumnetsAPI("10", filePart).enqueue(new Callback<ResponseUploadDocumnets>() {
            @Override
            public void onResponse(Call<ResponseUploadDocumnets> call, Response<ResponseUploadDocumnets> response) {

                System.out.println("UploadImageActivity.onResponse");

                if (response.body().isSuccess) {
                    mProgressBar.setIndeterminate(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseUploadDocumnets> call, Throwable t) {
                System.out.println("UploadImageActivity.onFailure " + t);
            }
        });
    }

    // The bitmap is saved in the app's folder
//  If the saved bitmap is not required use following code
    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.deleteImage();
    }


    @Override
    public void onProgressUpdate(int percentage) {
        System.out.println("UploadImageActivity.onProgressUpdate" + percentage);
        mProgressBar.setProgress(percentage);
    }

    @Override
    public void onError() {
        System.out.println("UploadImageActivity.onError");

    }

    @Override
    public void onFinish() {
        mProgressBar.setProgress(100);
        System.out.println("UploadImageActivity.onFinish");

    }
}
