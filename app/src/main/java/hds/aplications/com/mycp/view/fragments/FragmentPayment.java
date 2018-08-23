package hds.aplications.com.mycp.view.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import hds.aplications.com.mycp.MyCP;
import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.models.ReservationDetail;
import hds.aplications.com.mycp.view.components.LoadMask;
import mgleon.common.com.LogUtils;
import mgleon.common.com.MessageToast;

/**
 * Created by Miguel Gomez Leon.
 * mgleonsc@gmail.com
 */

public class FragmentPayment extends Fragment{
    private static final String TAG = LogUtils.makeLogTag(FragmentPayment.class);

    //private ProgressDialog progressBar;
    private LoadMask loadingMask;
    private ListenerFragmentPayment listenerFragmentPayment;
    private WebView webView;
    String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webView = (WebView) view.findViewById(R.id.web_view_payment);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url){
                super.onPageFinished(view, url);
                hideLoadMask();
                if(listenerFragmentPayment != null){
                    listenerFragmentPayment.endPostUrl();
                }
            }
        });

        view.findViewById(R.id.web_view_payment_close).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(listenerFragmentPayment != null){
                    listenerFragmentPayment.close();
                }
            }
        });

        view.findViewById(R.id.web_view_payment_reload).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                loadUrlPayment();
            }
        });

        loadUrlPayment();
    }

    private void loadUrlPayment(){
        showLoadMask();

        /*String post = "nextvar1=mgl@ddd&nextvar2=value2";
        webView.postUrl(MyCP.SERVER + "/test/index.php", post.getBytes());*/
        String[] a = url.split("\\?", 2);

        //String hostTest = "http://192.168.0.108/test/index.php";

       /*
        String testUrl = "https://www.moneybookers.com/app/payment.pl";
        String params = "pay_to_email=accounting%40mycasaparticular.com&recipient_description=MyCasaParticular.com&transaction_id=25417&return_url=http%3A%2F%2Fmycp.dev%2Fen%2Fpayment%2Fskrill-return%2F25417&return_url_text=Back+to+MyCasaParticular&return_url_target=1&cancel_url=http%3A%2F%2Fmycp.dev%2Fen%2Fpayment%2Fskrill-cancel&cancel_url_target=1&status_url=http%3A%2F%2Fmycp.dev%2Fen%2Fskrill%2Fstatus&status_url2=booking%40mycasaparticular.com&dynamic_descriptor=Descriptor&language=EN&confirmation_note=MyCasaParticular+whishes+you+a+very+nice+stay+in+Cuba&pay_from_email=mgleonsc%40gmail.com&logo_url=http%3A%2F%2Fmycp.dev%2Fbundles%2Ffrontend%2Fimg%2Fmycp.png&firstname=mgleonsc&lastname=G%C3%B3mez+Le%C3%B3n&address=Marianao&postal_code=1900&city=Habana&country=CUB&amount=20.492&currency=EUR&detail1_description=Booking+ID%3A++&detail1_text=25417&detail2_description=Reservation+IDs%3A+&detail2_text=CAS.140007&payment_methods=ACC%2CDID%2CSFT&hide_login=1&skrill_submit_button=Pay+with+Skrill";
        */
       webView.postUrl(a[0], a[1].getBytes());
    }

    private void showLoadMask(){
        if(loadingMask != null){
            loadingMask.show(getString(R.string.load_skrill));
        }
    }

    private void hideLoadMask(){
        if(loadingMask != null){
            loadingMask.hide();
        }
    }

    public void setListenerFragmentPayment(ListenerFragmentPayment listenerFragmentPayment){
        this.listenerFragmentPayment = listenerFragmentPayment;
    }

    public void setLoadingMask(LoadMask loadingMask){
        this.loadingMask = loadingMask;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public interface ListenerFragmentPayment{
        void endPostUrl();
        void close();
    }
}
