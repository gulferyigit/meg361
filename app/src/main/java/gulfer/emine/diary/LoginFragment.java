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
                    // No account check: proceed to diary list on successful local validation
                    NavHostFragment.findNavController(LoginFragment.this)
                            .navigate(R.id.diaryListFragment);
                } catch (Exception ignored) {}
            });
        }

        if (registerLink != null) {
            registerLink.setOnClickListener(v -> {
                try {
                        NavHostFragment.findNavController(LoginFragment.this)
                                .navigate(R.id.diaryListFragment);
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
