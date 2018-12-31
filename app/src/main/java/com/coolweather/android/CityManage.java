package com.coolweather.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.coolweather.android.db.SaveData;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static org.litepal.LitePalApplication.getContext;

public class CityManage extends AppCompatActivity {
    private Button backBt;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<SaveData> saveDataList;
    private List<SaveData> selectList;
    private SaveData deleteList;
    private List<String> dataList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manage);
        backBt=findViewById(R.id.back_button_1);
        listView=findViewById(R.id.saveCity_list);

//        从数据库调出全部数据到saveDataList
        saveDataList=DataSupport.findAll(SaveData.class);
        if(saveDataList.size()>0)
        {
            for(SaveData saveData:saveDataList)
            {
                dataList.add(saveData.csName);
            }
        }
        adapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);

//        点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saveDataList=DataSupport.findAll(SaveData.class);
                String city_id=saveDataList.get(position).getCsCode();
                Intent intent=new Intent(CityManage.this,WeatherActivity.class);
                intent.putExtra("weather_id",city_id);
                startActivity(intent);
                finish();
            }
        });


//        长按删除
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder test = new AlertDialog.Builder(CityManage.this);
                test.setPositiveButton("sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         deleteList=saveDataList.get(position);
                         DataSupport.deleteAll(SaveData.class,"csName = ?",deleteList.getCsName());
                         selectList=DataSupport.findAll(SaveData.class);
                         System.out.println("selectList.size()"+selectList.size());
                         if (selectList.size()>0)
                         {
                             dataList.clear();
                             //listView.setEnabled(true);
                             for (SaveData saveData:selectList)
                                 dataList.add(saveData.csName);
                         }
                         else
                         {
                             dataList.clear();
                             //listView.setEnabled(false);
                         }
                        adapter = new ArrayAdapter<>(CityManage.this, android.R.layout.simple_list_item_1, dataList);
                        listView.setAdapter(adapter);
                    }
                });

                test.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                test.setMessage("删除！");
                test.setTitle("提示框");
                test.show();
                return true;
            }
        });



        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
