package com.example.progass2.controller;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progass2.R;
import com.example.progass2.model.DatabaseHelper;
import com.example.progass2.model.Profile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class MainActivity extends AppCompatActivity implements DialogFragment.DialogListener {

    private DatabaseHelper db;
    private Toolbar toolbar;
    private FloatingActionButton floatingButton;
    private RecyclerView recyclerView;
    private List<Profile> profileList;
    private ProfileAdapter profileAdapter;
    private boolean isSortedByName = true;
    private TextView listOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profiles");
        floatingButton = findViewById(R.id.floatingButton);
        floatingButton.setOnClickListener(v -> {
            DialogFragment dialogFragment = new DialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "DialogFragment");
        });

        db = new DatabaseHelper(this);
        profileList = db.getAllProfilesByName();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        profileAdapter = new ProfileAdapter(this, profileList);
        recyclerView.setAdapter(profileAdapter);
        listOrder = findViewById(R.id.listOrder);

        loadProfiles();
    }

    @Override
    public void onProfileAdded() {
        Toast.makeText(this, "New profile added", Toast.LENGTH_SHORT).show();
        loadProfiles();
    }

    private void loadProfiles() {
        List<Profile> newProfiles;
        if (isSortedByName) {
            newProfiles = db.getAllProfilesByName();
        } else {
            newProfiles = db.getAllProfilesById();
        }
        profileList.clear();
        profileList.addAll(newProfiles);

        profileAdapter.notifyDataSetChanged();
        int count = profileList.size();
        String sortOrder = isSortedByName ? "name" : "ID";
        String statusText = "Showing " + count + " profiles, sorted by " + sortOrder;
        listOrder.setText(statusText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.toggle_view) {
            isSortedByName = !isSortedByName;
            loadProfiles();
            String sortOrder = isSortedByName ? "name" : "ID";
            Toast.makeText(this, "Sorted by " + sortOrder, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

        private final Context context;
        private final List<Profile> profileList;

        public class ProfileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView tvName, tvId;

            public ProfileViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvProfileName);
                tvId = itemView.findViewById(R.id.tvProfileId);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Profile clickedProfile = profileList.get(position);
                    Toast.makeText(context, "Clicked on " + clickedProfile.getStudentId(), Toast.LENGTH_SHORT).show();
            }
        }

    }
        public ProfileAdapter(Context context, List<Profile> profileList) {
            this.context = context;
            this.profileList = profileList;
        }

        @NonNull
        @Override
        public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.item_profile, parent, false);
            return new ProfileViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
            Profile profile = profileList.get(position);
            String positionText = (position + 1) + ".";
            if (isSortedByName) {
                String fullName = profile.getSurname() + ", " + profile.getName();
                holder.tvName.setText(fullName);
            }else {
                holder.tvId.setText("ID: " + profile.getStudentId());
            }
        }

        @Override
        public int getItemCount() {
            // Return the total number of items in the list
            return profileList.size();
        }
    }
}
