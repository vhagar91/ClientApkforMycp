package hds.aplications.com.mycp.map.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.squareup.otto.Subscribe;

import hds.aplications.com.mycp.MyCP;
import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.map.downloader.MapDownloaderLoader;
import hds.aplications.com.mycp.map.util.event.MapDownloaderProgressEvent;
import hds.aplications.com.mycp.map.util.event.MapSuccessfulDownloadedEvent;


/**
 * Created by Miguel Gomez Leon on 11/01/2016.
 * mgleonsc@gmail.com
 * * A placeholder fragment containing a simple view.
 */
public class MapDownloaderFragment extends Fragment {

    private ViewSwitcher mViewSwitcher;
    private TextView mDownloadingProgressTextView;
    private ProgressBar mDownloadingProgressBar;

    private final LoaderManager.LoaderCallbacks<Boolean> mLoadManager = new LoaderManager.LoaderCallbacks<Boolean>() {

        @Override
        public Loader<Boolean> onCreateLoader(int id, Bundle args) {
            return new MapDownloaderLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
            getLoaderManager().destroyLoader(R.id.loader_map_downloader);
            MyCP.getApplication(getActivity()).sendOttoEvent(new MapSuccessfulDownloadedEvent(1));
        }

        @Override
        public void onLoaderReset(Loader<Boolean> loader) {}
    };

    public MapDownloaderFragment() {}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_downloader, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLoaderManager().destroyLoader(R.id.loader_map_downloader);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewSwitcher = (ViewSwitcher) view.findViewById(R.id.view_flipper);
        mDownloadingProgressTextView = (TextView) view.findViewById(R.id.text_view_downloading_progress);
        mDownloadingProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar_downloading);
        view.findViewById(R.id.button_start_downloading).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownloading();
            }
        });
        view.findViewById(R.id.button_cancel_downloading).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDownloading();
            }
        });
        mViewSwitcher.setDisplayedChild(getLoaderManager().getLoader(R.id.loader_map_downloader) == null ? 0 : 1);
    }

    @Override
    public void onPause() {
        super.onPause();
        MyCP.getApplication(getActivity()).unregisterOttoBus(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MyCP.getApplication(getActivity()).registerOttoBus(this);
    }

    private void stopDownloading() {
        getLoaderManager().destroyLoader(R.id.loader_map_downloader);
        mViewSwitcher.setDisplayedChild(0);
    }

    private void startDownloading() {
        answerAvailable(new MapDownloaderProgressEvent(0));
        getLoaderManager().initLoader(R.id.loader_map_downloader, null, mLoadManager);
        mViewSwitcher.setDisplayedChild(1);
    }

    @Subscribe
    public void answerAvailable(final MapDownloaderProgressEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDownloadingProgressTextView.setText(getString(R.string.text_view_downloading_progress, event.progress));
                mDownloadingProgressBar.setProgress(event.progress);
            }
        });

    }
}
