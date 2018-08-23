package hds.aplications.com.mycp.view.fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.Arrays;
import java.util.List;

import hds.aplications.com.mycp.MyCP;
import hds.aplications.com.mycp.R;


public class FragmentSlide extends Fragment {
    private String url;
    private boolean toGalery;
    private ImageView imageView;

    public static FragmentSlide create(String url, boolean toGalery) {
        FragmentSlide fragment = new FragmentSlide();
        fragment.setUrl(MyCP.SERVER + url);
        fragment.setToGalery(toGalery);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView;

        if(toGalery){
            rootView = (ViewGroup) inflater.inflate(R.layout.fragment_slide_galery, container, false);
        }
        else {
            rootView = (ViewGroup) inflater.inflate(R.layout.fragment_slide, container, false);
        }

        imageView = ((ImageView)rootView.findViewById(R.id.slide_image_remote));
        //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        loadImage();
        return rootView;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void setToGalery(boolean toGalery){
        this.toGalery = toGalery;
    }

    private void loadImage(){
        if (url != null && !url.isEmpty()){
            RequestCreator requestCreator = MyCP.getPicassoBuilder(getContext()).build().load(url).placeholder(R.drawable.animated_load_image);
            requestCreator.fit();

            if(toGalery){
                requestCreator.centerInside();
            }
            else {
                requestCreator.centerCrop();
            }

            requestCreator.into(imageView);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(!toGalery){
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                loadImage();
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
                loadImage();
            }
        }
    }
}
