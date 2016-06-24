package com.rba.firebaseauth.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rba.firebaseauth.R;
import com.rba.firebaseauth.ui.base.BaseActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText tieEmail, tiePassword;
    private AppCompatButton btnLogin;
    private AppCompatTextView lblRecoveryPassword, lblCreateAccount;
    private FirebaseAuth firebaseAuth;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
    }

    private void initUI(){

        firebaseAuth = FirebaseAuth.getInstance();
        tilEmail = (TextInputLayout)findViewById(R.id.tilEmail);
        tilPassword = (TextInputLayout)findViewById(R.id.tilPassword);
        tieEmail = (TextInputEditText)findViewById(R.id.tieEmail);
        tiePassword = (TextInputEditText)findViewById(R.id.tiePassword);
        btnLogin = (AppCompatButton)findViewById(R.id.btnLogin);
        lblRecoveryPassword = (AppCompatTextView)findViewById(R.id.lblRecoveryPassword);
        lblCreateAccount = (AppCompatTextView)findViewById(R.id.lblCreateAccount);

        btnLogin.setOnClickListener(this);
        lblRecoveryPassword.setOnClickListener(this);
        lblCreateAccount.setOnClickListener(this);

    }

    private boolean validForm(){
        email = tieEmail.getText().toString().trim();
        password = tiePassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_user),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_password), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                Log.i("x- msge", "login");

                if(validForm()){
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        Log.i("x- msge", "false");
                                    } else {
                                        Log.i("x- msge", "true");
                                    }
                                }
                            });
                }

                break;
            case R.id.lblCreateAccount:
                Log.i("x- msge", "account");
                break;
            case R.id.lblRecoveryPassword:
                Log.i("x- msge", "password");
                break;
            default:
                break;
        }
    }

}
