package com.rba.firebaseauth.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rba.firebaseauth.R;
import com.rba.firebaseauth.model.entity.ProductEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ricardo Bravo on 26/06/16.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private List<ProductEntity> productEntityList;
    static LayoutInflater inflater = null;


    public ProductAdapter(Context context, List<ProductEntity> productEntityList) {
        if(context!=null){
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.productEntityList = productEntityList;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ProductEntity productEntity = productEntityList.get(position);
        holder.lblTitle.setText(productEntity.getDescription());
        holder.lblPrice.setText(context.getString(R.string.price_text, productEntity.getPrice()));

        Picasso.with(context)
                .load(productEntity.getImage())
                .error(R.drawable.ic_avatar)
                .placeholder(R.drawable.ic_avatar)
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return productEntityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgProduct;
        private TextView lblTitle, lblPrice;
        private CardView cvGeneral;

        public ViewHolder(View view) {
            super(view);
            lblTitle = (TextView) view.findViewById(R.id.lblTitle);
            lblPrice = (TextView) view.findViewById(R.id.lblPrice);
            cvGeneral = (CardView) view.findViewById(R.id.cvGeneral);
            imgProduct = (ImageView) view.findViewById(R.id.imgProduct);

            cvGeneral.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cvGeneral:
                    Toast.makeText(context, "Pressed: "
                            +productEntityList.get(getAdapterPosition()).getDescription(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
}
