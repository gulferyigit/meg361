package gulfer.emine.diary;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

public class ProfileFragment extends Fragment {

    private static final String PREFS = "app_prefs";
    private static final String KEY_NAME = "pref_name";
    private static final String KEY_EMAIL = "pref_email";
    private static final String KEY_AVATAR = "pref_avatar"; // "male" or "female"

    private TextView tvUsername;
    private TextView tvEmail;
    private ImageView ivAvatar;
    private ImageButton btnMale;
    private ImageButton btnFemale;
    private View itemThemeCard;

    private static final String KEY_THEME = "pref_theme"; // "light" or "dark"
    private SwitchCompat switchNotifications;
    private View itemHelp;
    private static final String KEY_NOTIF = "pref_notifications"; // boolean

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        ivAvatar = view.findViewById(R.id.ivAvatar);
        btnFemale = view.findViewById(R.id.btnAvatarFemale);
        btnMale = view.findViewById(R.id.btnAvatarMale);

        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String name = prefs.getString(KEY_NAME, "Kullanıcı Adı");
        String email = prefs.getString(KEY_EMAIL, "email@example.com");
        String avatar = prefs.getString(KEY_AVATAR, "female");

        tvUsername.setText(name);
        tvEmail.setText(email);

        applyAvatar(avatar);

        btnFemale.setOnClickListener(v -> {
            saveAvatar("female");
            applyAvatar("female");
        });

        btnMale.setOnClickListener(v -> {
            saveAvatar("male");
            applyAvatar("male");
        });

        // Theme selection
        try {
            itemThemeCard = view.findViewById(R.id.itemTheme);
            if (itemThemeCard != null) {
                itemThemeCard.setOnClickListener(v -> showThemeDialog());
            }
        } catch (Exception ignored) {}

        // Notifications switch
        try {
            switchNotifications = view.findViewById(R.id.switchNotifications);
            if (switchNotifications != null) {
                boolean on = prefs.getBoolean(KEY_NOTIF, true);
                switchNotifications.setChecked(on);
                switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    prefs.edit().putBoolean(KEY_NOTIF, isChecked).apply();
                    Toast.makeText(requireContext(), isChecked ? "Bildirimler açık" : "Bildirimler kapalı", Toast.LENGTH_SHORT).show();
                });
            }
        } catch (Exception ignored) {}

        // Help card
        try {
            itemHelp = view.findViewById(R.id.itemHelp);
            if (itemHelp != null) {
                itemHelp.setOnClickListener(v -> showHelpDialog());
            }
        } catch (Exception ignored) {}
    }

    private void saveAvatar(String which) {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_AVATAR, which).apply();
    }

    private void showThemeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialog = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_theme_choice, null);

        View cardLight = dialog.findViewById(R.id.cardLight);
        View cardDark = dialog.findViewById(R.id.cardDark);

        builder.setView(dialog);
        AlertDialog ad = builder.create();

        cardLight.setOnClickListener(v -> {
            applyThemeChoice("light");
            ad.dismiss();
        });

        cardDark.setOnClickListener(v -> {
            applyThemeChoice("dark");
            ad.dismiss();
        });

        ad.show();
    }

    private void applyThemeChoice(String which) {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_THEME, which).apply();
        if (which.equals("dark")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void showHelpDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(requireContext());
        b.setTitle("Yardım");
        b.setMessage("Yardım için destek@orneksite.com adresine e-posta atabilirsiniz. Uygulama içi hızlı destek yakında eklenecek.");
        b.setPositiveButton("Tamam", (d, w) -> d.dismiss());
        b.show();
    }

    private void applyAvatar(String which) {
        if (which == null) which = "female";
        if (which.equals("male")) {
            ivAvatar.setImageResource(R.drawable.ic_avatar_male);
            btnMale.setSelected(true);
            btnFemale.setSelected(false);
        } else {
            ivAvatar.setImageResource(R.drawable.ic_avatar_female);
            btnFemale.setSelected(true);
            btnMale.setSelected(false);
        }
    }

}
