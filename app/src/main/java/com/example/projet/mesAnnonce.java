package com.example.projet;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link mesAnnonce#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mesAnnonce extends Fragment {
    RecyclerView   recyclerView ;
    adapter2 adapterAnonnce;
    FloatingActionButton floatingActionButton;
    annonce model;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public mesAnnonce() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mesAnnonce.
     */
    // TODO: Rename and change types and number of parameters
    public static mesAnnonce newInstance(String param1, String param2) {
        mesAnnonce fragment = new mesAnnonce();
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
    @Override
    public void onStart() {
        super.onStart();
        adapterAnonnce.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterAnonnce.stopListening();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_mes_annonce, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        floatingActionButton=(FloatingActionButton) view.findViewById(R.id.add);
        recyclerView.setHasFixedSize(true);


            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));


        FirebaseRecyclerOptions<annonce> options =
                new FirebaseRecyclerOptions.Builder<annonce>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("annonces") ,annonce.class)
                        .build();
        adapterAnonnce =  new adapter2(options);
        recyclerView.setAdapter(adapterAnonnce);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                final DialogPlus dialogPlus = DialogPlus.newDialog(getActivity())
                        .setContentHolder(new ViewHolder(R.layout.add_pop))
                        .setExpanded(true,1400)
                        .create();
                //dialogPlus.show();
                View view = dialogPlus.getHolderView();
                EditText name = view.findViewById(R.id.nameannonceupdate);
                EditText etat = view.findViewById(R.id.etatannonceupdate);
                EditText prix = view.findViewById(R.id.Prixannonceupdate);
                EditText img = view.findViewById(R.id.imgannonceupdate);
                EditText text = view.findViewById(R.id.Textannonceupdate);
                Button btnupdate = view.findViewById(R.id.update);

                dialogPlus.show();
                btnupdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("name",name.getText().toString());
                        map.put("etat",etat.getText().toString());
                        map.put("prix",prix.getText().toString());
                        map.put("text",text.getText().toString());
                        map.put("image",img.getText().toString());
                        map.put("idUser", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        FirebaseDatabase.getInstance().getReference().child("annonces")
                                .push()
                                .setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });

            }
        });

        return view ;

    }


}
