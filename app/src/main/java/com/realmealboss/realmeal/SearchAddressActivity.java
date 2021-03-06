package com.realmealboss.realmeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.realmealboss.realmeal.Home.MartAddActivity;

public class SearchAddressActivity extends AppCompatActivity {

    private WebView webView;
    private TextView address;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_address);

        address = findViewById(R.id.text_address);

        // WebView 초기화
        init_webView();

        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MartAddActivity.class);
        startActivity(intent);
        finish();
    }
    public void init_webView() {
        // WebView 설정
        webView = (WebView) findViewById(R.id.webview_address);

        // JavaScript 허용
        webView.getSettings().setJavaScriptEnabled(true);

        // JavaScript의 window.open 허용
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        webView.addJavascriptInterface(new AndroidBridge(), "TestApp");

        // web client 를 chrome 으로 설정
        webView.setWebChromeClient(new WebChromeClient());

        // webview url load. php 파일 주소
        webView.loadUrl("http://101.101.211.145/kakao_address2.php");

    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3
                , final String arg4, final String arg5
        ) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //address.setText(String.format("(%s) %s %s", arg1, arg2, arg3));
                    address.setText(String.format("%s %s", arg2, arg3));
                    System.out.println(address.getText().toString());
                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    init_webView();
                    Intent intent = new Intent(getApplicationContext(), MartAddActivity.class);
                    intent.putExtra("address", address.getText().toString());
                    intent.putExtra("lat", arg4);
                    intent.putExtra("lng", arg5);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}
