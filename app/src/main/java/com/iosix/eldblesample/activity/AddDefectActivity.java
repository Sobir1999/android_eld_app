package com.iosix.eldblesample.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.fragments.AddDefectFragment;

import static com.iosix.eldblesample.MyApplication.userData;

public class AddDefectActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private boolean isTruckSelected;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_defect;
    }

    @Override
    public void initView() {
        super.initView();

        isTruckSelected = getIntent().getBooleanExtra("isTruckSelected", false);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewpager);
        ImageView backButton = findViewById(R.id.idImageBack);
        TextView save = findViewById(R.id.save);
        backButton.setOnClickListener(v -> onBackPressed());
        save.setOnClickListener(v -> createDialog(v.getContext()));

        FragmentManager fragmentManager = getSupportFragmentManager();
        DefectsFragmentAdapter adapter = new DefectsFragmentAdapter(fragmentManager, getLifecycle(), isTruckSelected);
        viewPager.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("Unit"));
        if (isTruckSelected) tabLayout.addTab(tabLayout.newTab().setText("Trailer"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    private void createDialog(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Add a note to your defect");

        View v = getLayoutInflater().inflate(R.layout.add_unit_trailer_dialog_view, null, false);
        EditText editText = v.findViewById(R.id.idAddUnitTrailerEdit);
        editText.setHint("Notes");

        dialog.setPositiveButton("OK", (dialog12, which) -> {
            if (!editText.getText().toString().equalsIgnoreCase("")) {
                back(editText.getText().toString());
            }
        });

        dialog.setNegativeButton("Cancel", (dialog1, which) -> dialog1.cancel());

        dialog.setView(v);
        dialog.show();
    }

    private void back(String note) {
        String unitDefects = userData.getDefects(1);
        String trailerDefects = userData.getDefects(2);

        Intent intent = getIntent();
        if (unitDefects.equals("") && trailerDefects.equals("")) {
            setResult(RESULT_CANCELED, intent);
        } else {
            intent.putExtra("unitDefects", userData.getDefects(1));
            intent.putExtra("trailerDefects", userData.getDefects(2));
            intent.putExtra("notes", note);
            setResult(RESULT_OK, intent);
            userData.clearDefects(1);
            userData.clearDefects(2);
        }
        finish();
    }

}

class DefectsFragmentAdapter extends FragmentStateAdapter {
    boolean isTruckSelected = false;

    public DefectsFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, boolean isTruckSelected) {
        super(fragmentManager, lifecycle);
        this.isTruckSelected = isTruckSelected;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new AddDefectFragment(1);
        }
        return new AddDefectFragment(2);

    }

    @Override
    public int getItemCount() {
        return isTruckSelected ? 2 : 1;
    }
}