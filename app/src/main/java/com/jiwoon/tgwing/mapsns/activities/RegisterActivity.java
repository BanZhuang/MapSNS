package com.jiwoon.tgwing.mapsns.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jiwoon.tgwing.mapsns.R;
import com.jiwoon.tgwing.mapsns.models.User;
import com.jiwoon.tgwing.mapsns.networking.UserNetwork;

/**
 * Created by jiwoonwon on 2017. 4. 3..
 */
// 이 클래스에서는 facebook 로그인은 되있지만 firebase에 정보가 없는 유저의 정보를 입력받고 이를 전달하는 역할을 함
public class RegisterActivity extends BaseActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    private User mUser;

    private EditText userNameEdit;
    private EditText emailEdit;
    private EditText ageEdit;
    private Button submitButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUser = User.getInstance();

        userNameEdit = (EditText) findViewById(R.id.activity_register_username);
        emailEdit = (EditText) findViewById(R.id.activity_register_email);
        ageEdit = (EditText) findViewById(R.id.activity_register_age);

        userNameEdit.setText(mUser.getUserName());
        emailEdit.setText(mUser.getUserEmail());

        submitButton = (Button) findViewById(R.id.activity_register_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ageEdit.getText().toString().length() > 0) {
                    mUser.setUserName(userNameEdit.getText().toString());
                    mUser.setUserEmail(emailEdit.getText().toString());
                    mUser.setAge(ageEdit.getText().toString());
                    Log.d("RegisterActivity", "User Id : " + mUser.getUserId());

                    // 파이어베이스에 저장
                    UserNetwork.setUserToFirebase(mUser.getUserId(), mUser.getUserName(), mUser.getUserEmail(),
                            mUser.getAge(), "", "");

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "나이를 입력해주세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }
}
