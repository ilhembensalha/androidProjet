package com.example.projet;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.res.Configuration;
import android.os.Bundle;


import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextView tvFirstName, tvLastName, tvEmail, tv_name;
    private profileFragment your_fragment;
    RecyclerView recyclerView ;
    adapterAnonnce adapterAnonnce;
    public ActionBarDrawerToggle actionBarDrawerToggle;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        final DrawerLayout drawerLayout = findViewById(R.id.my_drawer_layout);

        findViewById(R.id.imenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        NavigationView NAvigationView = findViewById(R.id.navigation);
        NAvigationView.setItemIconTintList(null);

        NavController navController = Navigation.findNavController(this, R.id.NavHostFragment);
        NavigationUI.setupWithNavController(NAvigationView, navController);


        NAvigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(menuItem -> {
            logoutUser();
            SearchView searchView = findViewById(R.id.floating_search_view);
            searchView.setVisibility(View.GONE);
            return true;

        });


        // Initialize Firebase Auth
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        your_fragment = new profileFragment("pass the string value here");


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {

                    View header = NAvigationView.getHeaderView(0);
                    TextView ilhem = (TextView) header.findViewById(R.id.nometprenom);
                    TextView mail = (TextView) header.findViewById(R.id.mail);
                    ilhem.setText(""+user.firstName + "  " + user.lastName);
                    mail.setText(user.email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.rv);



            SearchView searchView = findViewById(R.id.floating_search_view);
            searchView.setVisibility(View.VISIBLE);
            // perform set on query text listener event
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    txtsearch(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    txtsearch(query);
                    return false;
                }
            });


        }


    private void txtsearch(String str){
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        FirebaseRecyclerOptions<annonce> options =
                new FirebaseRecyclerOptions.Builder<annonce>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("annonces").orderByChild("name").startAt(str).endAt(str+"~"),annonce.class)
                        .build();
        adapterAnonnce =  new adapterAnonnce(options);
        adapterAnonnce.startListening();
        recyclerView.setAdapter(adapterAnonnce);

    }

    private void logoutUser() {
       FirebaseAuth.getInstance().signOut();
       Intent intent = new Intent(this, LoginActivity.class);
       startActivity(intent);
       finish();
   }
}