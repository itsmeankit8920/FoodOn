package com.ankit.foodon;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhone extends AppCompatActivity {

    String verificationId;
    FirebaseAuth FAuth;
    Button verify , Resend ;
    TextView txt;
    EditText entercode;
    String phoneno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        phoneno = getIntent().getStringExtra("phonenumber").trim();

        entercode = (EditText) findViewById(R.id.codee);
        txt = (TextView) findViewById(R.id.text);
        Resend = (Button)findViewById(R.id.Resendotpp);
        verify = (Button) findViewById(R.id.Verifyy);
        FAuth = FirebaseAuth.getInstance();

        Resend.setVisibility(View.INVISIBLE);
        txt.setVisibility(View.INVISIBLE);

        sendverificationcode(phoneno);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = entercode.getText().toString().trim();
                Resend.setVisibility(View.INVISIBLE);

                if (code.isEmpty() && code.length()<6){
                    entercode.setError("Enter code");
                    entercode.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });
        new CountDownTimer(60000,1000){

            @Override
            public void onTick(long millisUntilFinished) {

                txt.setVisibility(View.VISIBLE);
                txt.setText("Resend Code Within"+millisUntilFinished/1000+"Seconds");

            }

            /**
             * Callback fired when the time is up.
             */
            @Override
            public void onFinish() {
                Resend.setVisibility(View.VISIBLE);
                txt.setVisibility(View.INVISIBLE);

            }
        }.start();

        Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Resend.setVisibility(View.INVISIBLE);
                Resendotp(phoneno);

                new CountDownTimer(60000,1000){

                    @Override
                    public void onTick(long millisUntilFinished) {

                        txt.setVisibility(View.VISIBLE);
                        txt.setText("Resend Code Within "+millisUntilFinished/1000+" Seconds");

                    }

                    /**
                     * Callback fired when the time is up.
                     */
                    @Override
                    public void onFinish() {
                        Resend.setVisibility(View.VISIBLE);
                        txt.setVisibility(View.INVISIBLE);

                    }
                }.start();
            }
        });


    }

    private void Resendotp(String phonenum) {

        sendverificationcode(phonenum);
    }

    private void sendverificationcode(String number) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(

                number,
                60,
                TimeUnit.SECONDS,
                (Activity) TaskExecutors.MAIN_THREAD,
                mcallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if(code != null){
                entercode.setText(code);  // Auto Verification
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyPhone.this , e.getMessage(),Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCodeSent(String s , PhoneAuthProvider.ForceResendingToken forceResendingToken){
            super.onCodeSent(s,forceResendingToken);

            verificationId = s;

        }
    };

    private void verifyCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId , code);
        linkCredential(credential);
    }

    private void linkCredential(PhoneAuthCredential credential) {

        FAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(VerifyPhone.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            Intent intent = new Intent(VerifyPhone.this , MainMenu.class);
                            startActivity(intent);
                            finish();
                        }else{
                            ReusableCodeForAll.ShowAlert(VerifyPhone.this,"Error",task.getException().getMessage());
                        }
                    }
                });

    }

}