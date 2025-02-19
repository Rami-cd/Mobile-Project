package com.example.mobileproject.ui.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.mobileproject.LoginActivity;
import com.example.mobileproject.R;
import com.example.mobileproject.databinding.FragmentProfileBinding;
import com.example.mobileproject.ui.cart.CartFragment;
import com.example.mobileproject.ui.Profile.EditProfileActivity;


public class Profile extends Fragment {

    private FragmentProfileBinding binding_to_fragment;
    private SharedPreferences credentials_sp;

    ImageView profileImage;
    TextView profileName, profileEmail;
    Button editProfileButton, logoutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding_to_fragment = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding_to_fragment.getRoot();

        profileImage = binding_to_fragment.profileImage;
        profileName = binding_to_fragment.profileName;
        profileEmail = binding_to_fragment.profileEmail;
        editProfileButton = binding_to_fragment.btnEditProfile;

        logoutButton = binding_to_fragment.btnLogout;

        // Initialize SharedPreferences
        credentials_sp = requireActivity().getSharedPreferences("credentials", Context.MODE_PRIVATE);
        loadUserData();

        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });




        logoutButton.setOnClickListener(v -> showLogoutConfirmationDialog());

        return root;
    }

    private void loadUserData() {
        String username = credentials_sp.getString("name", "User Name");
        String email = credentials_sp.getString("email", "user@example.com");
        String profilePicUri = credentials_sp.getString("profile_pic_uri", null);

        profileName.setText(username);
        profileEmail.setText(email);

        // Load profile image if available
        if (profilePicUri != null) {
            Glide.with(this).load(profilePicUri).into(profileImage);
        } else {
            profileImage.setImageResource(R.drawable.ic_profile);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData(); // Refresh data when fragment resumes
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding_to_fragment = null;
    }

    public void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> logoutUser())
                .setNegativeButton("Cancel", null)
                .show();
    }

    public void logoutUser() {
        SharedPreferences.Editor cred_sp_editor = credentials_sp.edit();
        cred_sp_editor.clear(); // Clear all stored user data
        cred_sp_editor.apply();

        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
