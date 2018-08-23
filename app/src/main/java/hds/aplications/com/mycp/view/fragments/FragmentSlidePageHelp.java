package hds.aplications.com.mycp.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import hds.aplications.com.mycp.R;


public class FragmentSlidePageHelp extends Fragment {
    public static final String ARG_PAGE = "page";

    private static final List<Integer> stepsHelpPage = Arrays.asList(
            R.layout.fragment_slide_page_0,//
            R.layout.fragment_slide_page_0,//
            R.layout.fragment_slide_page_2,
            R.layout.fragment_slide_page_3,
            R.layout.fragment_slide_page_0,//
            R.layout.fragment_slide_page_0,//
            R.layout.fragment_slide_page_6);//

    private int mPageNumber;
    private DataInfo dataInfo;

    static class DataInfo {
        int icon;
        int text;

        public DataInfo(int icon, int text) {
            this.icon = icon;
            this.text = text;
        }
    }

    public static FragmentSlidePageHelp create(int pageNumber) {
        FragmentSlidePageHelp fragment = new FragmentSlidePageHelp();

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);

        DataInfo dataInfo = null;
        switch (pageNumber) {
            case 1:
                dataInfo = new DataInfo(R.mipmap.home, R.string.text_1_page_2);
                break;
            case 4:
                dataInfo = new DataInfo(R.mipmap.indications, R.string.text_1_page_5);
                break;
            case 5:
                dataInfo = new DataInfo(R.mipmap.reset_route, R.string.text_1_page_6);
                break;
            default:
                break;
        }

        fragment.setDataInfo(dataInfo);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(stepsHelpPage.get(mPageNumber), container, false);

        if (dataInfo != null){
            ((TextView)rootView.findViewById(R.id.text_1_page_6)).setText(dataInfo.text);
            ((ImageButton)rootView.findViewById(R.id.btn_get_location_help)).setImageResource(dataInfo.icon);
            rootView.findViewById(R.id.btn_get_location_help).setBackgroundResource(R.drawable.selector_btn_transparent);
        }

        return rootView;
    }

    public DataInfo getDataInfo() {
        return dataInfo;
    }

    public void setDataInfo(DataInfo dataInfo) {
        this.dataInfo = dataInfo;
    }
}
