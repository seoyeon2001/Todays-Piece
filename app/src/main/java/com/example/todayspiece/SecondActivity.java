package com.example.todayspiece;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.IOException;

public class SecondActivity extends Activity {

    private static final int PICK_IMAGE_REQUEST = 1; // Request code for gallery intent
    private EditText dairytitle; // edittext 입력한 값
    private EditText dairytxt; // 내용 입력한 값
    private ImageButton dairyimage; // 이미지 선택한 값

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        // Bind views
        dairytitle = findViewById(R.id.dateEditText);
        dairytxt = findViewById(R.id.diaryEditText);
        dairyimage = findViewById(R.id.dateImagebtn);

        // Placeholder for fetching data from the database
        //dairytitle.setText(); // Fetch title from DB
       //dairytxt.setText(""); // Fetch content from DB
        //dairyimage.setImageBitmap(); // Fetch image from DB if needed

        // ImageButton to open gallery
        dairyimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        // Save button logic
        Button savebtn = findViewById(R.id.savebtn);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = dairytitle.getText().toString().trim();
                String dairycontent = dairytxt.getText().toString().trim();

                // Save title, content to the database
            }
        });

        // Exit button logic
        Button exitbtn = findViewById(R.id.exitbtn);
        exitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Method to open the gallery
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*"); // Set type to only images
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData(); // Get image URI

            try {
                // Convert URI to Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                // Set the selected image as the ImageButton's image
                dairyimage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
