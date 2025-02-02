package com.example.mobileproject.ui.Profile;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mobileproject.LoginActivity;
import com.example.mobileproject.MainActivity;
import com.example.mobileproject.SplashActivity;
import com.example.mobileproject.databinding.FragmentProfileBinding;

public class Profile extends Fragment {

    SharedPreferences credentials_sp;

    Button logout_button;
    private  FragmentProfileBinding binding_to_fragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding_to_fragment = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding_to_fragment.getRoot();

        binding_to_fragment.fragmentPofileText.setText("finally");

        logout_button = binding_to_fragment.logoutButton;

        logout_button.setOnClickListener(this::logOut);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding_to_fragment = null;
    }

    public void logOut(View view) {
        credentials_sp = getContext().getSharedPreferences("credentials", MODE_PRIVATE);
        SharedPreferences.Editor cred_sp_editor = credentials_sp.edit();
        cred_sp_editor.remove("username");
        cred_sp_editor.remove("password");
        cred_sp_editor.remove("token");
        cred_sp_editor.apply();
        startActivity(new Intent(getContext(), LoginActivity.class));
    }
}
