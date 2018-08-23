package hds.aplications.com.mycp.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import hds.aplications.com.mycp.R;

public class MessageToast {

     public static void showError(Context context, String text){
        MessageToast.show(context, text, R.layout.error_toast);
    }

    public static void showInfo(Context context, String text){
        MessageToast.show(context, text, R.layout.info_toast);
    }

    public static void showSuccess(Context context, String text){
        MessageToast.show(context, text, R.layout.success_toast);
    }

    public static void showWarning(Context context, String text){
        MessageToast.show(context, text, R.layout.warning_toast);
    }

    private static void show(Context context, String text, int resource){
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource, null);
        toast.setView(view);

        TextView textView = (TextView) view.findViewById(R.id.messageToastText);
        textView.setText(text);
        toast.show();
    }
}
