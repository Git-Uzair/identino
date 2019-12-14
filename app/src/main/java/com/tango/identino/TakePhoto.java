package com.tango.identino;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Mode;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;
import java.util.List;



public class TakePhoto extends AppCompatActivity implements FrameProcessor {
    private Button cameraButton,exitButton;
    private CameraView cameraView;
    private ImageView imageView;
    private FirebaseAutoMLLocalModel localModel;
    private FirebaseVisionImageLabeler labeler;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String course_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        course_name = getIntent().getStringExtra("course_name");
        localModel = new FirebaseAutoMLLocalModel.Builder()
                .setAssetFilePath("manifest.json")
                .build();
        labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler();
        try {
            FirebaseVisionOnDeviceAutoMLImageLabelerOptions options =
                    new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder(localModel)
                            .setConfidenceThreshold(0.0f)  // Evaluate your model in the Firebase console
                            // to determine an appropriate value.
                            .build();
            labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options);
        } catch (FirebaseMLException e) {
            // ...
        }

        exitButton=findViewById(R.id.exit_take_photo_activity);
        cameraButton = findViewById(R.id.take_photo_camera_button);
        cameraView = findViewById(R.id.camera_view);
        cameraView.setFacing(Facing.FRONT);
        cameraView.setLifecycleOwner(TakePhoto.this);
        cameraView.addFrameProcessor(TakePhoto.this);
        imageView = findViewById(R.id.take_photo_image_view);
        cameraView.setMode(Mode.PICTURE);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageBitmap(null);
                cameraView.open();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        cameraView.takePicture();
                    }
                }, 300);


            }
        });

        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                super.onPictureTaken(result);
                byte[] bitmap = result.getData();
                final Bitmap bitmap1 = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
                imageView.setImageBitmap(bitmap1);
                cameraView.close();

                if (bitmap1 == null) {
                    Toast.makeText(TakePhoto.this, "hello ji", Toast.LENGTH_LONG).show();
                }
                FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap1);
                FirebaseVisionFaceDetectorOptions options = new FirebaseVisionFaceDetectorOptions.Builder().setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS).build();
                FirebaseVisionFaceDetector firebaseVisionFaceDetector = FirebaseVision.getInstance().getVisionFaceDetector(options);
                firebaseVisionFaceDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
                    @Override
                    public void onSuccess(final List<FirebaseVisionFace> firebaseVisionFaces) {

                        if (firebaseVisionFaces.size() > 1) {
                            Toast.makeText(getApplicationContext(), "Error: More than one individual in the frame", Toast.LENGTH_LONG).show();
                            return;
                        }

                        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap1);
                        labeler.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                            @Override
                            public void onSuccess(List<FirebaseVisionImageLabel> firebaseVisionImageLabels) {
                                float max = 0;
                                String name = "";
                                for (FirebaseVisionImageLabel label : firebaseVisionImageLabels) {
                                    String text = label.getText();
                                    float confidence = label.getConfidence();
                                    if (confidence > max) {
                                        max = confidence;
                                        name = text;
                                    }
                                }
                                Bitmap copy1 = bitmap1.copy(Bitmap.Config.ARGB_8888, true);
                                if (max < 0.4) {
                                    name = "Unknown";

                                } else {
                                    //Attendance Marking here
                                }

                                detectFaces(firebaseVisionFaces, copy1, name);
                                imageView.setImageBitmap(copy1);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Fail", "onFailure: " + e.toString());
                    }
                });
            }
        });
    }


    private void detectFaces(List<FirebaseVisionFace> firebaseVisionFaces, Bitmap bitmap, String name) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(20f);
        paint.setStyle(Paint.Style.STROKE);
        // paint.setStyle(Paint.Style.FILL_AND_STROKE);

        if (firebaseVisionFaces == null || bitmap == null) {
            Toast.makeText(TakePhoto.this, "errrororoor", Toast.LENGTH_LONG).show();
        }

        for (int i = 0; i < firebaseVisionFaces.size(); i++) {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawRect(firebaseVisionFaces.get(i).getBoundingBox(), paint);
            paint.setColor(Color.GREEN);
            paint.setTextSize(60f);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawText("Reg: " + name, firebaseVisionFaces.get(i).getBoundingBox().exactCenterX(), firebaseVisionFaces.get(i).getBoundingBox().exactCenterY() - 500, paint);
        }

    }

    @Override
    public void process(@NonNull Frame frame) {
        int width = frame.getSize().getWidth();
        int height = frame.getSize().getHeight();
        FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder().setWidth(width).setHeight(height)
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                .setRotation(FirebaseVisionImageMetadata.ROTATION_270).build();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            //Toast.makeText(TakePhoto.this,"Volume up presses",Toast.LENGTH_LONG).show();
            cameraView.takePicture();
        } else if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
            onBackPressed();
            imageView.setImageBitmap(null);
        }
        return true;
    }

//    boolean doubleBackToExitPressedOnce = false;
//
//    @Override
//    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//               // Intent intent=new Intent(TakePhoto.this,Post_login.class);
//                //startActivity(intent);
//                finish();
//            }
//        }, 2000);
//    }
}