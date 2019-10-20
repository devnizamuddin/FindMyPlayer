package com.example.findmyplayer.Navigation;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findmyplayer.PoJo.UserPoJo;
import com.example.findmyplayer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    private Context context;
    private ImageView profile_iv;
    private TextView name_tv, email_tv,
            phone_tv, address_tv, gender_tv, sports_tv, category_tv, history_tv;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private DatabaseReference databaseReference;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment getInstance(String userId) {

        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(bundle);
        return profileFragment;

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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profile_iv = view.findViewById(R.id.profile_iv);
        name_tv = view.findViewById(R.id.name_tv);
        email_tv = view.findViewById(R.id.email_tv);
        phone_tv = view.findViewById(R.id.phone_tv);
        address_tv = view.findViewById(R.id.address_tv);
        gender_tv = view.findViewById(R.id.gender_tv);
        sports_tv = view.findViewById(R.id.sports_tv);
        category_tv = view.findViewById(R.id.category_tv);
        history_tv = view.findViewById(R.id.history_tv);



       /* firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getUid();*/
        try {
            userId = getArguments().getString("userId");
        } catch (Exception e) {
        }


        databaseReference = FirebaseDatabase.getInstance().getReference("PlayerInfo");

        getDataFromFireBase();


        return view;
    }

    private void getDataFromFireBase() {


        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                    UserPoJo userPoJo = dataSnapshot.getValue(UserPoJo.class);
                    setUpLayout(userPoJo);
                } else {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setUpLayout(UserPoJo userPoJo) {

        Uri uri = Uri.parse(userPoJo.getProfile_img_url());
        Picasso.get().load(uri).into(profile_iv);
        name_tv.setText(userPoJo.getName());
        email_tv.setText(userPoJo.getEmail());
        phone_tv.setText(userPoJo.getPhone());
        address_tv.setText(userPoJo.getAddress());
        gender_tv.setText(userPoJo.getGender());
        sports_tv.setText(userPoJo.getSports());
        category_tv.setText(userPoJo.getPlayerType());
        history_tv.setText(userPoJo.getHistory());


    }

}
