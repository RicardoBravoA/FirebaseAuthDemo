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
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.rba.firebaseauth.R;
import com.rba.firebaseauth.util.Util;

/**
 * Created by Ricardo Bravo on 24/06/16.
 */

public class ChangePasswordDialogFragment extends DialogFragment implements View.OnClickListener{

    private AppCompatButton btnSend;
    private AppCompatEditText txtPassword;
    private TextInputLayout tilPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String password;

    public ChangePasswordDialogFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.change_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        btnSend = (AppCompatButton) view.findViewById(R.id.btnSend);
        txtPassword = (AppCompatEditText) view.findViewById(R.id.txtPassword);
        tilPassword = (TextInputLayout) view.findViewById(R.id.tilPassword);
        txtPassword.addTextChangedListener(new CustomTextWatcher(txtPassword));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        btnSend.setOnClickListener(this);
        txtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

                if(!validPassword()){
                    return;
                }

                firebaseUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), getString(R.string.success_change_password),
                                    Toast.LENGTH_SHORT).show();
                            getDialog().dismiss();
                            firebaseAuth.signOut();
                        }else{
                            Util.requestFocus(getActivity(), txtPassword);
                            Toast.makeText(getActivity(), getString(R.string.error_change_password),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
            default:
                break;
        }
    }

    private boolean validPassword(){
        password = txtPassword.getText().toString().trim();

        if(TextUtils.isEmpty(password)) {
            tilPassword.setError(Util.addCustomFont(getActivity(), getString(R.string.msg_password)));
            Util.requestFocus(getActivity(), txtPassword);
            return false;
        }

        if(password.length()<6){
            tilPassword.setError(Util.addCustomFont(getActivity(), getString(R.string.min_password)));
            Util.requestFocus(getActivity(), txtPassword);
            return false;
        }else{
            tilPassword.setErrorEnabled(false);
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
                case R.id.txtPassword:
                    validPassword();
                    break;
            }
        }
    }

}
