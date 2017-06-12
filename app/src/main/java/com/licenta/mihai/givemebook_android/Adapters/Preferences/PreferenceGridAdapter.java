package com.licenta.mihai.givemebook_android.Adapters.Preferences;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.licenta.mihai.givemebook_android.CustomViews.CustomText.TextViewOpenSansBold;
import com.licenta.mihai.givemebook_android.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mihai on 09.06.2017.
 */

public class PreferenceGridAdapter extends BaseAdapter {

    private ArrayList<PreferenceGridCell> gridCells;
    private ArrayList<Boolean> selectedGridCells;
    private Context context;


    public PreferenceGridAdapter(Context context, ArrayList<PreferenceGridCell> gridCells) {
        this.gridCells = gridCells;
        selectedGridCells = new ArrayList<>(Collections.nCopies(gridCells.size(), false));
        this.context = context;
    }


    @Override
    public int getCount() {
        return gridCells.size();
    }

    @Override
    public Object getItem(int position) {
        return gridCells.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cell = convertView;
        CellHolder cellHolder = null;
        if (cell == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            cell = inflater.inflate(R.layout.preference_cell_layout, parent, false);

            cellHolder = new CellHolder(cell);
            cell.setTag(cellHolder);
        } else {
            cellHolder = (CellHolder) cell.getTag();
        }
        cellHolder.category.setText(gridCells.get(position).getPreferences().getPname().toUpperCase());
        return cell;
    }


    private class CellHolder {
        TextViewOpenSansBold category;

        public CellHolder(View view) {
            category = (TextViewOpenSansBold) view.findViewById(R.id.gridPreferenceCellText);
        }
    }
}
