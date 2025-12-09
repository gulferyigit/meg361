package gulfer.emine.diary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View btn = view.findViewById(R.id.button_first);
        if (btn != null) {
            btn.setOnClickListener(v -> {
                try {
            NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.diaryListFragment);
                } catch (Exception ignored) {}
            });
        }
    }

}