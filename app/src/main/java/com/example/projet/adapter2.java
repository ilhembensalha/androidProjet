package com.example.projet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class adapter2 extends FirebaseRecyclerAdapter<annonce,adapter2.myViewHolder> {
    @NonNull annonce model;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public adapter2(@NonNull FirebaseRecyclerOptions<annonce> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull adapter2.myViewHolder holder, final int position, @NonNull annonce model) {
        holder.etat.setText(model.getEtat());
        holder.name.setText(model.getName());
        holder.text.setText(model.getText());
        holder.prix.setText(model.getPrix());
        Glide.with(holder.img.getContext())
                .load(model.getImage())
                .placeholder(com.firebase.ui.database.R.drawable.googleg_disabled_color_18)
                .circleCrop()
                .error(com.google.firebase.appcheck.interop.R.drawable.googleg_disabled_color_18)
                .into(holder.img);
        Button delelet, update;
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if ((!model.getIdUser().equals(user))) {
            holder.card.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
            holder.update.setVisibility(View.GONE);
            holder.deltaRelative.setVisibility(View.GONE);


        } else {
            holder.delete.setVisibility(View.VISIBLE);
            holder.update.setVisibility(View.VISIBLE);
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
                builder.setTitle("are you sure");
                builder.setMessage("delete data ");
                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("annonces")
                                .child(getRef(position).getKey()).removeValue();

                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.name.getContext(), "cancel", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true, 1400)
                        .create();
                //dialogPlus.show();
                View view = dialogPlus.getHolderView();
                EditText name = view.findViewById(R.id.nameannonceupdate);
                EditText etat = view.findViewById(R.id.etatannonceupdate);
                EditText prix = view.findViewById(R.id.Prixannonceupdate);
                EditText img = view.findViewById(R.id.imgannonceupdate);
                EditText text = view.findViewById(R.id.Textannonceupdate);
                Button btnupdate = view.findViewById(R.id.update);
                name.setText(model.getName());
                etat.setText(model.getEtat());
                prix.setText(model.getPrix());
                img.setText(model.getImage());
                text.setText(model.getText());
                dialogPlus.show();
                btnupdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", name.getText().toString());
                        map.put("etat", etat.getText().toString());
                        map.put("prix", prix.getText().toString());
                        map.put("text", text.getText().toString());
                        map.put("image", img.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("annonces")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.name.getContext(), "update", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.name.getContext(), "err", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });

            }
        });
    }

    @NonNull
    @Override
    public adapter2.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.annonce_item, parent, false);

            return new adapter2.myViewHolder(view);

    }


    class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView name ,prix ,etat,text ;
        Button delete,update;
        CardView card;
        RelativeLayout deltaRelative;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img= (CircleImageView)itemView.findViewById(R.id.img1);
            name= (TextView)itemView.findViewById(R.id.namecard);
            prix= (TextView)itemView.findViewById(R.id.prix);
            etat= (TextView)itemView.findViewById(R.id.etat);
            text= (TextView)itemView.findViewById(R.id.text);
            card=(CardView)itemView.findViewById(R.id.card);
            deltaRelative=(RelativeLayout)itemView.findViewById(R.id.deltaRelative);
            delete= (Button) itemView.findViewById(R.id.delete);
            update= (Button)itemView.findViewById(R.id.BTN);

        }
    }

}
