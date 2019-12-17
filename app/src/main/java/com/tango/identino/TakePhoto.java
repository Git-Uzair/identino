package com.tango.identino;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceImageLabelerOptions;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Mode;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;
import com.tango.identino.model.Single_Attendance_status;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class TakePhoto extends AppCompatActivity implements FrameProcessor {
    private Button exitButton;
    private ImageButton cameraButton;
    private CameraView cameraView;
    private ImageView imageView;
    private FirebaseAutoMLLocalModel localModel;
    private FirebaseVisionImageLabeler labeler;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String course_name;
    private Map<String, Integer> attendance = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        course_name = getIntent().getStringExtra("course_name");

        //Image labeler/Mobile detector model instances//
        FirebaseVisionOnDeviceImageLabelerOptions mobileDetectorOptions =
     new FirebaseVisionOnDeviceImageLabelerOptions.Builder()
         .setConfidenceThreshold(0.7f)
         .build();
 final FirebaseVisionImageLabeler objectLabeler = FirebaseVision.getInstance()
     .getOnDeviceImageLabeler(mobileDetectorOptions);

 ////Image labeler/Mobile detector model instances ends here//

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
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        final String Date = dateFormat.format(date).toString();

        exitButton = findViewById(R.id.exit_take_photo_activity);
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

                //start new intent

                Intent intent = new Intent(getApplicationContext(), Attendance_marking.class);
                intent.putExtra("attendance", (Serializable) attendance);
                intent.putExtra("course_name",course_name);
                startActivity(intent);


                //  finish();
            }
        });


        //fetching all students from the firebase database for given subject to local hashmap
        db.collection("courses").document(course_name).collection("students").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d("check", "onSuccess: "+"working");
                for (final QueryDocumentSnapshot snap : queryDocumentSnapshots) {

                    db.collection("courses").document(course_name).collection("students").document(snap.get("regno").toString()).collection("attendance").document(Date).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if(documentSnapshot.exists())
                            {
                                attendance.put(snap.get("regno").toString(),documentSnapshot.toObject(Single_Attendance_status.class).getStatus());
                            }
                            else
                            {
                                attendance.put(Objects.requireNonNull(snap.get("regno")).toString(), 0);
                            }

                        }
                    });



                }






            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

            }
        });


        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cameraView.isOpened()) {
                    Toast.makeText(getApplicationContext(), "Press Back Button to Take picture again", Toast.LENGTH_LONG).show();
                } else {
                    cameraView.takePicture();

                }

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
                //Detecting Mobile///

//                objectLabeler.processImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
//                    @Override
//                    public void onSuccess(List<FirebaseVisionImageLabel> firebaseVisionImageLabels) {
//                        for (FirebaseVisionImageLabel firebaseVisionImageLabel: firebaseVisionImageLabels)
//                        {
//                            Log.d("mobiledec", "onSuccess: "+firebaseVisionImageLabel.getText());
//                            if(firebaseVisionImageLabel.getText().equals("Mobile Phone"))
//                            {
//                                Toast.makeText(getApplicationContext(),"Mobile Phone detected",Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("mobiledec", "onSuccess: it sucks");
//
//                    }
//                });

                //Detecting Mobile end///


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
                                String reg = "";
                                for (FirebaseVisionImageLabel label : firebaseVisionImageLabels) {
                                    String text = label.getText();
                                    float confidence = label.getConfidence();
                                    if (confidence > max) {
                                        max = confidence;
                                        reg = text;
                                    }
                                }
                                Bitmap copy1 = bitmap1.copy(Bitmap.Config.ARGB_8888, true);
                                if (max < 0.4) {
                                    reg = "Unknown";

                                } else {
                                    //Attendance Marking here


                                    //Attendance Marking here


                                    //Mark Attendance from firebase here
                                    //course_name is from the intent extra
                                    //replace 2017494 with name variable when dataset is trained on reg_nos
                                    //replace document TEST with todays date
                                    final String finalReg = reg;
                                    db.collection("courses").document(course_name).collection("students").document(reg).collection("attendance").document(Date).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                if(documentSnapshot.toObject(Single_Attendance_status.class).getStatus()==0)
                                                {
                                                    attendance.put(finalReg,1);
                                                    Toast.makeText(getApplicationContext(), "Attendance for today updated for " + finalReg, Toast.LENGTH_LONG).show();
                                                }
                                                else
                                                {
                                                    Toast.makeText(getApplicationContext(), "Attendance for today already marked for " + finalReg, Toast.LENGTH_LONG).show();
                                                }



                                            } else {
                                                //mark attendance locally
                                                //save attendance to hashmap
                                                if (attendance.get(finalReg) == 1) {
                                                    Toast.makeText(getApplicationContext(), "Attendance for today already marked for " + finalReg, Toast.LENGTH_LONG).show();
                                                } else {
                                                    attendance.put(finalReg, 1);
                                                    Toast.makeText(getApplicationContext(), "Attendance Marked for " + finalReg, Toast.LENGTH_LONG).show();
                                                }


                                            }

                                        }
                                    });


                                }

                                detectFaces(firebaseVisionFaces, copy1, reg);
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


    private void detectFaces(List<FirebaseVisionFace> firebaseVisionFaces, Bitmap bitmap, String reg) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(20f);
        paint.setStyle(Paint.Style.STROKE);

        if (firebaseVisionFaces == null || bitmap == null) {
            Toast.makeText(TakePhoto.this, "errrororoor", Toast.LENGTH_LONG).show();
        }

        for (int i = 0; i < firebaseVisionFaces.size(); i++) {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawRect(firebaseVisionFaces.get(i).getBoundingBox(), paint);
            paint.setColor(Color.GREEN);
            paint.setTextSize(60f);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawText("Reg: " + reg, firebaseVisionFaces.get(i).getBoundingBox().exactCenterX(), firebaseVisionFaces.get(i).getBoundingBox().exactCenterY() - 500, paint);
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
            cameraView.takePicture();
        } else if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
            cameraView.open();
            imageView.setImageBitmap(null);
        }
        return true;
    }
}