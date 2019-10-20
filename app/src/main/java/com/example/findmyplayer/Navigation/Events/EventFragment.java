package com.example.findmyplayer.Navigation.Events;


import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.findmyplayer.Adapter.EventAdapter;
import com.example.findmyplayer.PoJo.EventPoJo;
import com.example.findmyplayer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment implements EventAdapter.EventClickListener {

    private Context context;
    private FloatingActionButton add_event_fab;
    private RecyclerView event_rv;
    private DatabaseReference databaseReference;

    private static int year, month, day;
    private Calendar calendar;
    private ArrayList<EventPoJo>eventPoJos;
    private EventAdapter eventAdapter;

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_event, container, false);

        add_event_fab = view.findViewById(R.id.add_event_fab);
        event_rv = view.findViewById(R.id.event_rv);
        databaseReference = FirebaseDatabase.getInstance().getReference("Event");

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        eventPoJos = new ArrayList<>();
        eventAdapter = new EventAdapter(eventPoJos,context,this);

        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, int itemPosition, @NonNull RecyclerView parent) {
                super.getItemOffsets(outRect, itemPosition, parent);
                outRect.set(0,0,0,20);
            }
        };
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        event_rv.addItemDecoration(itemDecoration);
        event_rv.setLayoutManager(layoutManager);
        event_rv.setAdapter(eventAdapter);

        
        add_event_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                createEvent();
            }
        });

        getDataFromDataBase();
        return view;
    }

    private void getDataFromDataBase() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    eventPoJos.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()){

                        EventPoJo eventPoJo = data.getValue(EventPoJo.class);
                        eventPoJos.add(eventPoJo);

                    }
                    eventAdapter.updateData(eventPoJos);
                }
                else {
                    Toast.makeText(context, "No Event Exists", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(context, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void createEvent() {

       final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context).inflate(R.layout.create_event_layout,null,false);
        builder.setView(view);
        final AlertDialog dialog = builder.show();


        final EditText date_et = view.findViewById(R.id.date_et);

        final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE dd, MMM, yyyy");
                calendar.set(year,month,dayOfMonth);
                String finalDate = sdf.format(calendar.getTime());
                date_et.setText(finalDate);
            }
        };

        date_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog =new DatePickerDialog(context,dateListener,year,month,day);
                datePickerDialog.show();

            }
        });


        Button create_event_btn = view.findViewById(R.id.create_event_btn);

        //Spinner.............................
        final Spinner sports_sp = view.findViewById(R.id.sports_sp);
        final Spinner address_sp = view.findViewById(R.id.address_sp);

        ArrayAdapter<CharSequence> addressArrayAdapter,sportsArrayAdapter;
        addressArrayAdapter = ArrayAdapter.
                createFromResource(context, R.array.location_array, android.R.layout.simple_list_item_1);
        addressArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sportsArrayAdapter = ArrayAdapter.
                createFromResource(context, R.array.sports, android.R.layout.simple_list_item_1);
        sportsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        address_sp.setAdapter(addressArrayAdapter);
        sports_sp.setAdapter(sportsArrayAdapter);
        //......................................Spinner

        create_event_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = databaseReference.push().getKey();
                String date = date_et.getText().toString();
                String sports = sports_sp.getSelectedItem().toString();
                String address = address_sp.getSelectedItem().toString();

                EventPoJo eventPoJo = new EventPoJo(id,sports,address,date);

                databaseReference.child(id).setValue(eventPoJo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){

                            Toast.makeText(context, "Event Added", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, ""+task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                Toast.makeText(context, ""+date, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


    }

    @Override
    public void onClickEvent(EventPoJo eventPoJo) {

    }
}
