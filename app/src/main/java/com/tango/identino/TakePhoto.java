package com.tango.identino;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Mode;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;


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
                Bitmap bitmap1 = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
                imageView.setImageBitmap(bitmap1);
            }
        });
    }

    @Override
    public void process(@NonNull Frame frame) {

    }
}