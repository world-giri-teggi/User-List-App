package com.userlistapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<User> userList;
    private List<User> filteredUserList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        this.filteredUserList = userList; // Initialize with the original list
    }

    @Override
    public int getCount() {
        return filteredUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        }

        User user = filteredUserList.get(position);

        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView emailTextView = convertView.findViewById(R.id.emailTextView);
        ImageView avatarImageView = convertView.findViewById(R.id.avatarImageView);

        nameTextView.setText(user.getFirstName() + " " + user.getLastName());
        emailTextView.setText(user.getEmail());

        Glide.with(context)
                .load(user.getAvatar())
                .into(avatarImageView);

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = userList;
                    results.count = userList.size();
                } else {
                    List<User> filteredList = new ArrayList<>();
                    for (User user : userList) {
                        if (user.getFirstName().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                user.getLastName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredList.add(user);
                        }
                    }
                    results.values = filteredList;
                    results.count = filteredList.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredUserList = (List<User>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    // Update the user list and refresh the view
    public void updateData(List<User> newUserList) {
        userList.clear();
        userList.addAll(newUserList);
        filteredUserList = newUserList;
        notifyDataSetChanged();
    }
}
