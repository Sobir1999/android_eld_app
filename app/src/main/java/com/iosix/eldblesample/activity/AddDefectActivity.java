package com.iosix.eldblesample.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_defect;
    }

    @Override
    public void initView() {
        super.initView();

        boolean isTruckSelected = getIntent().getBooleanExtra("isTruckSelected", false);
        boolean isUnitSelected = getIntent().getBooleanExtra("isUnitSelected", false);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewpager);
        ImageView backButton = findViewById(R.id.idImageBack);
        TextView save = findViewById(R.id.save);
        backButton.setOnClickListener(v -> onBackPressed());
        save.setOnClickListener(v -> createDialog(v.getContext()));

        FragmentManager fragmentManager = getSupportFragmentManager();
        DefectsFragmentAdapter adapter = new DefectsFragmentAdapter(fragmentManager, getLifecycle(), isTruckSelected,isUnitSelected);
        viewPager.setAdapter(adapter);

        if (isUnitSelected) tabLayout.addTab(tabLayout.newTab().setText("Unit"));
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

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_notes_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView cancel = dialog.findViewById(R.id.idNotesDialogCancel);
        TextView ok = dialog.findViewById(R.id.idNotesDialogSend);
        EditText note = dialog.findViewById(R.id.idNotesDialogEdit);

        cancel.setOnClickListener(v->{
            dialog.dismiss();
        });

        ok.setOnClickListener(v->{
            dialog.dismiss();
            if (!note.getText().toString().equals("")){
                back(note.getText().toString());
            }else {
                Toast.makeText(context,"Please add note to defects",Toast.LENGTH_SHORT).show();
            }
        });

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
    boolean isTruckSelected;
    boolean isUnitSelected;

    public DefectsFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, boolean isTruckSelected,boolean isUnitSelected) {
        super(fragmentManager, lifecycle);
        this.isTruckSelected = isTruckSelected;
        this.isUnitSelected = isUnitSelected;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            if (isUnitSelected){
                return new AddDefectFragment(1);
            }else return new AddDefectFragment(2);
        }
        return new AddDefectFragment(2);

    }

    @Override
    public int getItemCount() {
        return isTruckSelected && isUnitSelected ? 2 : 1;
    }
}