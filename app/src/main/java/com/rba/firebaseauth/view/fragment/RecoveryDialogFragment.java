package com.rba.firebaseauth.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.rba.firebaseauth.R;
import com.rba.firebaseauth.util.Util;

/**
 * Created by Ricardo Bravo on 24/06/16.
 */

public class RecoveryDialogFragment extends DialogFragment implements View.OnClickListener{

    private AppCompatButton btnSend;
    private AppCompatEditText txtEmail;
    private TextInputLayout tilEmail;
    private FirebaseAuth firebaseAuth;
    private String email;

    public RecoveryDialogFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recovery_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        btnSend = (AppCompatButton) view.findViewById(R.id.btnSend);
        txtEmail = (AppCompatEditText) view.findViewById(R.id.txtEmail);
        tilEmail = (TextInputLayout) view.findViewById(R.id.tilEmail);
        txtEmail.addTextChangedListener(new CustomTextWatcher(txtEmail));

        firebaseAuth = FirebaseAuth.getInstance();

        btnSend.setOnClickListener(this);
        txtEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEND){
                    btnSend.performClick();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSend:

                if(!validEmail()){
                    return;
                }

                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), getString(R.string.msg_email_sent),
                                    Toast.LENGTH_SHORT).show();
                            getDialog().dismiss();
                        }else{
                            Util.requestFocus(getActivity(), txtEmail);
                            Toast.makeText(getActivity(), getString(R.string.msg_error_email),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
            default:
                break;
        }
    }

    public boolean validEmail(){

        email = txtEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            tilEmail.setError(Util.addCustomFont(getActivity(), getString(R.string.msg_email)));
            Util.requestFocus(getActivity(), txtEmail);
            return false;
        }

        if(Util.validEmail(email)){
            tilEmail.setErrorEnabled(false);
        }else{
            tilEmail.setError(Util.addCustomFont(getActivity(), getString(R.string.incorrect_email)));
            Util.requestFocus(getActivity(), txtEmail);
            return false;
        }

        return true;
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
                case R.id.txtEmail:
                    validEmail();
                    break;
            }
        }
    }

}
