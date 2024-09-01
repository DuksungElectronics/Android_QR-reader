package com.example.admin_qr_reader;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    //view Objects
    private Button buttonScan; //ScanButton
    int a = 0; //userID

    String num; //userNumber

    //qr code scanner object
    private IntentIntegrator qrScan;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference(); //firebase db에 접근하기 위한 메소드

        readUser(); //사용자 데이터를 읽어오는 코드

        buttonScan = (Button) findViewById(R.id.buttonScan);

        qrScan = new IntentIntegrator(this); //QR코드스캔 기능 라이브러리 제공

        buttonScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                qrScan.setPrompt("Scanning..."); //스캔 옵션
                qrScan.initiateScan(); //QR스캔 화면 나타남
            }
        });
    }

    //스캔 결고 얻오는 메소드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data); //스캔결과저장
        if (result != null) {
            //qrcode 가 없으면
            if (result.getContents() == null) {
                Toast.makeText(MainActivity.this, "취소!", Toast.LENGTH_SHORT).show();
            } else {
                //qrcode 결과가 있으면
                Toast.makeText(MainActivity.this, "스캔완료!", Toast.LENGTH_SHORT).show();
                try {
                    //data를 json으로 변환
                    JSONObject obj = new JSONObject(result.getContents());
                    num = obj.getString("userNumber"); //json 객체에서 userNumber라는 키에 해당하는 값을 가져와 num 변수에 할당

                    if (num != null && !num.isEmpty()) {
                        String getUserNumber = num;
                        //hashmap 만들기
                        HashMap result1 = new HashMap<>();
                        result1.put("번호", getUserNumber); //번호 키와 getUserNumber를 할당

                        a = a + 1; //userID
                        writeNewUser(a, getUserNumber); //와 getUserNumber 저장
                    }else {
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Admin 회원가입이 되어있지 않습니다.", Toast.LENGTH_SHORT).show(); // 형식이 다르면
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //사용자 데이터를 저장하는 메소드
    private void writeNewUser(int userId, String userNumber) {
        user user = new user(userNumber); //새로운 인스턴스를 생성

        mDatabase.child("users").child(String.valueOf(userId)).setValue(user) //firebase users경로
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // write 성공하면!
                        Toast.makeText(MainActivity.this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // write 실패하면!
                        Toast.makeText(MainActivity.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    // 사용자 데이터를 읽어오는 코드
    private void readUser(){
        mDatabase.child("users").child("1").addValueEventListener(new ValueEventListener() {
            //db에 데이터를 읽어왔을때 호출
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(user.class) != null){
                    user post = dataSnapshot.getValue(user.class);
                    Log.w("FireBaseData", "getData" + post.toString());
                } else {
                    Toast.makeText(MainActivity.this, "데이터 없음...", Toast.LENGTH_SHORT).show();
                }
            }

            // db에 데이터를 읽어오는 중에 취소되었을때, 디버깅용
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }








}