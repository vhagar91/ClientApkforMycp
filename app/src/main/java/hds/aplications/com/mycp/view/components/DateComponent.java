package hds.aplications.com.mycp.view.components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import hds.aplications.com.mycp.R;
import mgleon.common.com.DateUtils;
import mgleon.common.com.MessageToast;

/**
 * Created by Miguel Gomez Leon.
 * mgleonsc@gmail.com
 */

public class DateComponent {
    private EditText editText;
    private AppCompatActivity activity;
    private Context context;
    private AlertDialog.Builder alert;
    private AlertDialog dlg;
    private MaterialCalendarView myCalendar;
    private Date date;
    private Listener listener;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public DateComponent(final EditText editText, final AppCompatActivity activity, final Listener xlistener){
        this.editText = editText;
        this.activity = activity;
        this.context = this.activity.getApplicationContext();
        this.listener = xlistener;

        /* ********** wdfsdfw *********** */
        this.alert = new AlertDialog.Builder(activity);
        final View view = View.inflate(context, R.layout.dialog_calendar, null);
        alert.setView(view);
        //alert.setCancelable(false);

        view.findViewById(R.id.cancel_date).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dlg.dismiss();
            }
        });
        view.findViewById(R.id.accept_date).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(myCalendar.getSelectedDate() == null){
                    MessageToast.showInfo(context, context.getString(R.string.select_date_info));
                    return;
                }

                Date xdate = myCalendar.getSelectedDate().getDate();
                date = xdate;

                String d = DateUtils.generateStringDate(date, simpleDateFormat);
                editText.setText(d);
                dlg.dismiss();

                if(listener != null){
                    listener.dateAccept(date);
                    listener.setDate();
                }
            }
        });

        OnDateSelectedListener onDateSelectedListener = new OnDateSelectedListener(){
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay pdate, boolean selected){
                /*date = pdate.getDate();
                if(listener != null){
                    listener.dateAccept(date);
                }*/
                //MessageToast.showInfo(context, date1.toString());
            }
        };

        Calendar actualYear = Calendar.getInstance();
        myCalendar = ((MaterialCalendarView)view.findViewById(R.id.calendar_view));
        myCalendar.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(actualYear)
                .commit();

        myCalendar.setOnDateChangedListener(onDateSelectedListener);

        this.editText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dlgShow();
            }
        });
        this.editText.setInputType(InputType.TYPE_NULL);
        this.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dlgShow();
                }
            }
        });
    }

    private void dlgShow(){
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if(dlg == null){
            dlg = alert.show();
        }
        else {
            dlg.show();
        }
    }

    public Date getDate(){
        return date;
    }

    public void setDate(Date date){
        this.date = date;

        String d = DateUtils.generateStringDate(this.date, simpleDateFormat);
        editText.setText(d);

        Calendar calendar = DateUtils.getCalendar(date);
        myCalendar.setSelectedDate(calendar);
        myCalendar.setCurrentDate(CalendarDay.from(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)), true);
        if(listener != null){
            listener.setDate();
        }
    }

    public Listener getListener(){
        return listener;
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public interface Listener{
        void dateAccept(Date date);
        void setDate();
    }

    /*private void goToMonthOfDate(Calendar actualDate,Calendar newDate) {
        if(newDate.after(actualDate)) {
            int nb=(newDate.get(Calendar.YEAR) - actualDate.get(Calendar.YEAR)) * 12 + newDate.get(Calendar.MONTH) - dateActive.get(Calendar.MONTH);
            for(int i=0;i<nb;i++) myCalendar.goToNext();
        }
        else {
            int nb=(actualDate.get(Calendar.YEAR) -
                    newDate.get(Calendar.YEAR)) * 12 +
                    actualDate.get(Calendar.MONTH) -
                    newDate.get(Calendar.MONTH);
            for(int i=0;i<nb;i++) myCalendar.goToPrevious();
        }
    }*/
}
