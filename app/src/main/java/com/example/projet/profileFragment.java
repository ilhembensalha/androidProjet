package com.example.projet;

import android.annotation.SuppressLint;
import android.app.usage.NetworkStats;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView userfullName;

    private FirebaseAuth firebaseAuth;

    DatabaseReference databaseProfile;


    private String your_text;


    public profileFragment(String your_text) {
        this.your_text = your_text;
    }

    TextView tvFirstName, tvLastName, tvEmail, tv_name;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static profileFragment newInstance(String param1, String param2) {
        profileFragment fragment = new profileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView tvLastName = (TextView) view.findViewById(R.id.tvLastName);
       // TextView tvFirstName = (TextView) getView().findViewById(R.id.tvFirstName);
        TextView tvFirstName = (TextView) view.findViewById(R.id.tvFirstName);
        TextView tvEmail = view.findViewById(R.id.tvEmail);
        TextView tv_name =  view.findViewById(R.id.tv_name);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        databaseProfile = FirebaseDatabase.getInstance().getReference("users").child(uid);

        databaseProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String firstName = (String) dataSnapshot.child("firstName").getValue();
                String lastName = (String) dataSnapshot.child("lastName").getValue();
                String email = (String) dataSnapshot.child("email").getValue();
                tvFirstName.setText(firstName);
                tvLastName.setText( lastName);
                tvEmail.setText(email);
                tv_name.setText(" " + lastName+" "+ firstName);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ImageView editprofil = (ImageView) view.findViewById(R.id.BTN);

        editprofil.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                final DialogPlus dialogPlus = DialogPlus.newDialog(getActivity())
                        .setContentHolder(new ViewHolder(R.layout.profile_edit))
                        .setExpanded(true,800)
                        .create();
                //dialogPlus.show();
                View view2 = dialogPlus.getHolderView();
                EditText nom = view2.findViewById(R.id.nameedit);
                EditText prenom = view2.findViewById(R.id.prenomedit);
                TextView tvLastName = (TextView) view.findViewById(R.id.tvLastName);
                TextView tvFirstName = (TextView) view.findViewById(R.id.tvFirstName);

                nom.setText(tvLastName.getText().toString());
                prenom.setText(tvFirstName.getText().toString());

                Button btnupdate = view2.findViewById(R.id.update);

                dialogPlus.show();
                btnupdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("firstName",nom.getText().toString());
                        map.put("lastName",prenom.getText().toString());
                        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        FirebaseDatabase.getInstance().getReference("users")
                                .child(user)
                                .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // successfully updated
                                dialogPlus.dismiss();

                            }
                        });

                    }
                });

            }
        });
        return  view;
    }


}