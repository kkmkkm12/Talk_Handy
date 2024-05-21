package com.example.final_test;

import static com.example.final_test.MyDatabaseHelper.COLUMN_ID;
import static com.example.final_test.MyDatabaseHelper.COLUMN_NAME;
import static com.example.final_test.MyDatabaseHelper.COLUMN_PATH;
import static com.example.final_test.MyDatabaseHelper.TABLE_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class StudyActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private PreviewView previewView;
    private TextView select_study;
    private ImageView studyImage;

    private MyDatabaseHelper dbHelper;
    private static final String DB_NAME = "MyDB.db";
    private static final int DB_VERSION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        select_study = findViewById(R.id.select_study);
        previewView = findViewById(R.id.previewView);
        studyImage = findViewById(R.id.studyImage);

        dbHelper = new MyDatabaseHelper(this, DB_NAME, null, DB_VERSION);





        String selectedItem = getIntent().getStringExtra("selectedItem");
        select_study.setText("지문자 " + selectedItem);

        Cursor cursor = selectReadDB(selectedItem);
        if (cursor != null && cursor.moveToFirst()) {
            int pathIndex = cursor.getColumnIndex(COLUMN_PATH);
            String path = cursor.getString(pathIndex);

            String imageName = path;
            int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            if (resId != 0) {
                studyImage.setImageResource(resId);
            }
        } else {
            // Cursor가 빈 결과를 반환했을 때 처리할 코드
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            startCamera();
        }

        //if로 selectedItem == 모델의 결과 값이 같다면 옳바른 손모양 입니다. 틀리면 옳바르지 않은 지문자 입니다. 출력


    }
    private Cursor selectReadDB(String a) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] from = {COLUMN_PATH,};
        String selection = COLUMN_NAME + " = ?";
        String[] selectionArgs = {a};
        Cursor cursor = db.query(TABLE_NAME, from, selection, selectionArgs, null, null, COLUMN_ID + " ASC");
        return cursor;
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // Handle any errors (including cancellation) here.
            }
        }, ContextCompat.getMainExecutor(this));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle(this, cameraSelector, preview);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }
}