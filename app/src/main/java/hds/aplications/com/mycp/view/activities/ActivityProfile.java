package hds.aplications.com.mycp.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.hbb20.CountryCodePicker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.view.View;
import hds.aplications.com.mycp.MyCP;
import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.PermissionsAdapter;
import hds.aplications.com.mycp.helpers.Request;
import hds.aplications.com.mycp.models.Country;
import hds.aplications.com.mycp.models.User;
import hds.aplications.com.mycp.services.UserClient;
import hds.aplications.com.mycp.view.components.LoadMask;
import hds.aplications.com.mycp.view.others.Utility;
import hds.aplications.com.mycp.view.others.ViewUtils;
import mgleon.common.com.MessageToast;
import mgleon.common.com.services.Util.ProcessRetrofitError;
import retrofit.Callback;
import retrofit.RetrofitError;
import hds.aplications.com.mycp.helpers.Request;
import retrofit.client.Response;
import retrofit.http.POST;

public class ActivityProfile extends ActivityBase {

    private static final String TAG = ActivityProfile.class.getSimpleName();
    private String userChoosenTask;
    private ImageView ivImage;


    @Override
    protected void initialize() {

        super.initialize();





        View viewItem = View.inflate(ActivityProfile.this, R.layout.activity_profile, null);
        this.addView(viewItem);
        updateInfo();
    }
    @Override
    protected void onResume(){
        super.onResume();
        sTracker.setScreenName("See Profile");
        sTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    private void updateInfo(){

         TextView user_name = (TextView) findViewById(R.id.profile_name);
         user_name.setText(user.getFullName());
         TextView city = (TextView) findViewById(R.id.et_city);
         city.setText(user.getCity());
         TextView email = (TextView) findViewById(R.id.et_email);
         email.setText(user.getEmail());

         TextView phone = (TextView) findViewById(R.id.et_phone);
         phone.setText(user.getPhone());

         FloatingActionButton fabMenu = findViewById(R.id.fab_menu);
         fabMenu.setVisibility(View.GONE);




    }

    public void onClickTakePicture(View v) {
        ivImage = (ImageView) findViewById(R.id.circle_image_view_profile_user_image);
        selectImage();
    }

    @Override
    protected void onMenuOptions(){
        //super.onMenuOptions();
        finish();
    }
    @Override
    protected void bindActions(){
        super.bindActions();
        submitProfile();
        ViewUtils.replaceBackgroundbyImage(context, (ViewGroup) findViewById(R.id.contss), R.id.image_to_remplace, R.drawable.image_habana);
    }
    private void submitProfile(){
        findViewById(R.id.ok_profile).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


                String city = ((EditText)findViewById(R.id.et_city)).getText().toString();

                String phone = ((EditText)findViewById(R.id.et_phone)).getText().toString();

                final String email = ((EditText)findViewById(R.id.et_email)).getText().toString();
                if(email.isEmpty()){
                    MessageToast.showError(context, getString(R.string.email_r));
                    return;
                }
                CountryCodePicker country = (CountryCodePicker)findViewById(R.id.country);
                String country_code=country.getSelectedCountryEnglishName();

                UserClient userClient = new UserClient(context);
                Callback<User> callback = new Callback<User>(){
                    @Override
                    public void success(User user, Response response){
                        loadingMask.hide();

                    }

                    @Override
                    public void failure(RetrofitError error){
                        loadingMask.hide();
                        ProcessRetrofitError.showError(error, context);
                    }
                };
                loadingMask.show(getString(R.string.c_profile));
                userClient.editProfile(email,country_code,phone,city, callback);
            }
        });
    }
    private void selectImage() {
        final CharSequence[] items = { getString(R.string.take_foto), getString(R.string.choose_pic),
                getString(R.string.cancel) };


        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityProfile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(ActivityProfile.this);
                if (items[item].equals(getString(R.string.take_foto))) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals(getString(R.string.choose_pic))) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent()
    {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ivImage.setImageBitmap(bm);
        new Upload(bm,user.getEmail()).execute();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1)
                onSelectFromGalleryResult(data);
            else if (requestCode == 0)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ivImage.setImageBitmap(thumbnail);
        new Upload(thumbnail,user.getEmail()).execute();
    }
    //async task to upload image
    private class Upload extends AsyncTask<Void,Void,String>{
        private Bitmap image;
        private String name;

        public Upload(Bitmap image,String name){
            this.image = image;
            this.name = name;
        }

        @Override
        protected String doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //compress the image to jpg format
            image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            /*
            * encode image to base64 so that it can be picked by saveImage.php file
            * */
            String encodeImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);

            //generate hashMap to store encodedImage and the name
            HashMap<String,String> detail = new HashMap<>();
            detail.put("name", name);
            detail.put("image", encodeImage);

            try{
                //convert this HashMap to encodedUrl to send to php file
                String dataToSend = hashMapToUrl(detail);
                //make a Http request and send data to saveImage.php file

                String response = Request.post(myCP.SERVER3+user.getEmail(),dataToSend);

                //return the response
                return response;

            }catch (Exception e){
                e.printStackTrace();
                Log.e(TAG,"ERROR  "+e);
                return null;
            }
        }

        private String hashMapToUrl(HashMap<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> entry : params.entrySet()){
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
        }


    }
}
    