package hds.aplications.com.mycp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import hds.aplications.com.mycp.R;

public class ActivityInitial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_files_map);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.ai_toolbar_welcome);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public void actionInitLogin(View view){
        ActivityInitial.this.startActivity(new Intent(ActivityInitial.this, LoginActivity.class));
    }

    public void actionNewUser(View view){
        //ActivityNoFilesMap.this.startActivity(new Intent(ActivityNoFilesMap.this, ActivityRegister.class));
    }

}
