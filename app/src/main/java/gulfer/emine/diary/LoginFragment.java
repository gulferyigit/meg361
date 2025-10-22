package gulfer.emine.diary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        try {
            View bottomNav = requireActivity().findViewById(R.id.bottomNav);
            if (bottomNav != null) bottomNav.setVisibility(View.GONE);
        } catch (Exception ignored) {}
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText emailEdit = view.findViewById(R.id.emailEdit);
        TextInputEditText passwordEdit = view.findViewById(R.id.passwordEdit);
        TextInputLayout emailInput = view.findViewById(R.id.emailInput);
        TextInputLayout passwordInput = view.findViewById(R.id.passwordInput);
        MaterialButton loginButton = view.findViewById(R.id.loginButton);
        TextView registerLink = view.findViewById(R.id.registerLink);

        if (loginButton != null) {
            loginButton.setOnClickListener(v -> {
                String email = emailEdit != null && emailEdit.getText() != null ? emailEdit.getText().toString().trim() : "";
                String password = passwordEdit != null && passwordEdit.getText() != null ? passwordEdit.getText().toString() : "";

                boolean ok = true;
                if (email.isEmpty()) { if (emailInput!=null) emailInput.setError("E-posta girin"); ok = false; } else if (emailInput!=null) emailInput.setError(null);
                if (password.isEmpty()) { if (passwordInput!=null) passwordInput.setError("Şifre girin"); ok = false; } else if (passwordInput!=null) passwordInput.setError(null);
                if (!ok) return;

                try {
                    // check if email is registered in Room users table asynchronously
                    UserRepository ur = new UserRepository(requireContext());
                    ur.findByEmail(email, user -> {
                        requireActivity().runOnUiThread(() -> {
                            if (user == null) {
                                if (emailInput != null) emailInput.setError("Bu e-posta kayıtlı değil. Kayıt ol veya doğru e-postayı gir.");
                                return;
                            }
                            // persist email (and guess name if not present) so ProfileFragment can show current user
                            try {
                                android.content.SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE);
                                prefs.edit().putString("pref_email", email).apply();
                                // Always update displayed name on login (derive from email local-part)
                                String guess = email != null && email.contains("@") ? email.substring(0, email.indexOf("@")) : email;
                                if (guess == null || guess.isEmpty()) guess = "Kullanıcı";
                                if (guess.length() > 0) guess = guess.substring(0,1).toUpperCase() + (guess.length() > 1 ? guess.substring(1) : "");
                                prefs.edit().putString("pref_name", guess).apply();
                            } catch (Exception ignored) {}

                            NavHostFragment.findNavController(LoginFragment.this)
                                    .navigate(R.id.action_login_to_diaryList);
                        });
                    });
                } catch (Exception ignored) {}
            });
        }

        if (registerLink != null) {
            registerLink.setOnClickListener(v -> {
                try {
                    NavHostFragment.findNavController(LoginFragment.this)
                            .navigate(R.id.action_login_to_register);
                } catch (Exception ignored) {}
            });
        }
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

}
