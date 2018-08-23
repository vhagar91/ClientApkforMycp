package hds.aplications.com.mycp.helpers.alerts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class Confirmation extends DialogFragment {

    public DialogInterface.OnClickListener clickOk = null;
    public DialogInterface.OnClickListener clickNotOk = null;

    private String message = "¿Confirma la acción seleccionada?";
    private String title = "Confirmación";
    private String titleOKBnt = "Aceptar";
    private String titleNotOkBnt = "Cancelar";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        builder.setMessage(this.message)
        	   .setTitle(this.title)
               .setPositiveButton(this.titleOKBnt, clickOk)
               .setNegativeButton(this.titleNotOkBnt, clickNotOk);

        return builder.create();
    }

    public String getMessage() {
        return message;
    }

    public Confirmation setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Confirmation setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTitleOKBnt() {
        return titleOKBnt;
    }

    public Confirmation setTitleOKBnt(String titleOKBnt) {
        this.titleOKBnt = titleOKBnt;
        return this;
    }

    public String getTitleNotOkBnt() {
        return titleNotOkBnt;
    }

    public Confirmation setTitleNotOkBnt(String titleNotOkBnt) {
        this.titleNotOkBnt = titleNotOkBnt;
        return this;
    }
}
