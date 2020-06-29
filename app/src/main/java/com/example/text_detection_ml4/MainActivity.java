package com.example.text_detection_ml4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView image;
    Button click,detect;
    Bitmap image_rec;
    private int CAMERA_REQUEST=100;
    FirebaseVisionTextRecognizer detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init of widgets
        image=findViewById(R.id.image);
        click=findViewById(R.id.click);
        detect=findViewById(R.id.detect);
        detector= FirebaseVision.getInstance().getOnDeviceTextRecognizer();

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,CAMERA_REQUEST);
            }
        });

        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectText(image_rec);
            }
        });


    }//on create
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==CAMERA_REQUEST){

            Bundle extra=data.getExtras();
            image_rec=(Bitmap) extra.get("data");
            image.setImageBitmap(image_rec);
        }


    }//onActivityresult

    public void detectText(Bitmap b) {
        //firebase image format
        FirebaseVisionImage f_image = FirebaseVisionImage.fromBitmap(b);


        detector.processImage(f_image).addOnCompleteListener(new OnCompleteListener<FirebaseVisionText>() {
            @Override
            public void onComplete(@NonNull Task<FirebaseVisionText> task) {
                List<FirebaseVisionText.TextBlock> blocks = task.getResult().getTextBlocks();
                String d = "";
                for (int i = 0; i < blocks.size(); i++) {
                    List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
                    for (int j = 0; j < lines.size(); j++) {
                        String k = lines.get(j).getText();
                        d += k + "\n";

                    }

                    d += "\n";
                }
                Toast.makeText(MainActivity.this,""+d,Toast.LENGTH_LONG).show();
            }
        });












    }

}//Main activity
