package com.rba.firebaseauth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rba.firebaseauth.R;
import com.rba.firebaseauth.ui.base.BaseActivity;
import com.rba.firebaseauth.util.Util;

/**
 * Created by Ricardo Bravo on 24/06/16.
 */

public class CreateAccountActivity extends BaseActivity implements View.OnClickListener {

    private CoordinatorLayout clGeneral;
    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText tieEmail, tiePassword;
    private AppCompatButton btnRegister;
    private AppCompatTextView lblSession;
    private FirebaseAuth firebaseAuth;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        initUI();
    }

    private void initUI(){
        firebaseAuth = FirebaseAuth.getInstance();
        tilEmail = (TextInputLayout)findViewById(R.id.tilEmail);
        clGeneral = (CoordinatorLayout)findViewById(R.id.clGeneral);
        tilPassword = (TextInputLayout)findViewById(R.id.tilPassword);
        tieEmail = (TextInputEditText)findViewById(R.id.tieEmail);
        tiePassword = (TextInputEditText)findViewById(R.id.tiePassword);
        btnRegister = (AppCompatButton)findViewById(R.id.btnRegister);
        lblSession = (AppCompatTextView)findViewById(R.id.lblSession);

        btnRegister.setOnClickListener(this);
        lblSession.setOnClickListener(this);
        tieEmail.addTextChangedListener(new CustomTextWatcher(tieEmail));
        tiePassword.addTextChangedListener(new CustomTextWatcher(tiePassword));

        tiePassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_GO){
                    btnRegister.performClick();
                    return true;
                }
                return false;
            }
        });

    }

    public boolean validEmail(){

        email = tieEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            tilEmail.setError(Util.addCustomFont(this, getString(R.string.msg_email)));
            Util.requestFocus(this, tieEmail);
            return false;
        }

        if(Util.validEmail(email)){
            tilEmail.setErrorEnabled(false);
        }else{
            tilEmail.setError(Util.addCustomFont(this, getString(R.string.incorrect_email)));
            Util.requestFocus(this, tieEmail);
            return false;
        }

        return true;
    }

    private boolean validPassword(){
        password = tiePassword.getText().toString().trim();

        if(TextUtils.isEmpty(password)) {
            tilPassword.setError(Util.addCustomFont(this, getString(R.string.msg_password)));
            Util.requestFocus(this, tiePassword);
            return false;
        }

        if(password.length()<6){
            tilPassword.setError(Util.addCustomFont(this, getString(R.string.min_password)));
            Util.requestFocus(this, tiePassword);
            return false;
        }else{
            tilPassword.setErrorEnabled(false);
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lblSession:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.btnRegister:

                if(!validEmail()){
                    return;
                }

                if(!validPassword()){
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(CreateAccountActivity.this, MainActivity.class));
                        }else{
                            Util.showSnackBar(clGeneral, getString(R.string.msg_invalid_login));
                        }
                    }
                });

                break;
        }
    }

    private class CustomTextWatcher implements TextWatcher {

        private View view;

        private CustomTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.tieEmail:
                    validEmail();
                    break;
                case R.id.tiePassword:
                    validPassword();
                    break;
            }
        }
    }

}
