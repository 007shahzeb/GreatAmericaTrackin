package android.com.activity;
// https://stackoverflow.com/questions/22874735/upload-large-file-with-progress-bar-and-without-outofmemory-error-in-android

import android.com.CustomMultiPartEntity;
import android.com.garytransportnew.R;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class UploadImageActivity extends AppCompatActivity implements View.OnClickListener {

    //, ProgressRequestBody.UploadCallbacks

    private ImageView image_SelectFromCamera;
    private TextView tv_SelectFromPhotos, tv_transport_docFile, tv_MBTextview;
    private static int RESULT_LOAD_IMAGE = 2;
    //    ProgressBar mProgressBar;
    Handler handler = new Handler();

    private TextView showingProgressiveValue, imageUploadingButton, tv_Cancel;
    public Camera camera;
    public Uri selectedImage;
    public Bitmap bitmap;
    String filePath = "";
    int file_size, file_size_gallery;

    //    int size;
    private static long back_pressed;
    String filename_gallery;


    // New Implementations

    private final String filename = "path.substring(path.lastIndexOf(\"/\") + 1)";
    // private final String filename = "/mnt/sdcard/a.3gp";
    private String urlString = "http://34.234.186.44:4000/driverapp/sendDocument";
    private TextView tv;
    long totalSize = 0;
    private long lastClickTime = 0;
    private Context mContext;
    private SpotsDialog dialog = null;

    private boolean isClickable = false;

    TextView row_fileNameTV;
    TextView row_fileSizeTV;
    ImageView row_deleteImage;
    ProgressBar row_progressbar;
    LinearLayout linear_parent;
    LinearLayout parent_layout;
    ProgressBar mProgressBar;
    private TextView filepathName;
    private TextView filesizeName;
    private ImageView image;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image_layout_contraint);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        findingIdsHere();
        listener();
        buildingTheCamera();
        mContext = this;

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


    // Get the bitmap and image path onActivityResult of an activity or fragment
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

//            size = file_size_gallery;
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
//                mProgressBar.getProgressDrawable().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                if (imageUploadingButton.getText().toString().equalsIgnoreCase("UPLOADED"))
                    imageUploadingButton.setText("UPLOAD");
                clickingPicturesFromCamera();
                break;


            case R.id.tv_SelectFromPhotos:

                imageUploadingButton.setClickable(true);
//                mProgressBar.getProgressDrawable().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                if (imageUploadingButton.getText().toString().equalsIgnoreCase("UPLOADED"))
                    imageUploadingButton.setText("UPLOAD");
                buildingTheGallery();
                break;


            case R.id.imageUploadingButton:

                if (dialog == null) {
                    dialog = new SpotsDialog(mContext);
                    dialog.setCancelable(false);
                    dialog.show();

                } else dialog.show();

                if (imageUploadingButton.getText().toString().equalsIgnoreCase("UPLOAD"))
                    imageUploadingButton.setText("UPLOADING...");

//                addindLayout();


                uploadingDocumentsAPICalling(v);
                break;


            case R.id.tv_Cancel:
//
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.finish();
                startActivity(intent);
                finish();
                break;

        }
    }

    private void addindLayout() {
        View child = getLayoutInflater().inflate(R.layout.progressbar_row, null);
        linear_parent.addView(child);

    }

    private void uploadingDocumentsAPICalling(View v) {
        new Uploadtask().execute();
    }

    // The bitmap is saved in the app's folder
//  If the saved bitmap is not required use following code
    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.deleteImage();
    }


    private class Uploadtask extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            mProgressBar.setProgress(progress[0]);
        }

        @Override
        protected String doInBackground(Void... params) {
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
            isClickable = true;
            imageUploadingButton.setClickable(isClickable);
            mProgressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
            dialog.dismiss();
            if (imageUploadingButton.getText().toString().equalsIgnoreCase("UPLOADING..."))
                imageUploadingButton.setText("UPLOADED");
            imageUploadingButton.setClickable(false);

            TastyToast.makeText(getApplicationContext(), "Document Uploaded Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
            super.onPostExecute(result);

        }

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
