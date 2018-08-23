package hds.aplications.com.mycp.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.graphhopper.util.InstructionList;

import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.view.adapters.InstructionAdapter;

public class ActivityInstructions extends AppCompatActivity {
     private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        listView = (ListView) findViewById(R.id.list);

        InstructionList instructions = SAppData.getInstance().getInstructions();

        if(instructions.size() > 0) {
            listView.setAdapter(new InstructionAdapter(this, instructions, listView));

            findViewById(R.id.emptyList).setVisibility(View.INVISIBLE);

            Bundle bundle = this.getIntent().getExtras();
            String fmDistanceRoute = bundle.getString("fmDistanceRoute");
            ((TextView)this.findViewById(R.id.fm_distance_route)).setText(fmDistanceRoute);
        }
        else{
            findViewById(R.id.emptyList).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.fm_clear_distance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityMap)SAppData.getInstance().getAppCompatActivity()).clearDistance();

                onBackPressed();
            }
        });

        findViewById(R.id.btn_scroll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setSelectionFromTop(listView.getFirstVisiblePosition(), listView.getChildAt(0).getTop() - 40);
            }
        });
    }

    @Override
    public void onBackPressed() {
        clearDataOfInstrauction();
    }


    public void backAction(View view) {
        clearDataOfInstrauction();
    }

    private void clearDataOfInstrauction(){
        this.finish();
    }
}
