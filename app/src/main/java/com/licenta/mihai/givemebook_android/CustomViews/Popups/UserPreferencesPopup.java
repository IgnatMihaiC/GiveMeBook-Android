package com.licenta.mihai.givemebook_android.CustomViews.Popups;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.licenta.mihai.givemebook_android.Adapters.Preferences.PreferenceGridAdapter;
import com.licenta.mihai.givemebook_android.Adapters.Preferences.PreferenceGridCell;
import com.licenta.mihai.givemebook_android.CustomViews.CustomText.TextViewOpenSansBold;
import com.licenta.mihai.givemebook_android.Models.BaseModels.Preferences;
import com.licenta.mihai.givemebook_android.R;

import java.util.ArrayList;
import java.util.Collections;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

/**
 * Created by mihai on 09.06.2017.
 */

public class UserPreferencesPopup {

    private Context context;
    private GridView gridView;
    private Dialog dialog;
    private DialogInterface.OnDismissListener dismissListener;

    private ArrayList<Boolean> selectedGridCells;

    public UserPreferencesPopup(Context context, DialogInterface.OnDismissListener dismissListener) {
        this.context = context;
        this.dismissListener = dismissListener;
    }

    public void init() {
        dialog = new Dialog(context, R.style.CustomAlertDialog);
        dialog.setContentView(R.layout.profile_preference);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnDismissListener(dismissListener);
        gridView = (GridView) dialog.findViewById(R.id.userPreferencesGridView);
        ArrayList<PreferenceGridCell> cells = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Preferences p = new Preferences();
            p.setPname("pref" + i);
            cells.add(new PreferenceGridCell(p));
        }
        PreferenceGridAdapter preferenceGridAdapter = new PreferenceGridAdapter(context, cells);
        this.gridView.setAdapter(preferenceGridAdapter);
        selectedGridCells = new ArrayList<>(Collections.nCopies(cells.size(), false));
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.w("PreffsGrid", "item " + position);
                TextViewOpenSansBold item = (TextViewOpenSansBold) view.findViewById(R.id.gridPreferenceCellText);
                if (selectedGridCells.get(position).equals(false)) {
                    selectedGridCells.set(position, true);
                    item.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                } else {
                    selectedGridCells.set(position, false);
                    item.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryLight));
                }
            }
        });

        CircularProgressButton saveButtons = (CircularProgressButton) dialog.findViewById(R.id.userPreferencesUpdateButton);
        saveButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDateDialog();
            }
        });
    }

    public void showUserPopup() {
        try {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissDateDialog() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                try {
                    dialog.dismiss();
                    dialog = null;
                } catch (final Exception e) {
                    e.printStackTrace();

                } finally {
                    dialog = null;
                }
            }
        }
    }
}
