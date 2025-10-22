package gulfer.emine.diary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import gulfer.emine.diary.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View v = binding.getRoot();
        try {
            View bottomNav = requireActivity().findViewById(R.id.bottomNav);
            if (bottomNav != null) bottomNav.setVisibility(View.GONE);
        } catch (Exception ignored) {}
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding == null) return;

        binding.registerButton.setOnClickListener(v -> {
            String name = binding.nameEdit.getText() != null ? binding.nameEdit.getText().toString().trim() : "";
            String email = binding.emailEdit.getText() != null ? binding.emailEdit.getText().toString().trim() : "";
            String password = binding.passwordEdit.getText() != null ? binding.passwordEdit.getText().toString() : "";
            String passwordConfirm = binding.passwordConfirmEdit.getText() != null ? binding.passwordConfirmEdit.getText().toString() : "";

            boolean ok = true;
            if (name.isEmpty()) { binding.nameInput.setError(getString(R.string.hint_name)); ok = false; } else binding.nameInput.setError(null);
            if (email.isEmpty()) { binding.emailInput.setError(getString(R.string.hint_email)); ok = false; } else binding.emailInput.setError(null);
            if (password.length() < 6) { binding.passwordInput.setError("Şifre en az 6 karakter olmalı"); ok = false; } else binding.passwordInput.setError(null);
            if (!password.equals(passwordConfirm)) { binding.passwordConfirmInput.setError("Şifreler eşleşmiyor"); ok = false; } else binding.passwordConfirmInput.setError(null);

            if (!ok) return;

                try {
                    // Save name/email locally so ProfileFragment can show them
                    try {
                        android.content.SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE);
                        prefs.edit().putString("pref_name", name).putString("pref_email", email).apply();
                    } catch (Exception ignored) {}

                    // insert user into Room users table
                    UserRepository ur = new UserRepository(requireContext());
                    UserEntity ue = new UserEntity(name, email, password);
                    ur.insert(ue, id -> requireActivity().runOnUiThread(() -> {
                        NavHostFragment.findNavController(RegisterFragment.this)
                                .navigate(R.id.action_register_to_login);
                    }));
                } catch (Exception ex) {
                    // ignore navigation failures
                }
        });

        // Clickable "Giriş yap" in the bottom text
        binding.registerLink.setOnClickListener(v -> {
            try {
                NavHostFragment.findNavController(RegisterFragment.this)
                        .navigate(R.id.action_register_to_login);
            } catch (Exception ex) {
                // ignore
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
}
