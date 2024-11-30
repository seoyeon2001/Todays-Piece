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
import android.widget.Toast;

import com.example.todayspiece.database.CalendarEntry;
import com.example.todayspiece.database.DatabaseHelper;
import com.example.todayspiece.database.DatabaseManager;

import java.io.IOException;

public class SecondActivity extends Activity {

    private static final int PICK_IMAGE_REQUEST = 1; // Request code for gallery intent
    private EditText dairytitle; // 제목 입력
    private EditText dairytxt; // 내용 입력
    private ImageButton dairyimage; // 이미지 선택
    private Bitmap selectedBitmap = null; // 선택된 이미지

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        // View 초기화
        dairytitle = findViewById(R.id.dateEditText);
        dairytxt = findViewById(R.id.diaryEditText);
        dairyimage = findViewById(R.id.dateImagebtn);

        // Intent로부터 선택된 날짜 가져오기
        Intent intent = getIntent();
        String selectedDate = intent.getStringExtra("selectedDate");

        // 데이터베이스 매니저 초기화
        DatabaseManager databaseManager = new DatabaseManager(this);

        // 기존 데이터 로드
        CalendarEntry entry = databaseManager.getEntry(DatabaseHelper.convertStringToLocalDate(selectedDate));
        if (entry != null) {
            dairytitle.setText(entry.getTitle());
            dairytxt.setText(entry.getDetails());
            if (entry.getImage() != null) {
                dairyimage.setImageBitmap(entry.getImage());
                selectedBitmap = entry.getImage();
            }
        }

        // 갤러리 열기 버튼
        dairyimage.setOnClickListener(view -> openGallery());

        // 저장 버튼 동작
        Button savebtn = findViewById(R.id.savebtn);
        savebtn.setOnClickListener(view -> {
            String title = dairytitle.getText().toString().trim();
            String content = dairytxt.getText().toString().trim();

            // 데이터 저장
            databaseManager.insertEntry(
                    DatabaseHelper.convertStringToLocalDate(selectedDate),
                    selectedBitmap,
                    title,
                    content
            );

            Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
        });

        // 나가기 버튼 동작
        Button exitbtn = findViewById(R.id.exitbtn);
        exitbtn.setOnClickListener(view -> finish());
    }

    // 갤러리 열기
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*"); // 이미지만 표시
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData(); // 선택된 이미지 URI

            try {
                // URI를 Bitmap으로 변환
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                // 선택된 이미지를 ImageButton에 표시
                dairyimage.setImageBitmap(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
