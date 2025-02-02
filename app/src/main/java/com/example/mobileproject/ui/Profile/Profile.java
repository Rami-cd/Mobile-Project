package com.example.mobileproject.ui.Profile;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mobileproject.databinding.FragmentProfileBinding;

public class Profile extends Fragment {
    private  FragmentProfileBinding binding_to_fragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding_to_fragment = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding_to_fragment.getRoot();

        binding_to_fragment.fragmentPofileText.setText("finally");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding_to_fragment = null;
    }
}
