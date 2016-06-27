package com.rba.firebaseauth.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rba.firebaseauth.R;
import com.rba.firebaseauth.model.entity.ProductEntity;
import com.rba.firebaseauth.util.Constant;
import com.rba.firebaseauth.view.adapter.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ricardo Bravo on 25/06/16.
 */

public class ProductFragment extends Fragment {

    private RecyclerView rcvProduct;
    private ProductAdapter productAdapter;
    private List<ProductEntity> productEntityList;
    private  View view;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public ProductFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Constant.TABLE_PRODUCT);

        view = inflater.inflate(R.layout.fragment_product, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        productEntityList = new ArrayList<>();

        rcvProduct = (RecyclerView) view.findViewById(R.id.rcvProduct);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot productSnapshot : dataSnapshot.getChildren()){
                    ProductEntity productEntity = productSnapshot.getValue(ProductEntity.class);
                    productEntityList.add(productEntity);
                }

                productAdapter = new ProductAdapter(getActivity(), productEntityList);
                //LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                rcvProduct.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                //rcvProduct.setLayoutManager(layoutManager);

                rcvProduct.setItemAnimator(new DefaultItemAnimator());
                rcvProduct.setAdapter(productAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), getString(R.string.error_get_data),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

}
