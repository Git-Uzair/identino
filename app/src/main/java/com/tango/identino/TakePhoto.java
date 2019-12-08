package com.tango.identino;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Mode;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;

import java.util.List;


public class TakePhoto extends AppCompatActivity implements FrameProcessor {
    private Button cameraButton;
    private CameraView cameraView;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        cameraButton = findViewById(R.id.take_photo_camera_button);
        cameraView=findViewById(R.id.camera_view);
        cameraView.setFacing(Facing.FRONT);
        cameraView.setLifecycleOwner(TakePhoto.this);
        cameraView.addFrameProcessor(TakePhoto.this);
        imageView=findViewById(R.id.take_photo_image_view);
        cameraView.setMode(Mode.PICTURE);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraView.takePicture();

            }
        });

        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                super.onPictureTaken(result);
               byte[] bitmap=result.getData();
                final Bitmap bitmap1 = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
                imageView.setImageBitmap(bitmap1);

                if (bitmap1==null)
                {
                    Toast.makeText(TakePhoto.this,"hello ji",Toast.LENGTH_LONG).show();
                }
                FirebaseVisionImage firebaseVisionImage=FirebaseVisionImage.fromBitmap(bitmap1);
                FirebaseVisionFaceDetectorOptions options=new FirebaseVisionFaceDetectorOptions.Builder().setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS).build();
                FirebaseVisionFaceDetector firebaseVisionFaceDetector= FirebaseVision.getInstance().getVisionFaceDetector(options);

                firebaseVisionFaceDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                        Bitmap copy1=bitmap1.copy(Bitmap.Config.ARGB_8888,true);
                        detectFaces(firebaseVisionFaces,copy1);
                        imageView.setImageBitmap(copy1);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Fail", "onFailure: "+e.toString());
                    }
                });
            }
        });
    }
    private void detectFaces(List<FirebaseVisionFace> firebaseVisionFaces,Bitmap bitmap)
    {
        Paint paint=new Paint();
        paint.setColor(Color.GREEN);
        paint.setTextSize(3f);
        if(firebaseVisionFaces==null||bitmap==null)
        {
            Toast.makeText(TakePhoto.this,"errrororoor",Toast.LENGTH_LONG).show();
        }

        for (int i=0;i<firebaseVisionFaces.size();i++)
        {
            Canvas canvas=new Canvas(bitmap);
            canvas.drawRect(firebaseVisionFaces.get(i).getBoundingBox(),paint);
        }

    }

    @Override
    public void process(@NonNull Frame frame) {
        int width=frame.getSize().getWidth();
        int height=frame.getSize().getHeight();
        FirebaseVisionImageMetadata metadata=new FirebaseVisionImageMetadata.Builder().setWidth(width).setHeight(height)
                                                 .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                                                 .setRotation(FirebaseVisionImageMetadata.ROTATION_270).build();

    }
}