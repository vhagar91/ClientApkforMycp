package hds.aplications.com.mycp.view.others;

import android.view.ViewTreeObserver;
import android.widget.ScrollView;



public abstract class OnScrollViewChangedListener implements ViewTreeObserver.OnScrollChangedListener{
    private ScrollView scrollView;
    private DirectionScroll directionScroll = DirectionScroll.up;
    private int scrollY = 0;

    public OnScrollViewChangedListener(ScrollView scrollView){
        this.scrollView = scrollView;
    }

    @Override
    public void onScrollChanged(){
        int newScrollY = scrollView.getScrollY();

        if (scrollY < newScrollY && directionScroll != DirectionScroll.down) {
            directionScroll = DirectionScroll.down;
            onHide();
        }
        if (scrollY > newScrollY && directionScroll != DirectionScroll.up) {
            directionScroll = DirectionScroll.up;
            onShow();
        }

        scrollY = newScrollY;

        //scrollY = scrollView.getScrollY(); // For ScrollView
        //int scrollX = scrollView.getScrollX(); // For HorizontalScrollView
    }

    public abstract void onHide();

    public abstract void onShow();
}