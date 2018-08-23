package hds.aplications.com.mycp.view;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hds.aplications.com.mycp.R;

public class LoadMask {

    private RelativeLayout frame;
    private TextView textView;
    private String defaultText;
    private Activity activity;

    public LoadMask(Activity activity) {
        this.activity = activity;
    }

    private void inizializateLoadMask(){
        if(this.frame == null){
            this.frame = (RelativeLayout)activity.findViewById(R.id.loading_mask);
        }

        if(this.textView == null){
            this.textView = (TextView)activity.findViewById(R.id.loading_mask_text);
            this.defaultText = this.activity.getResources().getString(R.string.loading_mask_text);
        }
    }

    public void show(String text){
        this.inizializateLoadMask();
        this.textView.setText(text);
        this.frame.setVisibility(View.VISIBLE);
    }

    public void show(){
        this.inizializateLoadMask();
        this.show(this.defaultText);
    }

    public void hide(){
        this.inizializateLoadMask();
        this.frame.setVisibility(View.INVISIBLE);
    }

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }
}
