package com.tu.place.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.tu.place.R;
import com.tu.place.firebase.FirebaseManager;
import com.tu.place.model.Place;
import com.tu.place.utils.AppContants;
import com.tu.place.utils.AppDialogManager;
import com.tu.place.utils.AppUtils;

import java.util.HashMap;

import im.delight.android.webview.AdvancedWebView;

public class PlaceInfoActivity extends AppCompatActivity implements AdvancedWebView.Listener {

    private Place place;
    private AdvancedWebView webView;
    private FloatingActionButton fbEdit;
    private Dialog mDialogLoading;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_info);
        place = new Place();
        place.setId(getIntent().getStringExtra("placeId"));
        mDialogLoading = AppDialogManager.createLoadingDialog(this);
        webView = (AdvancedWebView) findViewById(R.id.webView);
        fbEdit = (FloatingActionButton) findViewById(R.id.fb_edit);
        fbEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editInfo();
            }
        });
        fbEdit.setVisibility(View.GONE);
        JavaScriptInterface jsInterface = new JavaScriptInterface(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(jsInterface, "JSInterface");
        getPlaceDetail();

    }


    public void editInfo(){
        webView.loadUrl(AppContants.ASSETS_PATH+"summnernote.html");
//        webView.loadUrl("javascript:setNote("+place.getTitleInfo()+","+place.getInfo()+")");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    webView.evaluateJavascript("setNote('"+place.getTitleInfo()+"','"+place.getInfo()+"');", null);
                } else {
                    webView.loadUrl("javascript:setNote('"+place.getTitleInfo()+"','"+place.getInfo()+"');");
                }
            }
        }, 500);
        fbEdit.setVisibility(View.GONE);
    }

    public void loadInfo(){
        StringBuilder summary = new StringBuilder();
        summary.append("<!DOCTYPE html>");
        summary.append("<html><head><meta charset=\"UTF-8\"><title>Summernote</title>");
        summary.append("<link href='http://netdna.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.css' rel='stylesheet'>");
        summary.append("<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.js'></script>");
        summary.append("<script src='http://netdna.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.js'></script>");
        summary.append("<link href='http://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.8/summernote.css' rel='stylesheet'>");
        summary.append("<script src='http://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.8/summernote.js'></script>");
        summary.append("</head><body style='margin:2%'>");
        summary.append("<center><h4>"+place.getTitleInfo()+"</h4></center>");
        summary.append(place.getInfo());
        summary.append("</body></html>");

        webView.loadData(summary.toString(), "text/html", null);
        if(place.getUserId().equals(MainActivity.user.username))
            fbEdit.setVisibility(View.VISIBLE);
        else fbEdit.setVisibility(View.GONE);
//        mDialogLoading.dismiss();
    }

    private void getPlaceDetail(){
        FirebaseManager firebaseManager = new FirebaseManager();
        firebaseManager.getRef(AppContants.FIREBASE_PLACE_TABLE + "/" + place.getId(), new FirebaseManager.Callback() {
            @Override
            public void success(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    if(dataSnapshot.hasChild("userId"))
                        place.setUserId(dataSnapshot.child("userId").getValue().toString());
                    if(dataSnapshot.hasChild("info")){
                        place.setTitleInfo(dataSnapshot.child("titleInfo").getValue().toString());
                        place.setInfo(dataSnapshot.child("info").getValue().toString());
                    }
                    if(AppUtils.isEmptyString(place.getInfo())) webView.loadUrl(AppContants.ASSETS_PATH+"summnernote.html");
                    else loadInfo();
                }
            }

            @Override
            public void failed() {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        webView.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }

    public class JavaScriptInterface {
        private Activity activity;


        public JavaScriptInterface(Activity activiy) {
            this.activity = activiy;

        }

        @JavascriptInterface
        public void getNote(final String title, final String note){
            if(AppUtils.isEmptyString(title)){
                Snackbar.make(webView, "Tiêu đề không được rỗng!", Snackbar.LENGTH_LONG).show();
                return;
            }else if(AppUtils.isEmptyString(title)){
                Snackbar.make(webView, "Bài viết không được rỗng!", Snackbar.LENGTH_LONG).show();
                return;
            }
//            mDialogLoading.show();
            FirebaseManager firebaseManager = new FirebaseManager();
            HashMap<String, String> hashMap = new HashMap();
            hashMap.put("userId", MainActivity.user.username);
            hashMap.put("titleInfo", title);
            hashMap.put("info", new String(note));
            firebaseManager.writeRef(AppContants.FIREBASE_PLACE_TABLE, place.getId(), hashMap, new FirebaseManager.Callback() {
                @Override
                public void success(DataSnapshot dataSnapshot) {
                    Toast.makeText(activity, "Lưu thành công", Toast.LENGTH_LONG).show();
                    place.setTitleInfo(title);
                    place.setInfo(note);
                    loadInfo();

                }

                @Override
                public void failed() {
//                    mDialogLoading.dismiss();
                    Snackbar.make(webView, "Lưu thất bại", Snackbar.LENGTH_LONG).show();
                }
            });

        }

        public void setNote(String note){

        }

        public void startVideo(String videoAddress){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(videoAddress), "video/3gpp"); // The Mime type can actually be determined from the file
            activity.startActivity(intent);
        }
    }

}
