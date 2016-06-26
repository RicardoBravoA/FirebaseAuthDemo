package com.rba.firebaseauth.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rba.firebaseauth.R;

/**
 * Created by Ricardo Bravo on 25/06/16.
 */

public class ProductFragment extends Fragment {


    public ProductFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

}
