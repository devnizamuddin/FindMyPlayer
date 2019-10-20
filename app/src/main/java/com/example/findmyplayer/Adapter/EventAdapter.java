package com.example.findmyplayer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findmyplayer.PoJo.EventPoJo;
import com.example.findmyplayer.R;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private ArrayList<EventPoJo>eventPoJos;
    private Context context;
    private EventClickListener eventClickListener;

    public EventAdapter(ArrayList<EventPoJo> eventPoJos, Context context, EventClickListener eventClickListener) {
        this.eventPoJos = eventPoJos;
        this.context = context;
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.event_single_layout,parent,false);

        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        EventPoJo eventPoJo = eventPoJos.get(position);
        holder.sports_location_tv.setText(eventPoJo.getSports()+" Match at "+eventPoJo.getLocation());
        holder.date_tv.setText(eventPoJo.getDate());

    }

    @Override
    public int getItemCount() {
        return eventPoJos.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

        TextView sports_location_tv,date_tv;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            sports_location_tv = itemView.findViewById(R.id.sports_location_tv);
            date_tv = itemView.findViewById(R.id.date_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eventClickListener.onClickEvent(eventPoJos.get(getAdapterPosition()));
                }
            });

        }
    }
    public interface EventClickListener{
        void onClickEvent(EventPoJo eventPoJo);
    }

    public void updateData(ArrayList<EventPoJo>eventPoJos){

        this.eventPoJos = eventPoJos;
        notifyDataSetChanged();

    };
}
