package com.licenta.mihai.givemebook_android.Adapters.Friend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.licenta.mihai.givemebook_android.CustomViews.CustomText.TextViewOpenSans;
import com.licenta.mihai.givemebook_android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mihai on 12.06.2017.
 */

public class FriendRecyclerAdapterAdapter extends RecyclerView.Adapter<FriendRecyclerAdapterAdapter.CellHolder> {

    private List<FriendListCell> friendListCells;
    private Context context;

    private CustomItemClickListener customItemClickListener;

    public FriendRecyclerAdapterAdapter(Context context, List<FriendListCell> friendListCells, CustomItemClickListener listener) {
        this.friendListCells = new ArrayList<>();
        this.friendListCells = friendListCells;
        this.context = context;
        this.customItemClickListener = listener;
    }


    @Override
    public CellHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FriendListCell friendListCell = friendListCells.get(viewType);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cell = inflater.inflate(R.layout.friend_list_element, parent, false);
        final CellHolder cellHolder = new CellHolder(cell);
        cellHolder.userName.setText(friendListCell.getUserName());
        Picasso.with(context).load("http://" + friendListCell.getPhotoUrl()).into(cellHolder.userPhoto);
        cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customItemClickListener.onItemClick(v, cellHolder.getPosition());
            }
        });
        return cellHolder;
    }

    @Override
    public void onBindViewHolder(CellHolder holder, int position) {

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return friendListCells.size();
    }


    public class CellHolder extends RecyclerView.ViewHolder {
        TextViewOpenSans userName;
        CircleImageView userPhoto;

        public CellHolder(View view) {
            super(view);
            userName = (TextViewOpenSans) view.findViewById(R.id.friend_list_userName);
            userPhoto = (CircleImageView) view.findViewById(R.id.friend_list_userPhoto);
        }
    }

    public interface CustomItemClickListener {
        void onItemClick(View v, int position);
    }
}
