package com.example.testverificationcode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private VerificationCodeInput mVerificationCodeInput;
    private Button mBtn1, mBtn2, mBtn3, mBtn4, mBtn5, mBtn6, mBtn7, mBtn8, mBtn9, mBtn0, mBtnDelete, mBtnClear;
    private StringBuilder mSb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSb = new StringBuilder();

        mVerificationCodeInput = findViewById(R.id.verificationCodeInput);
        mVerificationCodeInput.setOnCompleteListener(new VerificationCodeInput.Listener() {
            @Override
            public void onComplete(String content) {
                if ("666666".equals(content)) {
                    mVerificationCodeInput.setSuccessBg();
                    Toast.makeText(getApplicationContext(), "通过验证", Toast.LENGTH_SHORT).show();
                } else {
                    mVerificationCodeInput.setErrorBg();
                    Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBtn0 = findViewById(R.id.btn0);
        mBtn1 = findViewById(R.id.btn1);
        mBtn2 = findViewById(R.id.btn2);
        mBtn3 = findViewById(R.id.btn3);
        mBtn4 = findViewById(R.id.btn4);
        mBtn5 = findViewById(R.id.btn5);
        mBtn6 = findViewById(R.id.btn6);
        mBtn7 = findViewById(R.id.btn7);
        mBtn8 = findViewById(R.id.btn8);
        mBtn9 = findViewById(R.id.btn9);
        mBtnDelete = findViewById(R.id.btn_delete);
        mBtnClear = findViewById(R.id.btn_clear);
        mBtn0.setOnClickListener(this);
        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
        mBtn3.setOnClickListener(this);
        mBtn4.setOnClickListener(this);
        mBtn5.setOnClickListener(this);
        mBtn6.setOnClickListener(this);
        mBtn7.setOnClickListener(this);
        mBtn8.setOnClickListener(this);
        mBtn9.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);
        mBtnClear.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn0:
                addOneCode("0");
                break;
            case R.id.btn1:
                addOneCode("1");
                break;
            case R.id.btn2:
                addOneCode("2");
                break;
            case R.id.btn3:
                addOneCode("3");
                break;
            case R.id.btn4:
                addOneCode("4");
                break;
            case R.id.btn5:
                addOneCode("5");
                break;
            case R.id.btn6:
                addOneCode("6");
                break;
            case R.id.btn7:
                addOneCode("7");
                break;
            case R.id.btn8:
                addOneCode("8");
                break;
            case R.id.btn9:
                addOneCode("9");
                break;
            case R.id.btn_delete:
                if (mSb.length() >= 1) {
                    mSb.deleteCharAt(mSb.length() - 1);
                    mVerificationCodeInput.delete();
                }
                break;
            case R.id.btn_clear:
                clearText();
                break;
            default:
                break;
        }
    }

    private void clearText() {
        if (mSb.length() > 0) {
            mSb.delete(0, mSb.length());
            mVerificationCodeInput.setText(mSb.toString());
            Toast.makeText(getApplicationContext(), "已清空验证码", Toast.LENGTH_SHORT).show();
        }
    }

    private void addOneCode(String content) {
        if (mSb.length() < 6 && !TextUtils.isEmpty(content) && content.length() == 1) {
            mSb.append(content);
            mVerificationCodeInput.setText(mSb.toString());
        }
    }
}
