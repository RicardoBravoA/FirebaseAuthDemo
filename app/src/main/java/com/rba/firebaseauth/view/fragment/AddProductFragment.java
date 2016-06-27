package com.rba.firebaseauth.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.rba.firebaseauth.R;
import com.rba.firebaseauth.model.entity.ImageEntity;
import com.rba.firebaseauth.util.Constant;
import com.rba.firebaseauth.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ricardo Bravo on 26/06/16.
 */

public class AddProductFragment extends Fragment implements View.OnClickListener {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<ImageEntity> imageEntityList = new ArrayList<>();
    private TextInputLayout tilTitle, tilPrice;
    private TextInputEditText tieTitle, tiePrice;
    private AppCompatSpinner spImage;
    private AppCompatButton btnRegister;
    private String description, price;
    private View view;
    private Map<String, String> product;

    public AddProductFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("product");

        view =  inflater.inflate(R.layout.fragment_add_product, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        tilTitle = (TextInputLayout) view.findViewById(R.id.tilTitle);
        tilPrice = (TextInputLayout) view.findViewById(R.id.tilPrice);
        tieTitle = (TextInputEditText) view.findViewById(R.id.tieTitle);
        tiePrice = (TextInputEditText) view.findViewById(R.id.tiePrice);
        spImage = (AppCompatSpinner) view.findViewById(R.id.spImage);
        btnRegister = (AppCompatButton) view.findViewById(R.id.btnRegister);

        tieTitle.addTextChangedListener(new CustomTextWatcher(tieTitle));
        tiePrice.addTextChangedListener(new CustomTextWatcher(tiePrice));
        btnRegister.setOnClickListener(this);

        generateImage();

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
                case R.id.tieTitle:
                    validTitle();
                    break;
                case R.id.tiePrice:
                    validPrice();
                    break;
            }
        }
    }

    private boolean validTitle(){

        description = tieTitle.getText().toString().trim();

        if(TextUtils.isEmpty(description)) {
            tilTitle.setError(Util.addCustomFont(getActivity(), getString(R.string.min_character)));
            Util.requestFocus(getActivity(), tieTitle);
            return false;
        }

        if(description.length()<3){
            tilTitle.setError(Util.addCustomFont(getActivity(), getString(R.string.min_password)));
            Util.requestFocus(getActivity(), tieTitle);
            return false;
        }else{
            tilTitle.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validPrice(){

        price = tiePrice.getText().toString().trim();

        if(TextUtils.isEmpty(price)) {
            tilPrice.setError(Util.addCustomFont(getActivity(), getString(R.string.min_character)));
            Util.requestFocus(getActivity(), tiePrice);
            return false;
        }

        if(price.length()<1){
            tilPrice.setError(Util.addCustomFont(getActivity(), getString(R.string.min_password)));
            Util.requestFocus(getActivity(), tiePrice);
            return false;
        }else{
            tilPrice.setErrorEnabled(false);
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:

                if(!validPrice()){
                    return;
                }

                if(!validTitle()){
                    return;
                }


                ImageEntity imageEntity = (ImageEntity) spImage.getSelectedItem();

                product = new HashMap<>();
                product.put(Constant.TAG_DESCRIPTION, description);
                product.put(Constant.TAG_PRICE, price);
                product.put(Constant.TAG_IMAGE, imageEntity.getImage());

                databaseReference.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        databaseReference.push().setValue(product);
                        return null;
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                        if(b){
                            Toast.makeText(getActivity(), getString(R.string.msg_invalid_register), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), getString(R.string.msg_register), Toast.LENGTH_SHORT).show();
                            spImage.setSelection(0);
                            tiePrice.setText("");
                            tilPrice.setErrorEnabled(false);
                            tieTitle.setText("");
                            tilTitle.setErrorEnabled(false);
                        }
                    }
                });

                break;
            default:
                break;
        }
    }


    private void generateImage(){
        imageEntityList.add(new ImageEntity(
                "http://www.gadgets-reviews.com/images/wsscontent/articles/2015/03/the-best-mouse-for-mmorpg.jpg",
                "Image 1"));
        imageEntityList.add(new ImageEntity("http://assets.razerzone.com/eeimages/products/13785/razer-naga-2014-right-03.png",
                "Image 2"));
        imageEntityList.add(new ImageEntity("http://assets.razerzone.com/eeimages/wildcat/imgs/gallery/rzr_wildcat_05.png",
                "Image 3"));
        imageEntityList.add(new ImageEntity("https://ekit.co.uk/GalleryEntries/eCommerce_solutions_and_services/MedRes_Product-presentation-2.jpg?q=27012012153123",
                "Image 4"));
        imageEntityList.add(new ImageEntity("http://s3-us-west-2.amazonaws.com/hypebeast-wordpress/image/2009/07/huf-converse-product-red-skidgrip-1.jpg",
                "Image 5"));
        imageEntityList.add(new ImageEntity("https://alicarnold.files.wordpress.com/2009/11/new-product.jpg",
                "Image 6"));

        ArrayAdapter spinner_adapter = new ArrayAdapter(getActivity(), R.layout.item_spinner, imageEntityList);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spImage.setAdapter(spinner_adapter);
    }

}
