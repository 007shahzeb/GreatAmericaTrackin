package android.com.activity;
// https://stackoverflow.com/questions/22874735/upload-large-file-with-progress-bar-and-without-outofmemory-error-in-android

import android.com.CustomMultiPartEntity;
import android.com.ProgressRequestBody;
import android.com.garytransportnew.R;
import android.com.net.HttpModule;
import android.com.responseModel.ResponseUploadDocumnets;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mindorks.paracamera.Camera;
import com.sdsmdg.tastytoast.TastyToast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

import dmax.dialog.SpotsDialog;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadImageActivity extends AppCompatActivity implements View.OnClickListener {

    //, ProgressRequestBody.UploadCallbacks

    private ImageView image_Camera, test;
    private TextView tv_SelectFromPhotos, tv_transport_docFile, tv_MBTextview;
    private static int RESULT_LOAD_IMAGE = 2;
    ProgressBar mProgressBar;
    Handler handler = new Handler();

    private TextView showingProgressiveValue, imageUploadingButton, tv_Cancel;
    public Camera camera;
    public Uri selectedImage;
    public Bitmap bitmap;
    public RequestBody requestFile;
    String filePath = "";
    int file_size, file_size_gallery;

    MultipartBody.Part filePart;
    ProgressRequestBody fileBody;
//    int size;


    // New Implementations

    private final String filename = "path.substring(path.lastIndexOf(\"/\") + 1)";
    // private final String filename = "/mnt/sdcard/a.3gp";
    private String urlString = "http://34.234.186.44:4000/driverapp/sendDocument";
    private TextView tv;
    long totalSize = 0;
    private long lastClickTime = 0;
    private Context mContext;
    private SpotsDialog dialog = null;


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
        mContext = this;

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

//                fileBody = new ProgressRequestBody(file, this);
//                filePart = MultipartBody.Part.createFormData("image", file.getName(), fileBody);
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

//            fileBody = new ProgressRequestBody(file, this);
//            filePart = MultipartBody.Part.createFormData("image", file.getName(), fileBody);
            System.out.println("UploadImageActivity.onActivityResult - - -" + filePart);

//            size = file_size_gallery;
        }
    }


    private void listener() {
        image_Camera.setOnClickListener(this);
        tv_SelectFromPhotos.setOnClickListener(this);
        imageUploadingButton.setOnClickListener(this);
        tv_Cancel.setOnClickListener(this);
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
        tv_Cancel = findViewById(R.id.tv_Cancel);

    }

    //    @RequiresApi(api = Build.VERSION_CODES.M) // adding this annotations to make api level comfortable
    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.image_Camera: {
                mProgressBar.getProgressDrawable().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                clickingPicturesFromCamera();
                break;
            }

            case R.id.tv_SelectFromPhotos: {
                mProgressBar.getProgressDrawable().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
//                mProgressBar.setProgressTintList(ColorStateList.valueOf(getColor(R.color.lightgray)));
                buildingTheGallery();
                break;
            }

            case R.id.imageUploadingButton: {
//                imageUploadingButton.setClickable(false);


//                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
//                    return;
//                }
//                lastClickTime = SystemClock.elapsedRealtime();

                System.out.println("UploadImageActivity.onClick - - -12345 ");
//                TastyToast.makeText(getApplicationContext(), "Processing...", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
//
                if (dialog == null) {
                    dialog = new SpotsDialog(mContext);
                    dialog.setCancelable(false);
                    dialog.show();


                } else dialog.show();
//

                uploadingDocumentsAPICalling(v);
                break;
            }

            case R.id.tv_Cancel: {
//                mProgressBar.setProgress(0);
                System.out.println("UploadImageActivity.onClick");
                TastyToast.makeText(getApplicationContext(), "Cancelling The Docs ", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
            }

        }


    }

    private void uploadingDocumentsAPICalling(View v) {

        new Uploadtask().execute();
        System.out.println("UploadImageActivity.uploadingDocumentsAPICalling");


//        HttpModule.provideRepositoryService().getUploadDocumnetsAPI("10", filePart).enqueue(new Callback<ResponseUploadDocumnets>() {
//            @Override
//            public void onResponse(Call<ResponseUploadDocumnets> call, Response<ResponseUploadDocumnets> response) {
//
//                System.out.println("UploadImageActivity.onResponse");
//
//                if (response.body().isSuccess) {
//                    mProgressBar.setIndeterminate(false);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseUploadDocumnets> call, Throwable t) {
//                System.out.println("UploadImageActivity.onFailure " + t);
//            }
//        });
    }

    // The bitmap is saved in the app's folder
//  If the saved bitmap is not required use following code
    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.deleteImage();
    }


//    @Override
//    public void onProgressUpdate(int percentage) {
//        System.out.println("UploadImageActivity.onProgressUpdate" + percentage);
//        mProgressBar.setProgress(percentage);
//    }
//
//    @Override
//    public void onError() {
//        System.out.println("UploadImageActivity.onError");
//
//    }
//
//    @Override
//    public void onFinish() {
//        mProgressBar.setProgress(100);
//        System.out.println("UploadImageActivity.onFinish");
//
//    }


    private class Uploadtask extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setProgress(0);
//            tv.setText("shuru");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            System.out.println("Uploadtask.onProgressUpdate - - - PROGRESS IS " + progress.toString());
            mProgressBar.setProgress(progress[0]);
        }

        @Override
        protected String doInBackground(Void... params) {
            System.out.println("Uploadtask.doInBackground - - " + params);
            return upload();
        }

        private String upload() {
            String responseString = "no";

            File sourceFile = new File(filePath);
            if (!sourceFile.isFile()) {
                return "not a file";
            }
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urlString);


            try {
                CustomMultiPartEntity entity = new CustomMultiPartEntity(new CustomMultiPartEntity.ProgressListener() {

                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                });

                entity.addPart("type", new StringBody("image"));
                entity.addPart("uploadedfile", new FileBody(sourceFile));
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                responseString = EntityUtils.toString(r_entity);

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }


        @Override
        protected void onPostExecute(String result) {
            System.out.println("Uploadtask.onPostExecute - - -" + result);
//            imageUploadingButton.setClickable(true);
//            tv.setText(result);
            mProgressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
            dialog.dismiss();
            TastyToast.makeText(getApplicationContext(), "Document Uploaded Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
            super.onPostExecute(result);

        }

    }

}
