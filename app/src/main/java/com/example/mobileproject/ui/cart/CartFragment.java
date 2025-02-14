package com.example.mobileproject.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.mobileproject.databinding.FragmentCartBinding;
import com.example.mobileproject.datacart.AppDatabase;
import com.example.mobileproject.datacart.Cart;

import java.util.List;

public class CartFragment extends Fragment {

    private FragmentCartBinding binding;
    RecyclerView v;
    TextView totalprice;
    public static AppDatabase db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CartViewModel cartViewModel =
                new ViewModelProvider(this).get(CartViewModel.class);

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        v=binding.cartRecycler;
        totalprice=binding.gTotalprice;
        db= Room.databaseBuilder(getContext(),AppDatabase.class,"cart_db").build();
        db.cartdao().getallcartitems().observe(getViewLifecycleOwner(), new Observer<List<Cart>>() {
            @Override
            public void onChanged(List<Cart> carts) {
                CustomAdapter c = new CustomAdapter(carts, CartFragment.this);
                v.setLayoutManager(new LinearLayoutManager(getContext()));
                v.setAdapter(c);

            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}