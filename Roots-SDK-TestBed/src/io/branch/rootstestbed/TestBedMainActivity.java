package io.branch.rootstestbed;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import io.branch.roots.Roots;

/**
 * <p>
 * Class to demonstrate and test the Roots sdk. This app will open any app using the url
 * provided if app is installed. Fall back to a play store or a website is app is not installed.
 * Also show how to debug Roots SDK with debug app link data
 * </p>
 */
public class TestBedMainActivity extends Activity implements Roots.IRootsEvents {
    ProgressDialog progressDialog_;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_bed_main);
        final TextView utlTxtView = (TextView) findViewById(R.id.app_url_txt);


        findViewById(R.id.app_nav_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog_ = ProgressDialog.show(TestBedMainActivity.this, utlTxtView.getText().toString(), "Opening", true);
                progressDialog_.setCancelable(true);
                String navUrl = utlTxtView.getText().toString();

                Roots roots = new Roots(TestBedMainActivity.this, navUrl)
                        .setRootsConnectionEventsCallback(TestBedMainActivity.this)
                        .setAlwaysFallbackToPlayStore(((CheckBox) findViewById(R.id.web_fallback_chkbx)).isChecked());

                if (URLUtil.isValidUrl(navUrl)) {
                    roots.connect(); // Open the using the URL entered
                } else {     // Set debug data to open RootsRoutingTestbed app
                    try {
                        roots.debugConnect("https://my_awesome_site.com/user/my_user_id123456",
                                new JSONArray("[{\"property\":\"al:android:url\"," +
                                        "\"content\":\"myscheme://mypath/user/my_user_id1234/my_username\"}," +
                                        "{\"property\":\"al:android:package\",\"content\":\"io.branch.rootsroutingtestbed\"}," +
                                        "{\"property\":\"al:android:app_name\",\"content\":\"RootsTestBed\"}]"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    @Override
    public void onAppLaunched(String appName, String packageName) {
        if (progressDialog_ != null) {
            progressDialog_.dismiss();
        }
    }

    @Override
    public void onFallbackUrlOpened(String url) {
        if (progressDialog_ != null) {
            progressDialog_.dismiss();
        }
    }

    @Override
    public void onPlayStoreOpened(String appName, String packageName) {
        if (progressDialog_ != null) {
            progressDialog_.dismiss();
        }
    }
}
