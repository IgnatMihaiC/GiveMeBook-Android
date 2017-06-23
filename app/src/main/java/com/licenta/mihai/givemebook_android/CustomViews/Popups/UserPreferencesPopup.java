package com.licenta.mihai.givemebook_android.CustomViews.Popups;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.preference.Preference;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.licenta.mihai.givemebook_android.Adapters.Preferences.PreferenceGridAdapter;
import com.licenta.mihai.givemebook_android.Adapters.Preferences.PreferenceGridCell;
import com.licenta.mihai.givemebook_android.CustomViews.CustomText.BorderEditText;
import com.licenta.mihai.givemebook_android.CustomViews.CustomText.TextViewOpenSansBold;
import com.licenta.mihai.givemebook_android.CustomViews.RoundBorderButton;
import com.licenta.mihai.givemebook_android.Models.BaseModels.Preferences;
import com.licenta.mihai.givemebook_android.Models.NetModels.Replay.NetPreferences;
import com.licenta.mihai.givemebook_android.Models.NetModels.Response.NetStringResponse;
import com.licenta.mihai.givemebook_android.Network.RestClient;
import com.licenta.mihai.givemebook_android.R;
import com.licenta.mihai.givemebook_android.Singletons.User;
import com.licenta.mihai.givemebook_android.Utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mihai on 09.06.2017.
 */

public class UserPreferencesPopup {

    private Context context;
    private GridView gridView;
    private Dialog dialog;
    private DialogInterface.OnDismissListener dismissListener;
    private BorderEditText newPref;
    private RoundBorderButton addNewPref;

    private ArrayList<Boolean> selectedGridCells;
    private ArrayList<PreferenceGridCell> cells = new ArrayList<>();
    private List<NetPreferences> netPreferencesList = new ArrayList<>();
    private PreferenceGridAdapter preferenceGridAdapter;

    public UserPreferencesPopup(Context context, DialogInterface.OnDismissListener dismissListener) {
        this.context = context;
        this.dismissListener = dismissListener;
        preferenceGridAdapter = new PreferenceGridAdapter(this.context, cells);
    }

    public void init() {
        dialog = new Dialog(context, R.style.CustomAlertDialog);
        dialog.setContentView(R.layout.profile_preference);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnDismissListener(dismissListener);
        setUpGrid();
        setUpButtons();
    }

    private void setUpButtons() {
        newPref = (BorderEditText) dialog.findViewById(R.id.addPref_prefName);
        addNewPref = (RoundBorderButton) dialog.findViewById(R.id.addPref_prefButton);
        addNewPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!newPref.getText().toString().isEmpty()) {
                    cells.add(new PreferenceGridCell(newPref.getText().toString()));
                    preferenceGridAdapter.notifyDataSetChanged();
                    selectedGridCells.add(true);
                    newPref.setText("");
                }
            }
        });


        RoundBorderButton saveButtons = (RoundBorderButton) dialog.findViewById(R.id.userPreferencesUpdateButton);
        saveButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < selectedGridCells.size(); i++) {
                    if (selectedGridCells.get(i).equals(true))
                        netPreferencesList.add(new NetPreferences(cells.get(i).getPreferences().getPname()));
                }
                RestClient.networkHandler().updatePreferences(User.getInstance().getCurrentUser().getToken(), User.getInstance().getCurrentUser().getUid(), netPreferencesList)
                        .enqueue(new Callback<List<Preferences>>() {
                            @Override
                            public void onResponse(Call<List<Preferences>> call, Response<List<Preferences>> response) {
                                User.getInstance().getCurrentUser().getPreferences().clear();
                                User.getInstance().getCurrentUser().getPreferences().addAll(response.body());
                                Util.showToast(context, "Success");
                                dismissUserPopup();
                            }

                            @Override
                            public void onFailure(Call<List<Preferences>> call, Throwable t) {
                                Util.showToast(context, "Fail");
                            }
                        });
            }
        });
        RoundBorderButton dismissButton = (RoundBorderButton) dialog.findViewById(R.id.userPreferencesUpdateButton_dismiss);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissUserPopup();
            }
        });


    }

    public void populateGrid() {
        for (Preferences p : User.getInstance().getCurrentUser().getPreferences()) {
            cells.add(new PreferenceGridCell(p.getPname()));
        }
    }

    private void setUpGrid() {
        populateGrid();
        gridView = (GridView) dialog.findViewById(R.id.userPreferencesGridView);
        this.gridView.setAdapter(preferenceGridAdapter);
        selectedGridCells = new ArrayList<>(Collections.nCopies(cells.size(), true));
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextViewOpenSansBold item = (TextViewOpenSansBold) view.findViewById(R.id.gridPreferenceCellText);
                if (selectedGridCells.get(position).equals(true)) {
                    selectedGridCells.set(position, false);
                    item.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryLight));
                } else {
                    selectedGridCells.set(position, true);
                    item.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                }
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

    public void dismissUserPopup() {
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
