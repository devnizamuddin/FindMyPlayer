package com.example.findmyplayer.Auth;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.findmyplayer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForgetPasswordFragment extends Fragment {

    private Context context;
    private TextInputEditText email_et;
    private Button send_mail_btn;
    private FirebaseAuth firebaseAuth;


    public ForgetPasswordFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_forget_password, container, false);

        email_et = view.findViewById(R.id.email_et);
        send_mail_btn = view.findViewById(R.id.send_mail_btn);

        firebaseAuth = FirebaseAuth.getInstance();

        send_mail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPasswordResetEmail();
            }
        });


        return view;
    }

    private void sendPasswordResetEmail() {

        String email = email_et.getText().toString();

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    Toast.makeText(context, "Email send", Toast.LENGTH_SHORT).show();
                    changeFragment(new LoginFragment());

                }
                else {
                    Toast.makeText(context, ""+task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.container_auth, fragment);
        fragmentTransaction.commit();

    }

}
