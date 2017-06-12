package com.licenta.mihai.givemebook_android.Adapters.Books;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.licenta.mihai.givemebook_android.CustomViews.CustomText.TextViewOpenSansBold;
import com.licenta.mihai.givemebook_android.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mihai on 12.06.2017.
 */

public class BookGridAdapter extends BaseAdapter {


    private List<BookCell> bookCells;
    private Context context;


    public BookGridAdapter(Context context, List<BookCell> bookCells) {
        this.bookCells = bookCells;
        this.context = context;
    }


    @Override
    public int getCount() {
        return bookCells.size();
    }

    @Override
    public Object getItem(int position) {
        return bookCells.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cell = convertView;
        BookGridAdapter.CellHolder cellHolder = null;
        if (cell == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            cell = inflater.inflate(R.layout.book_item_layout, parent, false);

            cellHolder = new BookGridAdapter.CellHolder(cell);
            cell.setTag(cellHolder);
        } else {
            cellHolder = (BookGridAdapter.CellHolder) cell.getTag();
        }
        return cell;
    }

    private class CellHolder {

        ImageView bookImage;
        CircleImageView userImage;
        TextViewOpenSansBold bookTitle;

        public CellHolder(View view)

        {
            bookImage = (ImageView) view.findViewById(R.id.book_item_image);
            userImage = (CircleImageView) view.findViewById(R.id.book_item_image_user);
            bookTitle = (TextViewOpenSansBold) view.findViewById(R.id.book_item_name);
        }
    }

}
