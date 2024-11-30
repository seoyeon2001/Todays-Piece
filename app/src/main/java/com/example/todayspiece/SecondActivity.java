package com.example.todayspiece;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.todayspiece.database.CalendarEntry;
import com.example.todayspiece.database.DatabaseManager;

public class SecondActivity extends Activity {

    private EditText dairytitle; // EditText for diary title
    private EditText dairytxt; // EditText for diary content
    private ImageButton dairyimage; // ImageButton for image selection
    private TextView selectedDateTextView; // TextView for displaying the selected date
    private DatabaseManager databaseManager; // DatabaseManager instance
    private Bitmap selectedImageBitmap; // To store selected image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        // Initialize DatabaseManager
        databaseManager = new DatabaseManager(this);

        // Bind views
        selectedDateTextView = findViewById(R.id.selectedDateTextView);
        dairytitle = findViewById(R.id.dateEditText);
        dairytxt = findViewById(R.id.diaryEditText);
        dairyimage = findViewById(R.id.dateImagebtn);

        // Retrieve and display the selected date
        Intent intent = getIntent();
        String selectedDate = intent.getStringExtra("selectedDate");
        if (selectedDate != null) {
            selectedDateTextView.setText(selectedDate);
            loadEntryFromDatabase(selectedDate); // Load data from database if available
        }

        // ImageButton to open gallery
        dairyimage.setOnClickListener(view -> openGallery());

        // Save button logic
        Button savebtn = findViewById(R.id.savebtn);
        savebtn.setOnClickListener(view -> {
            String title = dairytitle.getText().toString().trim();
            String content = dairytxt.getText().toString().trim();

            if (selectedDate != null && !title.isEmpty() && !content.isEmpty()) {
                // Save data to the database
                databaseManager.insertEntry(selectedDate, selectedImageBitmap, title, content);
            }
        });

        // Exit button logic
        Button exitbtn = findViewById(R.id.exitbtn);
        exitbtn.setOnClickListener(view -> finish());
    }

    /**
     * Load data from the database and populate the UI.
     *
     * @param date The primary key (date) to search for in the database.
     */
    private void loadEntryFromDatabase(String date) {
        // Retrieve the entry from the database
        CalendarEntry entry = databaseManager.getEntry(date);

        // If an entry is found, populate the UI components
        if (entry != null) {
            dairytitle.setText(entry.getTitle()); // Set title
            dairytxt.setText(entry.getDetails()); // Set details

            // Set the image if it exists
            Bitmap image = entry.getImage();
            if (image != null) {
                dairyimage.setImageBitmap(image);
            }
        } else {
            // Handle the case where no data is found (e.g., show a placeholder)
            dairytitle.setText(""); // Clear title
            dairytxt.setText(""); // Clear details
            dairyimage.setImageResource(R.drawable.ic_launcher_background); // Set a placeholder image
        }
    }

    /**
     * Open the gallery to select an image for the diary.
     */
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 1); // Request code for gallery
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Get the image URI from the gallery
            selectedImageBitmap = data.getParcelableExtra("data");
            if (selectedImageBitmap != null) {
                dairyimage.setImageBitmap(selectedImageBitmap); // Set image to ImageButton
            }
        }
    }
}
