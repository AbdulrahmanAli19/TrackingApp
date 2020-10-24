package com.example.fadebook.ui.MainActivity.Home;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fadebook.R;
import com.example.fadebook.pojo.modules.Users;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> implements Filterable {

    private Context mContext;
    private List<Users> users;
    private List<Users> fullUsers;

    public UsersAdapter(Context mContext, List<Users> users) {
        this.mContext = mContext;
        this.users = users;
        fullUsers = new ArrayList<>(users);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.user_row, parent, false);
        UserViewHolder viewHolder = new UserViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Users currentUser = users.get(position);
        holder.txtName.setText(currentUser.getName());
        holder.txtEmail.setText(currentUser.getEmail());

        if (currentUser.getState()) {
            holder.txtState.setTextColor(mContext.getResources().getColor(R.color.green));
            holder.txtState.setText("Online");
        } else {
            holder.txtState.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.txtState.setText("Offline");
        }


        if (currentUser.getImageUrl().isEmpty()) {
            holder.progressBar.setVisibility(View.GONE);
            holder.imgUser.setVisibility(View.VISIBLE);
            holder.imgUser.setImageDrawable(mContext.getDrawable(R.drawable.ic_person));
        } else if (currentUser.getImageUrl() != null) {
            Picasso.get()
                    .load(Uri.parse(currentUser.getImageUrl()))
                    .into(holder.imgUser, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.GONE);
                            holder.imgUser.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.progressBar.setVisibility(View.GONE);
                            holder.txtState.setText(e.getMessage());
                        }
                    });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }

    private Filter myFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Users> filteredUsers = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0 || charSequence == "")
                filteredUsers.addAll(fullUsers);
            else {
                String pattern = charSequence.toString().toLowerCase().trim();
                for (int i = 0; i < fullUsers.size(); i++) {
                    if (fullUsers.get(i).getName().toLowerCase().trim().contains(pattern)) {
                        filteredUsers.add(fullUsers.get(i));
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredUsers;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            users.clear();
            users.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    class UserViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        CircleImageView imgUser;
        TextView txtName, txtEmail, txtState, txtError;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.user_image_progress);
            imgUser = itemView.findViewById(R.id.img_user);
            txtError = itemView.findViewById(R.id.txt_error);
            txtEmail = itemView.findViewById(R.id.txt_email);
            txtName = itemView.findViewById(R.id.txt_name);
            txtState = itemView.findViewById(R.id.txt_state);
        }
    }

}
