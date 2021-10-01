package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String key = "wEOWhOEV95XFLOJ4fqYdWXwJMODD6Ze7R8%2FQDUZdmBrHnlVU8ER0P2YenWEkb4imfh7IvqniyzIfj%2BEZp%2BnG%2Fw%3D%3D";
    private String urlAddress = "https://api.odcloud.kr/api/gov24/v1/serviceList?page=1&perPage=10&serviceKey=wEOWhOEV95XFLOJ4fqYdWXwJMODD6Ze7R8%2FQDUZdmBrHnlVU8ER0P2YenWEkb4imfh7IvqniyzIfj%2BEZp%2BnG%2Fw%3D%3D";
    private Button btnData;

    ArrayList<ServiceData> serviceList = new ArrayList<ServiceData>();
//    private ArrayList<PlaceData> arraylist;
    ServiceAdapter serviceAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);
        serviceAdapter = new ServiceAdapter(this.getApplicationContext(), serviceList);
        listView.setAdapter(serviceAdapter);

        btnData = (Button) findViewById(R.id.btnData);

        btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        serviceList.clear();
                        try {
                            URL url = new URL(urlAddress);

                            InputStream is = url.openStream();
                            InputStreamReader isr = new InputStreamReader(is);
                            BufferedReader reader = new BufferedReader(isr);

                            StringBuffer buffer = new StringBuffer();
                            String line = reader.readLine();
                            while (line != null) {
                                buffer.append(line + "\n");
                                line = reader.readLine();
                            }

                            String jsonData = buffer.toString();

                            // jsonData를 먼저 JSONObject 형태로 바꾼다.
                            JSONObject obj = new JSONObject(jsonData);
                            // obj의 "serviceList"의 JSONObject를 추출
                            JSONArray serviceArray = (JSONArray) obj.get("data");

                            for (int i = 0; i < serviceArray.length(); i++) {
                                JSONObject temp = serviceArray.getJSONObject(i);
                                String serviceName = temp.getString("서비스명");
                                String serviceID = temp.getString("서비스ID");
                                String servicePurpose = temp.getString("서비스목적");
                                String target = temp.getString("지원대상");
                                String content = temp.getString("지원내용");
                                String select = temp.getString("선정기준");
                                String surl = temp.getString("상세조회URL");
                                String serviceDepart = temp.getString("부서명");

                                serviceList.add(new ServiceData(serviceName, serviceID, servicePurpose, target, content, select, surl, serviceDepart));
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    serviceAdapter.notifyDataSetChanged();
                                }
                            });

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
        // 리스트뷰의 아이템 클릭 이벤트 > 토스트 메시지 띄우기
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String data = (String) parent.getItemAtPosition(position);
//                Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
            }
        });

    }
}

class ServiceData{
    private String serviceName; //서비스명
    private String serviceID;   //서비스ID
    private String servicePurpose;  //서비스목적
    private String target;  //지원대상
    private String content; //지원내용
    private String select;   //선정기준
    private String url;     //상세조회URL
    private String serviceDepart; //부서명


    public ServiceData(){

    }

    public ServiceData(String serviceName, String serviceID, String servicePurpose, String target, String content, String select, String url, String serviceDepart){
        this.serviceName = serviceName;
        this.serviceID = serviceID;
        this.servicePurpose = servicePurpose;
        this.target = target;
        this.content = content;
        this.select = select;
        this.url = url;
        this.serviceDepart = serviceDepart;

    }
    public String getServiceName() {
        return this.serviceName;
    }
    public String getServiceID(){
        return this.serviceID;
    }
    public String getServicePurpose(){
        return this.servicePurpose;
    }
    public String getTarget(){
        return this.target;
    }
    public String getContent(){
        return this.content;
    }
    public String getSelect(){
        return this.select;
    }
    public String getUrl(){
        return this.url;
    }
    public String getServiceDepart(){
        return this.serviceDepart;
    }

}
/* data:
      "부서명": "유아교육정책과",
      "상세조회URL": "https://gov.kr/portal/rcvfvrSvc/dtlEx/000000465790",
      "서비스ID": "000000465790",
      "서비스명": "누리과정(유아학비) 지원",
      "서비스목적": "만 3~5세 누리과정 도입으로 유치원·어린이집에 국가수준 공통 교육과정(누리과정)을 적용함에 따라, 보호자의 소득수준에 관계없이 전 계층 유아학비, 보육료 지원",
      "선정기준": "○ 지원대상 : 유치원, 어린이집을 이용하는 만 3~5세 아동\r\n\r\n○ 신청인 : 아동의 보호자\r\n\r\n○ 신청장소 : 읍면동 주민센터(아동 주민등록 주소지)",
      "소관기관명": "교육부",
      "소관기관코드": "1342000",
      "신청기한": "상시신청",
      "신청방법": "읍면동 주민센터에 방문하거나 온라인으로 신청",
      "조회수": 645,
      "지원내용": "○ 만 3~5세 누리과정 운영에 필요한 비용 지원 \r\n - 유아학비, 방과후과정비 지원(국공립 유치원 : 13만원, 사립유치원 : 33만원 지원)\r\n - 보육료, 담당교사 처우 개선비, 운영비 지원(330,000원 지원)",
      "지원대상": "○ 지원대상 : 유치원, 어린이집을 이용하는 만 3~5세 아동\r\n\r\n○ 추가지원 : 저소득층 유아(유아학비 지원 대상 자격이 있고, 사립유치원에 다니는 법정저소득층(기초생활수급자, 차상위계층, 한부모 가정) 유아)",
      "지원유형": "현금"

 */
class ServiceAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    private ArrayList<ServiceData> data;
    private TextView sercviceNameTextView;
    private TextView serviceIDTextView;
    private TextView servicePurposeTextView;
    private TextView targetTextView;
    private TextView contentTextView;
    private TextView selectTextView;
    private TextView urlTextView;
    private TextView serviceDepartTextView;


    public ServiceAdapter() {}
    public ServiceAdapter(Context context, ArrayList<ServiceData> dataArray) {
        mContext = context;
        data = dataArray;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ServiceData getItem(int position) {
        return data.get(position);
    }


    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.map_listview_custom, null);

        sercviceNameTextView = (TextView) view.findViewById(R.id.sN);
        sercviceNameTextView.setText(data.get(position).getServiceName());
        serviceIDTextView = (TextView) view.findViewById(R.id.sID);
        serviceIDTextView.setText(data.get(position).getServiceID());
//        servicePurposeTextView = (TextView) view.findViewById(R.id.tel);
//        servicePurposeTextView.setText(data.get(position).getServicePurpose());
//        targetTextView = (TextView) view.findViewById(R.id.tel);
//        targetTextView.setText(data.get(position).getTarget());
//        contentTextView = (TextView) view.findViewById(R.id.tel);
//        contentTextView.setText(data.get(position).getContent());
//        selectTextView = (TextView) view.findViewById(R.id.tel);
//        selectTextView.setText(data.get(position).getSelect());
//        urlTextView = (TextView) view.findViewById(R.id.tel);
//        urlTextView.setText(data.get(position).getUrl());
        serviceDepartTextView = (TextView) view.findViewById(R.id.sD);
        serviceDepartTextView.setText(data.get(position).getServiceDepart());

        return view;
    }

}

