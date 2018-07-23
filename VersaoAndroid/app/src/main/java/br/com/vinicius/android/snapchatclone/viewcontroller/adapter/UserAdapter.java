package br.com.vinicius.android.snapchatclone.viewcontroller.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.vinicius.android.snapchatclone.R;
import br.com.vinicius.android.snapchatclone.model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserAdapterHolder>{

    private List<User> users;
    private Context mContext;

    public UserAdapter(Context context, List<User> users){

        this.mContext = context;
        this.users = users;

    }

    @NonNull
    @Override
    public UserAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemViewLista = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.list_users_adapter,
                        parent,
                        false
                );

        return new UserAdapterHolder(itemViewLista);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapterHolder holder, int position) {

        User user = users.get(position);
        holder.mTextName.setText(user.getName());
        holder.mTextEmail.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class UserAdapterHolder extends RecyclerView.ViewHolder{

        TextView mTextEmail;
        TextView mTextName;

        public UserAdapterHolder(View itemView) {
            super(itemView);

            mTextName = itemView.findViewById(R.id.textNameUser);
            mTextEmail = itemView.findViewById(R.id.textEmailUser);

        }
    }

}
