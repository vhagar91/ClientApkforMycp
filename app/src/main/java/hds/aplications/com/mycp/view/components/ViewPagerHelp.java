package hds.aplications.com.mycp.view.components;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Miguel Gomez Leon on 3/25/16.
 * mgleonsc@gmail.com
 */
public class ViewPagerHelp extends ViewPager {

    private final Map<Integer, Fragment> mFragments = new HashMap<>();

    public ViewPagerHelp(Context context) {
        super(context);
    }

    public ViewPagerHelp(Context context, AttributeSet attrs){

        super(context, attrs);
    }

    public void addFragment(Fragment fragment, int pos){
        mFragments.put(pos, fragment);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        int childCount = getChildCount();
        int currentItem = getCurrentItem();
        for(int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            Fragment fragment = mFragments.get(currentItem);

            if (fragment != null && fragment.getView() == child){
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if(h > height) height = h;
            }
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}