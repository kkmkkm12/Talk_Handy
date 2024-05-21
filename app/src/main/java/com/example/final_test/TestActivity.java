package com.example.final_test;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.concurrent.ExecutionException;

public class TestActivity extends AppCompatActivity {
    private String lang[] = {"ㄱ", "ㄴ", "ㄷ", "ㄹ", "ㅁ", "ㅂ", "ㅅ", "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"
                            , "ㅏ", "ㅑ", "ㅓ", "ㅕ", "ㅗ", "ㅛ", "ㅜ", "ㅠ", "ㅡ", "ㅣ", "ㅐ", "ㅒ", "ㅔ", "ㅖ", "ㅚ", "ㅟ", "ㅢ"};
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private PreviewView previewView;
    private TextView question_text;
    private Button correct_btn;
    private Thread thread1;
    private ProgressBar progress;
    private Dialog dialog;
    int time_count = 100;
    boolean thread1_state = false;
    boolean dialog_state = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        previewView = findViewById(R.id.previewView);
        correct_btn = findViewById(R.id.correct_btn);
        progress = findViewById(R.id.progress);
        question_text = findViewById(R.id.question_text);

        Random random = new Random();

        question_text.setText(lang[random.nextInt(31)]);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            startCamera();
        }

        correct_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(TestActivity.this);
                dialog.setContentView(R.layout.activity_result);
                thread1.interrupt();

                Window window = dialog.getWindow();

                if (window != null) {
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(window.getAttributes());
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    window.setAttributes(layoutParams);
                }
                dialog.show();
                dialog_state = true;
                TextView result_text = dialog.findViewById(R.id.result_text);
                Button next_retry_btn = dialog.findViewById(R.id.next_retry_btn);

                result_text.setText("정답입니다.");
                next_retry_btn.setText("다음");
                next_retry_btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        question_text.setText(lang[random.nextInt(31)]);

                        time_count = 100;
                        thread_manager();

                        dialog.dismiss();
                        dialog_state = false;
                    }
                });
            }
        });


        thread_manager();
    }

    private void thread_manager(){
        thread1 = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        sleep(100);
                        time_count -= 1;
                        if(time_count == 0){
                            thread1_state = true;
                        }
                        Log.i("test", "thread running...");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!isInterrupted()) {
                                    if(thread1_state){
                                        dialog = new Dialog(TestActivity.this);
                                        dialog.setContentView(R.layout.activity_result);
                                        thread1.interrupt();

                                        Window window = dialog.getWindow();

                                        if (window != null) {
                                            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                                            layoutParams.copyFrom(window.getAttributes());
                                            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                                            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                            window.setAttributes(layoutParams);
                                        }
                                        dialog.show();

                                        TextView result_text = dialog.findViewById(R.id.result_text);
                                        Button next_retry_btn = dialog.findViewById(R.id.next_retry_btn);

                                        result_text.setText("시간 초과...");
                                        next_retry_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                time_count = 100;

                                                thread_manager();

                                                thread1_state = false;

                                                dialog.dismiss();
                                            }
                                        });
                                    }
                                    progress.setProgress(time_count % 301 + 1);
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    // 인터럽트가 발생하면 스레드를 종료합니다.
                    Thread.currentThread().interrupt();
                }
            }
        };
        thread1.start();
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