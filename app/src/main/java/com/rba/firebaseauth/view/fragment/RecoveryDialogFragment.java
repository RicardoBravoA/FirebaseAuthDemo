package com.rba.firebaseauth.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Ricardo Bravo on 24/06/16.
 */

public class RecoveryDialogFragment extends DialogFragment implements View.OnClickListener{

    private AppCompatButton btnSendEmail;
    private AppCompatEditText txtEmail;
    private TextInputLayout inputLayoutEmail;
    private LinearLayout linGeneral;
    OnDialogFragmentListener onDialogFragmentListener;

    public RecoverPasswordDialogFragment(){
    }

    public static RecoverPasswordDialogFragment newInstance(String title){
        RecoverPasswordDialogFragment frag = new RecoverPasswordDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recover_password_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        onDialogFragmentListener = (OnDialogFragmentListener)getActivity();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        btnSendEmail = (AppCompatButton) view.findViewById(R.id.btnSendEmail);
        txtEmail = (AppCompatEditText) view.findViewById(R.id.txtEmail);
        inputLayoutEmail = (TextInputLayout) view.findViewById(R.id.inputLayoutEmail);
        linGeneral = (LinearLayout) view.findViewById(R.id.linGeneral);

        btnSendEmail.setOnClickListener(this);
        txtEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEND){
                    btnSendEmail.performClick();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSendEmail:
                if(Util.validEmail(txtEmail.getText().toString().trim())){
                    inputLayoutEmail.setErrorEnabled(false);
                    getDialog().dismiss();
                    onDialogFragmentListener.onDialogFragmentClick();
                }else{
                    inputLayoutEmail.setError(getResources().getString(R.string.login_email_error));
                }
                break;
            default:
                break;
        }
    }

}
