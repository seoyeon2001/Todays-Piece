package com.example.todayspiece;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todayspiece.database.CalendarEntry;
import com.example.todayspiece.database.DatabaseHelper;
import com.example.todayspiece.database.DatabaseManager;

import java.io.IOException;

public class SecondActivity extends Activity {

    private static final int PICK_IMAGE_REQUEST = 1; // 이미지 선택 요청 코드
    private Bitmap selectedBitmap = null; // 선택된 이미지

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        // View 초기화
        TextView selectedDateTextView = findViewById(R.id.selectedDateTextView); // 선택된 날짜
        EditText dairytitle = findViewById(R.id.dateEditText); // 제목 입력
        EditText dairytxt = findViewById(R.id.diaryEditText); // 내용 입력
        ImageButton dairyimage = findViewById(R.id.dateImagebtn); // 이미지 선택 버튼

        // Intent로부터 선택된 날짜 가져오기
        Intent intent = getIntent();
        String selectedDate = intent.getStringExtra("selectedDate");

        // 선택된 날짜를 TextView에 표시
        selectedDateTextView.setText(selectedDate);

        // 데이터베이스 초기화
        DatabaseManager databaseManager = new DatabaseManager(this);

        // 기존 데이터 로드
        CalendarEntry entry = databaseManager.getEntry(DatabaseHelper.convertStringToLocalDate(selectedDate));
        if (entry != null) {
            dairytitle.setText(entry.getTitle()); // 제목 설정
            dairytxt.setText(entry.getDetails()); // 내용 설정
            if (entry.getImage() != null) {
                dairyimage.setImageBitmap(entry.getImage()); // 이미지 설정
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

            Toast.makeText(this, getString(R.string.saved_message), Toast.LENGTH_SHORT).show();

            // 저장 후 메인 화면으로 이동
            goToMainActivity();
        });

        // 나가기 버튼
        Button exitbtn = findViewById(R.id.exitbtn);
        exitbtn.setOnClickListener(view -> finish());
    }

    // 갤러리 열기
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"); // 이미지 파일만 표시
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                // URI -> Bitmap 변환
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                ImageButton dairyimage = findViewById(R.id.dateImagebtn);
                dairyimage.setImageBitmap(selectedBitmap); // 선택된 이미지 설정
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 메인 화면으로 이동
    private void goToMainActivity() {
        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();  // 현재 액티비티 종료
    }
}
