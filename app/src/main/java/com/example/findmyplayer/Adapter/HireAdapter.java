package com.example.findmyplayer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findmyplayer.PoJo.HirePoJo;
import com.example.findmyplayer.R;

import java.util.ArrayList;

public class HireAdapter extends RecyclerView.Adapter<HireAdapter.HireViewHolder> {

    private Context context;
    private HireItemClickListener hireItemClickListener;
    private ArrayList<HirePoJo>hirePoJos;

    public HireAdapter(Context context, HireItemClickListener hireItemClickListener, ArrayList<HirePoJo> hirePoJos) {
        this.context = context;
        this.hireItemClickListener = hireItemClickListener;
        this.hirePoJos = hirePoJos;
    }

    @NonNull
    @Override
    public HireViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.news_feed_single_layout,parent,false);
        return new HireViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HireViewHolder holder, int position) {

        HirePoJo hirePoJo = hirePoJos.get(position);
        holder.hire_tv.setText(hirePoJo.getRecruiterName()+" wants to hire you");

    }

    @Override
    public int getItemCount() {
        return hirePoJos.size();
    }

    public class HireViewHolder extends RecyclerView.ViewHolder {

        TextView hire_tv;

        public HireViewHolder(@NonNull View itemView) {
            super(itemView);

            hire_tv = itemView.findViewById(R.id.hire_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hireItemClickListener.onClickHireItem(hirePoJos.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface HireItemClickListener{

        void onClickHireItem(HirePoJo hirePoJo);
    }

    public void updateHireList(ArrayList<HirePoJo>hirePoJos){

        this.hirePoJos = hirePoJos;
        notifyDataSetChanged();

    }
}
