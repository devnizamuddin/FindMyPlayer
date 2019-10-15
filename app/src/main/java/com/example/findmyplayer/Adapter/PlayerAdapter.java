package com.example.findmyplayer.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findmyplayer.PoJo.UserPoJo;
import com.example.findmyplayer.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private Context context;
    private ArrayList<UserPoJo>userPoJos;
    private OnClickPlayerListener onClickPlayerListener;

    public PlayerAdapter(Context context, ArrayList<UserPoJo> userPoJos, OnClickPlayerListener onClickPlayerListener) {
        this.context = context;
        this.userPoJos = userPoJos;
        this.onClickPlayerListener = onClickPlayerListener;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.player_single_layout,parent,false);

        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {

        UserPoJo userPoJo = userPoJos.get(position);
        holder.name_tv.setText(userPoJo.getName());
        holder.phone_tv.setText(userPoJo.getPhone());
        Uri uri = Uri.parse(userPoJo.getProfile_img_url());
        Picasso.get().load(uri).into(holder.profile_iv);

    }

    @Override
    public int getItemCount() {
        return userPoJos.size();
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {

        TextView name_tv,phone_tv;
        Button hire_btn;
        ImageView profile_iv;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);

            name_tv = itemView.findViewById(R.id.name_tv);
            phone_tv = itemView.findViewById(R.id.phone_tv);
            hire_btn = itemView.findViewById(R.id.hire_btn);
            profile_iv = itemView.findViewById(R.id.profile_iv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onClickPlayerListener.onClickPlayer(userPoJos.get(getAdapterPosition()));
                }
            });


        }
    }

    public void updatePlayer(ArrayList<UserPoJo>userPoJos){

        this.userPoJos = userPoJos;
        notifyDataSetChanged();

    }

    public interface OnClickPlayerListener{

        void onClickPlayer(UserPoJo userPoJo);
    }
}
