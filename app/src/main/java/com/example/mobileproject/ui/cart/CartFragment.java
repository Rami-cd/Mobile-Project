package com.example.mobileproject.ui.cart;

import static com.example.mobileproject.MainActivity.cartDatabase;

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

import com.example.mobileproject.MainActivity;
import com.example.mobileproject.databinding.FragmentCartBinding;
import com.example.mobileproject.datacart.AppDatabase;
import com.example.mobileproject.datacart.Cart;

import java.util.List;

public class CartFragment extends Fragment {

    private FragmentCartBinding binding;
    RecyclerView v;
    TextView totalprice;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CartViewModel cartViewModel =
                new ViewModelProvider(this).get(CartViewModel.class);

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        v=binding.cartRecycler;
        totalprice=binding.gTotalprice;

        cartDatabase.cartdao().getallcartitems().observe((MainActivity)getContext(), new Observer<List<Cart>>() {
            @Override
            public void onChanged(List<Cart> carts) {
                CustomAdapter c = new CustomAdapter(carts, CartFragment.this);
                v.setLayoutManager(new LinearLayoutManager(getContext()));
                v.setAdapter(c);

            }
        });
//        cartDatabase.cartdao().getTotalPrice().observe(getViewLifecycleOwner(), total -> {
//            if (total != null) {
//                // Format to 2 decimal places
//                String formattedTotal = String.format("Total Price: $%.2f", total);
//                totalprice.setText(formattedTotal);
//            } else {
//                totalprice.setText("Total Price: $0.00"); // Handle empty cart
//            }
//        });


        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}