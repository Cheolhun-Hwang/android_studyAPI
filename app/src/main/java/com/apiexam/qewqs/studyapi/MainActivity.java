package com.apiexam.qewqs.studyapi;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String base_URL = "";
    String result="";

    Button btnRequest;
    TextView xml_view;
    EditText url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRequest = (Button) findViewById(R.id.POST_XML_BUTTON);
        xml_view = (TextView)findViewById(R.id.XML_VIEW);
        url = (EditText) findViewById(R.id.URL_Edit);

        //텍스트스크롤기능
        btnRequest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //다이알로그
                final MyDialogFragment frag = MyDialogFragment.newInstance();
                frag.show(getFragmentManager(), "TAG");

                //작업스레드
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            base_URL = url.getText().toString();

                            xml_view.setText(getXmlData(base_URL));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        frag.dismiss();
                    }
                });

                t.start();
            }
        });

    }



    public static class MyDialogFragment extends DialogFragment {
        public static MyDialogFragment newInstance() {
            return new MyDialogFragment();
        }

        public Dialog onCreateDialog(Bundle savedInstancestate) {

            ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setTitle("네트워크");
            dialog.setMessage("요청중...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            return dialog;
        }
    }

    /////////////////////////////////////

    private String getXmlData(String url_base) {
        InputStream is;
        BufferedReader reader;
        StringBuilder sb;
        try {
            URL url = new URL(url_base); //문자열로 된 요청 url을 URL 객체로 생성.
            is = url.openStream();  //url위치로 입력스트림 연결

            reader = new BufferedReader(new InputStreamReader(is));
            sb = new StringBuilder();

            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "None";
        }


        return sb.toString();

    }
}
