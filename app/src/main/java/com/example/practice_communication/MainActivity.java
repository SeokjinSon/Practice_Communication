package com.example.practice_communication;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import com.example.practice_communication.TagName;

public class MainActivity extends AppCompatActivity {

    // Member Variable
    private String                              myJSON;
    private JSONArray                           peoples     = null;
    private ArrayList<HashMap<String, String>>  personList;

    // View Variable
    private ListView                            list;

    // MainActivity's Override Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getData("http://IP주소/xxx.php");
    }

    // MainActivity's Member Method
    public void init() {
        list = findViewById(R.id.listview);
        personList = new ArrayList<HashMap<String, String>>(); // DB에서 받아온 json값은 속성과 값으로 이루어져 있으므로
    }


    public void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TagName.TAG_RESULT); // jsonobject가 배열 형태로 있음(모음)

            for(int i=0; i<peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String age = c.getString(TagName.TAG_AGE);
                String name = c.getString(TagName.TAG_NAME);
                String address = c.getString(TagName.TAG_ADD);

                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TagName.TAG_AGE, age);
                persons.put(TagName.TAG_NAME, name);
                persons.put(TagName.TAG_ADD, address);

                personList.add(persons);
            }
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, personList, R.layout.list_item,
                                                        new String[]{TagName.TAG_AGE, TagName.TAG_NAME, TagName.TAG_ADD},
                                                        new int[]{R.id.age, R.id.name, R.id.address});
            list.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            // 쓰레드에 의해 처리될 내용을 담은 함수
            // params : execute 함수의 인자로 전달되는 값(...은 배열이라고 생각)
            // execute 함수에 의해 호출
            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                BufferedReader bufferedReader = null;

                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection(); // 전달받은 url을 통해 연결
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream())); // inputstream을 통해 php 실행 결과 가져옴(json)
                    String json;
                    while((json = bufferedReader.readLine()) != null) {
                        Log.i("execute() => ", json);
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim(); // onPostExecute 함수로 넘겨주는 값
                } catch (Exception e) {
                    return null;
                }
            }
            // AsyncTask의 모든 작업이 완료된 후 가장 마지막에 호출(doInBackground 함수의 최종 값을 받기 위해)
            // result : json형태의 값(전체 뭉탱이)
            @Override
            protected void onPostExecute(String result) {
                Log.i("onPostExecute =>", result);
                myJSON = result;
                showList();
            }
        }

        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
}