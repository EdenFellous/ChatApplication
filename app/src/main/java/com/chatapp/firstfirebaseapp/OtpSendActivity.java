package com.chatapp.firstfirebaseapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class OtpSendActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    EditText etd;
    Button b,skip;
    ProgressBar p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_otp_send);
     //   talertg();
       etd=(EditText)findViewById(R.id.etPhone);
       b=(Button) findViewById(R.id.btnSend);
        skip=(Button) findViewById(R.id.btnskip);
       p=(ProgressBar) findViewById(R.id.progressBar);
       b.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (etd.getText().toString().trim().isEmpty()) {
                   Toast.makeText(OtpSendActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
               } else if (etd.getText().toString().trim().length() != 10) {
                   Toast.makeText(OtpSendActivity.this, "Type valid Phone Number", Toast.LENGTH_SHORT).show();
               } else {
                   otpSend();
               }
           }
       });


       skip.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent inte = new Intent(OtpSendActivity.this, MainActivity.class);
               startActivity(inte);
               finish();
           }
       });
        mAuth = FirebaseAuth.getInstance();



    }

    private void otpSend() {
       p.setVisibility(View.VISIBLE);
        b.setVisibility(View.INVISIBLE);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
               p.setVisibility(View.GONE);
               b.setVisibility(View.VISIBLE);
                Toast.makeText(OtpSendActivity.this, "Try to verify later", Toast.LENGTH_SHORT).show();
                Log.d("msg verification..........",e.getLocalizedMessage());
                Intent inten = new Intent(OtpSendActivity.this, MainActivity.class);
                startActivity(inten);
                finish();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                p.setVisibility(View.GONE);
               b.setVisibility(View.VISIBLE);
                Toast.makeText(OtpSendActivity.this, "OTP is successfully send.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OtpSendActivity.this, OtpVerifyActivity.class);
                intent.putExtra("phone", etd.getText().toString().trim());
                intent.putExtra("verificationId", verificationId);
                startActivity(intent);
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + etd.getText().toString().trim())
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


}