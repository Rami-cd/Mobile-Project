package com.example.mobileproject.ui.cart;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobileproject.R;
import com.example.mobileproject.datacart.Cart;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    List<Cart> carts;
    private final CartFragment fragment;
    public CustomAdapter(List<Cart> crt, CartFragment fragment){
        carts=crt;
        this.fragment = fragment;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row2,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getName().setText(carts.get(position).name);
        Double price = carts.get(position).price;
        holder.getPrice().setText(String.valueOf(price));
        Uri imagepath=Uri.parse(carts.get(position).image_url);
        Glide.with(fragment)
                .load(imagepath)
                .into(holder.imageg);



    }

    @Override
    public int getItemCount() {
        return carts.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView name;
        private  final TextView price;
        private final ImageView imageg;
        private ImageView deleteButton;

        public TextView getName() {
            return name;
        }



        public TextView getPrice() {
            return price;
        }

        public ImageView getDeleteButton() {
            return deleteButton;
        }

        public ImageView getImageg() {
            return imageg;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.g_name);
            price=itemView.findViewById(R.id.g_price);
            imageg=itemView.findViewById(R.id.g_imgid);
            deleteButton=itemView.findViewById(R.id.g_delete);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){


                        DeleteAlertDialog dialog = new DeleteAlertDialog(carts.get(position));
                        dialog.show(fragment.getChildFragmentManager(), "DeleteDialog");
                    }
                }
            });



        }
    }
}
