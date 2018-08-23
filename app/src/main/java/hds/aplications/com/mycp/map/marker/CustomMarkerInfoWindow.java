package hds.aplications.com.mycp.map.marker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
import org.osmdroid.views.MapView;

import java.util.Iterator;

import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.models.Reservation;
import mgleon.common.com.DateUtils;


public class CustomMarkerInfoWindow extends MarkerInfoWindow {

    private CustomMarker customMarker;
    private int reservationIndex = -1;

    public CustomMarkerInfoWindow(int layoutResId, MapView mapView) {
        super(layoutResId, mapView);
        this.mView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent e) {
                return true;
            }
        });
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onOpen(Object item) {
        this.customMarker = (CustomMarker)item;

        this.mView.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        String title = this.customMarker.model.getTitle();

        ((TextView)this.mView.findViewById(R.id.bubble_title)).setText(title);

        ShapeDrawable mCardBackground = new ShapeDrawable();
        mCardBackground.getPaint().setColor(ContextCompat.getColor(this.mView.getContext(), R.color.color_btn_ico_green));
        mCardBackground.setShape(new TriangleShape());
        TextView arrow = ((TextView)this.mView.findViewById(R.id.arrow));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            arrow.setBackground(mCardBackground);
        }
        else {
            arrow.setBackgroundDrawable(mCardBackground);
        }

        View btnNext = this.mView.findViewById(R.id.btn_next_date);
        View btnPrev = this.mView.findViewById(R.id.btn_prev_date);
        if(this.customMarker.model.getReservations().size() == 1){
            btnNext.setVisibility(View.GONE);
            btnPrev.setVisibility(View.GONE);
        }
        else {
            this.createAllPoint();
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomMarkerInfoWindow.this.navegate(1);
                }
            });
            btnPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomMarkerInfoWindow.this.navegate(-1);
                }
            });
        }

        this.reservationIndex = -1;
        this.navegate(1);
    }

    private void createAllPoint(){
        Iterator<Reservation> it = this.customMarker.model.getReservations().iterator();
        LinearLayout allPoint = (LinearLayout)this.mView.findViewById(R.id.layout_all_point);
        allPoint.removeAllViews();
        while (it.hasNext()) {
            it.next();
            View view = createPoint(true);
            allPoint.addView(view);
        }
    }

    private View createPoint(boolean green){
        LayoutInflater inflater = (LayoutInflater) this.mView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(green){
            return inflater.inflate(R.layout.point_green, null);
        }
        return inflater.inflate(R.layout.point_white, null);
    }

    private static class TriangleShape extends Shape {
        private final Path mPath = new Path();

        @Override
        protected void onResize(float width, float height) {
            mPath.reset();
            mPath.moveTo(0, 0);
            mPath.lineTo(width, 0);
            mPath.lineTo(width / 2, height);
            mPath.lineTo(0, 0);
            mPath.close();
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {
            canvas.drawPath(mPath, paint);
        }

    }

    public void navegate(int step) {

        if(this.reservationIndex >= 0 && this.reservationIndex < this.customMarker.model.getReservations().size()){
            LinearLayout allPoint = (LinearLayout)this.mView.findViewById(R.id.layout_all_point);
            allPoint.removeViewAt(this.reservationIndex);
            allPoint.addView(createPoint(true), this.reservationIndex);
        }

        LinearLayout linearLayoutCheckout = (LinearLayout)this.mView.findViewById(R.id.layout_day_checkout);
        LinearLayout linearLayoutCheckin = (LinearLayout)this.mView.findViewById(R.id.layout_day_checkin);

        this.reservationIndex += step;
        if (this.reservationIndex >= this.customMarker.model.getReservations().size()){
            this.reservationIndex = 0;
        }
        else if (this.reservationIndex < 0){
            this.reservationIndex = this.customMarker.model.getReservations().size() - 1;
        }

        Reservation reservation ;
        try{
            reservation= this.customMarker.model.getReservations().get(this.reservationIndex);
        }
        catch (Exception e){
            reservation=null;
        }
        if (reservation != null){
            linearLayoutCheckout.setVisibility(View.INVISIBLE);
            linearLayoutCheckin.setVisibility(View.INVISIBLE);

            String dateCheckin = DateUtils.getLargeDate(reservation.getDateFrom(), false);
            ((TextView)this.mView.findViewById(R.id.day_checkin)).setText(dateCheckin);

            String dateCheckout = DateUtils.getLargeDate(reservation.getDateTo(), false);
            ((TextView)this.mView.findViewById(R.id.day_checkout)).setText(dateCheckout);

            linearLayoutCheckout.setVisibility(View.VISIBLE);
            linearLayoutCheckin.setVisibility(View.VISIBLE);

            LinearLayout allPoint = (LinearLayout)this.mView.findViewById(R.id.layout_all_point);
            if(allPoint.getChildAt(this.reservationIndex) != null){
                allPoint.removeViewAt(this.reservationIndex);
                allPoint.addView(createPoint(false), this.reservationIndex);
            }
        }
        else {
            linearLayoutCheckout.setVisibility(View.GONE);
            linearLayoutCheckin.setVisibility(View.GONE);
        }
    }
}
