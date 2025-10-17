package com.example.progass2.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progass2.R;
import com.example.progass2.model.Access;
import com.example.progass2.model.DatabaseHelper;
import com.example.progass2.model.Profile;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DatabaseHelper db;
    private TextView tvProfileName, tvProfileSurname, tvProfileId, tvProfileGpa, tvProfileCreationDate;
    private Button deleteButton;
    private RecyclerView accessRecyclerView;
    private AccessLogAdapter accessLogAdapter;
    private List<Access> accessHistoryList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ProfilesActivity");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        db = new DatabaseHelper(this);
        tvProfileId = findViewById(R.id.tvProfileId);
        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileSurname = findViewById(R.id.tvProfileSurname);
        tvProfileGpa = findViewById(R.id.tvProfileGpa);
        tvProfileCreationDate = findViewById(R.id.tvCreationDate);
        deleteButton = findViewById(R.id.deleteButton);
        accessRecyclerView = findViewById(R.id.accessRecyclerView);

        int profileId = getIntent().getIntExtra("PROFILE_ID", -1);

        if (profileId != -1) {
            db.addAccess(new Access(profileId, "opened"));
            Profile profile = db.getProfileById(profileId);
            if (profile != null) {
                tvProfileId.setText(String.valueOf(profile.getStudentId()));
                tvProfileName.setText(profile.getName());
                tvProfileSurname.setText(profile.getSurname());
                tvProfileGpa.setText(String.valueOf(profile.getGpa()));
                tvProfileCreationDate.setText(profile.getCreationDate());
                accessHistoryList = db.getAccessHistoryForProfile(profileId);
                accessRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                accessLogAdapter = new AccessLogAdapter(this, accessHistoryList);
                accessRecyclerView.setAdapter(accessLogAdapter);
            } else {
                Toast.makeText(this, "Profile not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Error: Invalid profile ID", Toast.LENGTH_SHORT).show();
            finish();
        }

        deleteButton.setOnClickListener(v -> {
            if (profileId != -1) {
                db.deleteProfile(profileId);
                Toast.makeText(ProfileActivity.this, "Profile deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(ProfileActivity.this, "Error: Cannot delete profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        int profileId = getIntent().getIntExtra("PROFILE_ID", -1);
        if (profileId != -1) {
            if(db.profileExists(profileId)){
                db.addAccess(new Access(profileId, "closed"));
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class AccessLogAdapter extends RecyclerView.Adapter<AccessLogAdapter.AccessViewHolder> {

        private final Context context;
        private final List<Access> accessList;

        public class AccessViewHolder extends RecyclerView.ViewHolder {
            TextView tvAccessLog;

            public AccessViewHolder(@NonNull View itemView) {
                super(itemView);
                tvAccessLog = itemView.findViewById(R.id.tvAccessLog);
            }
        }

        public AccessLogAdapter(Context context, List<Access> accessList) {
            this.context = context;
            this.accessList = accessList;
        }

        @NonNull
        @Override
        public AccessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_access_log, parent, false);
            return new AccessViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AccessViewHolder holder, int position) {
            Access access = accessList.get(position);
            String logText = access.getTimeStamp() + ", " + access.getAccessType();
            holder.tvAccessLog.setText(logText);
        }

        @Override
        public int getItemCount() {
            return accessList.size();
        }
    }
}









