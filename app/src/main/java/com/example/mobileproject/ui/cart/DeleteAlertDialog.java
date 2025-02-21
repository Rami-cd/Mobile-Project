package com.example.mobileproject.ui.cart;



import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mobileproject.MainActivity;
import com.example.mobileproject.datacart.Cart;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeleteAlertDialog extends DialogFragment {
    Cart cart;
    public DeleteAlertDialog(Cart c){
        cart=c;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder biulder=new AlertDialog.Builder(getContext());
        biulder.setMessage("are you sure you want to delete this item from card");
        biulder.setTitle("Confirmation");
        biulder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ExecutorService ex= Executors.newSingleThreadExecutor();
                ex.execute(new Runnable() {
                    @Override

                    public void run() {
                        //i put mainactivity.db
                        MainActivity.cartDatabase.cartdao().deletefrom(cart);
                    }
                });
            }
        });
        biulder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return  biulder.create();
    }
}
