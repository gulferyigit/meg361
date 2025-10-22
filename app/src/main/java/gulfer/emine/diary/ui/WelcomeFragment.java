package gulfer.emine.diary.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import gulfer.emine.diary.R;

public class WelcomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_welcome, container, false);
        try {
            View bottomNav = requireActivity().findViewById(R.id.bottomNav);
            if (bottomNav != null) bottomNav.setVisibility(View.GONE);
        } catch (Exception ignored) {}
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            View bottomNav = requireActivity().findViewById(R.id.bottomNav);
            if (bottomNav != null) bottomNav.setVisibility(View.VISIBLE);
        } catch (Exception ignored) {}
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            View bottomNav = requireActivity().findViewById(R.id.bottomNav);
            if (bottomNav != null) bottomNav.setVisibility(View.GONE);
        } catch (Exception ignored) {}
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            View bottomNav = requireActivity().findViewById(R.id.bottomNav);
            if (bottomNav != null) bottomNav.setVisibility(View.VISIBLE);
        } catch (Exception ignored) {}
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View btnLogin = view.findViewById(R.id.btnLoginWelcome);
        if (btnLogin != null) btnLogin.setOnClickListener(v -> {
            try { Navigation.findNavController(v).navigate(R.id.loginFragment); } catch (Exception ignored) {}
        });

        View btnRegister = view.findViewById(R.id.btnRegisterWelcome);
        if (btnRegister != null) btnRegister.setOnClickListener(v -> {
            try { Navigation.findNavController(v).navigate(R.id.registerFragment); } catch (Exception ignored) {}
        });
    }
}
