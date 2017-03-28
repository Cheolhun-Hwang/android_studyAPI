package com.apiexam.qewqs.studyapi;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String base_URL = "";
    String result="";

    TextView xml_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnRequest = (Button) findViewById(R.id.POST_XML_BUTTON);
        TextView textview = (TextView) findViewById(R.id.XML_VIEW);

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

                            base_URL = "http://apis.data.go.kr/1300000/bistGongseok/list/bistGongseok/list?serviceKey=6tLZdNCgFtUUkC1aMEPPSDH5EqZB09HbJ9vEwO1DeRGItkpZQzyAxdTw2npenOfhIQdsklstTNt9qrj2RODhkQ%3D%3D&numOfRows=10&pageSize=10&pageNo=1&startPage=1";
                            //base_URL = "http://www.naver.com/";
                            getXmlData(base_URL);
                            xml_view = (TextView)findViewById(R.id.XML_VIEW);
                            xml_view.setText(result);


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

    void getXmlData(String url_base) {

        try {
            URL url = new URL(url_base); //문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream();  //url위치로 입력스트림 연결
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));  //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();
                        //테그 이름 얻어오기
                        if (tag.equals("bjdsggjusoCd")) {
                            xpp.next();
                            result += "우편주소 : " + xpp.getText() + "\n";
                        }else if(tag.equals("bjdsggjusoNm")){
                            xpp.next();
                            result += "주소 : " + xpp.getText() + "\n";
                        }else if(tag.equals("bmgigwanNm")){
                            xpp.next();
                            result += "기관명 : " + xpp.getText() + "\n";
                        }else if(tag.equals("rnum")){
                            xpp.next();
                            result += "공석수 : " + xpp.getText() + "\n";
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG: // </> End Tag
                        tag = xpp.getName();    //테그 이름 얻어오기

                        if (tag.equals("item")){

                        }



                        break;
                }

                eventType = xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
