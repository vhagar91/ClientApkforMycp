package hds.aplications.com.mycp.map.marker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
import org.osmdroid.views.MapView;

import hds.aplications.com.mycp.R;


public class CustomMarkerMenuWindow extends MarkerInfoWindow {

    private CustomMarker customMarker;
    private OnMenuSelectOptionListener mOnMenuSelectOptionListener;

    public CustomMarkerMenuWindow(int layoutResId, MapView mapView, OnMenuSelectOptionListener onMenuSelectOptionListener) {
        super(layoutResId, mapView);
        this.mView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent e) {
                return true;
            }
        });
        this.mOnMenuSelectOptionListener = onMenuSelectOptionListener;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onOpen(Object item) {
        this.customMarker = (CustomMarker)item;

        this.mView.findViewById(R.id.mmw_origin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnMenuSelectOptionListener.onSelectOrigin(customMarker);
                close();
            }
        });
        this.mView.findViewById(R.id.mmw_destination).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnMenuSelectOptionListener.onSelectDestination(customMarker);
                close();
            }
        });

        ShapeDrawable mCardBackground = new ShapeDrawable();

        mCardBackground.getPaint().setColor(ContextCompat.getColor(this.mView.getContext(), R.color.color_btn_black));
        mCardBackground.setShape(new TriangleShape());
        TextView arrow = ((TextView)this.mView.findViewById(R.id.arrow));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            arrow.setBackground(mCardBackground);
        }
        else {
            arrow.setBackgroundDrawable(mCardBackground);
        }
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

    public interface OnMenuSelectOptionListener {
        boolean onSelectOrigin(CustomMarker customMarker);
        boolean onSelectDestination(CustomMarker customMarker);
    }
}
