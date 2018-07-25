package br.com.vinicius.android.snapchatclone.ViewController.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.vinicius.android.snapchatclone.Model.Snap;
import br.com.vinicius.android.snapchatclone.R;

public class SnapAdapter extends RecyclerView.Adapter<SnapAdapter.SnapAdapterHolder>{

    private List<Snap> snaps;
    private Context mContext;

    public SnapAdapter(Context mContext, List<Snap> snaps){

        this.mContext = mContext;
        this.snaps = snaps;

    }

    @NonNull
    @Override
    public SnapAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemViewLista = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.list_snaps_adapter,
                        parent,
                        false
                );

        return new SnapAdapter.SnapAdapterHolder(itemViewLista);
    }

    @Override
    public void onBindViewHolder(@NonNull SnapAdapterHolder holder, int position) {

        Snap snap = snaps.get(position);
        holder.textName.setText(snap.getName());

    }

    @Override
    public int getItemCount() {
        return snaps.size();
    }


    public class SnapAdapterHolder extends RecyclerView.ViewHolder{

        TextView textName;

        public SnapAdapterHolder(View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.textNameUser);

        }
    }


}
