package com.jewelzqiu.sjtubbs.page;

import com.jewelzqiu.sjtubbs.R;
import com.jewelzqiu.sjtubbs.support.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.util.LinkedHashMap;

public class PageActivity extends Activity {

    public static final String POST_URL = "post_url";

    public static final String PAGE_TITLE = "page_title";

    private static final String IMG_AUTO_ZOOM = "javascript:"
            + "var elements = document.getElementsByTag('img').onload = "
            + "function(){"
            + "if(this.width > screen.width) {"
            + "this.width = screen.width"
            + "}"
            + "}";

    static final LinkedHashMap<String, Integer> imgUrlMap = new LinkedHashMap<String, Integer>();

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new MyWebViewClient());
        new PrepareContentTask(mWebView).execute(getIntent().getStringExtra(POST_URL));

        setTitle(getIntent().getStringExtra(PAGE_TITLE));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            view.loadUrl(IMG_AUTO_ZOOM);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (imgUrlMap.containsKey(url)) {
                Intent intent = new Intent(PageActivity.this, PicActivity.class);
                intent.putExtra(PicActivity.PHOTO_POSITION, imgUrlMap.get(url));
                startActivity(intent);
                return true;
            }
            return false;
        }
    }

    private class PrepareContentTask extends AsyncTask<String, Void, Boolean> {

        private String url;

        private WebView mWebView;

        private String content;

        public PrepareContentTask(WebView webView) {
            mWebView = webView;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            url = params[0];
            boolean success = true;

            try {
                Document doc = Jsoup.connect(url).get();
                Elements imgs = doc.select("img");
                for (Element img : imgs) {
                    img.wrap("<a href='" + Utils.BBS_BASE_URL + img.attr("src") + "'></a>");
                    int pos = imgUrlMap.size();
                    imgUrlMap.put(Utils.BBS_BASE_URL + img.attr("src"), pos);
                }
                content = doc.outerHtml();
            } catch (IOException e) {
                e.printStackTrace();
                success = false;
            }

            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                mWebView.loadDataWithBaseURL(url, content, "text/html", "UTF-8", null);
            } else {
                mWebView.loadUrl(url);
            }
        }
    }
}
