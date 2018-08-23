package hds.aplications.com.mycp.helpers;

/**
 * Created by vhagar on 15/05/2018.
 */
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.annotation.TargetApi;
import android.content.Context;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.os.Build;

import java.util.concurrent.ExecutionException;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.content.Intent;
import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.view.components.LoadMask;
import mgleon.common.com.MessageToast;
import mgleon.common.com.receivers.ConnectivityReceiver;

import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ProgressBar;


public class PDFTools {
    private static final String TAG = "PDFTools";
    private static final String GOOGLE_DRIVE_PDF_READER_PREFIX = "http://drive.google.com/viewer?url=";
    private static final String PDF_MIME_TYPE = "application/pdf";
    private static final String HTML_MIME_TYPE = "text/html";


    public static void showPDFUrl(final Context context, final String pdfUrl,final String name) {

        if (isPDFSupported(context)) {
            downloadAndOpenPDF(context, pdfUrl,name);
        } else {

            askToOpenPDFThroughGoogleDrive(context, pdfUrl);
        }
    }


    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void downloadAndOpenPDF(final Context context, final String pdfUrl,final String filename) {
        // Get filename
        // The place where the downloaded PDF file will be put
        //final String filename = pdfUrl.substring( pdfUrl.lastIndexOf( "/" ) + 1 );
        final File tempFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), filename);
        Log.e(TAG, "File Path:" + tempFile);
        if (tempFile.exists()) {
            // If we have downloaded the file before, just go ahead and show it.

            openPDF(context, Uri.fromFile(tempFile));

            return;
        }

//        String filename = "";
//        try {
//            filename = new GetFileInfo().execute(pdfUrl).get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//

        // Create the download request
        if(ConnectivityReceiver.isOnline(context)) {
            DownloadManager.Request r = new DownloadManager.Request(Uri.parse(pdfUrl));
            r.setVisibleInDownloadsUi(true);
            r.setTitle(context.getString(R.string.text_view_downloading_progress));
            r.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, filename);
            final DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            BroadcastReceiver onComplete = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    context.unregisterReceiver(this);


                    long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    Cursor c = dm.query(new DownloadManager.Query().setFilterById(downloadId));

                    if (c.moveToFirst()) {
                        int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {

                            openPDF(context, Uri.fromFile(tempFile));
                        }
                    }
                    c.close();
                }
            };
            context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

            // Enqueue the request
            dm.enqueue(r);
        }
        else{
            MessageToast.showError(context, context.getString(R.string.no_internet_connection));
        }

    }


    public static void askToOpenPDFThroughGoogleDrive(final Context context, final String pdfUrl) {
        if(ConnectivityReceiver.isOnline(context)) {
            new AlertDialog.Builder(context)
                    .setTitle("Open Voucher from Google Drive")
                    .setMessage("Do you want to open the file")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openPDFThroughGoogleDrive(context, pdfUrl);
                        }
                    })
                    .show();
        }
        else{
            MessageToast.showError(context, context.getString(R.string.no_internet_connection));
        }
    }

    public static void openPDFThroughGoogleDrive(final Context context, final String pdfUrl) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse(GOOGLE_DRIVE_PDF_READER_PREFIX + pdfUrl), HTML_MIME_TYPE);
        context.startActivity(i);


    }

    public static final void openPDF(Context context, Uri localUri) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(localUri, PDF_MIME_TYPE);
        context.startActivity(i);
    }

    public static boolean isPDFSupported(Context context) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        final File tempFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "test.pdf");
        i.setDataAndType(Uri.fromFile(tempFile), PDF_MIME_TYPE);
        return context.getPackageManager().queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    // get File name from url
    static class GetFileInfo extends AsyncTask<String, Integer, String> {

        protected String doInBackground(String... urls) {
            URL url;
            String filename = null;
            try {
                url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                conn.setInstanceFollowRedirects(false);
                if (conn.getHeaderField("Content-Disposition") != null) {
                    String depo = conn.getHeaderField("Content-Disposition");

                    String depoSplit[] = depo.split("filename=");
                    filename = depoSplit[1].replace("filename=", "").replace("\"", "").trim();
                } else {
                    filename = "download.pdf";
                }
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
            }
            return filename;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // use result as file name
        }
    }
    static public void copyFile(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, read);
        }
    }


}
