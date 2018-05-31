package com.zqw.dropadownmenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.zqw.dropadownmenu.adapter.ConstellationAdapter;
import com.zqw.dropadownmenu.adapter.GirdDropDownAdapter;
import com.zqw.dropadownmenu.adapter.ListDropDownAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 通过自定义控件仿美团下拉菜单
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private DropDownMenu dropDownMenu;
    private ListDropDownAdapter ageAdapter, sexAdaper;
    private GirdDropDownAdapter cityAdapter;
    private ConstellationAdapter constellationAdapter;  //星座适配器

    private String headers[] = {"城市", "年龄", "性别", "星座"};

    private List<View> popViews = new ArrayList<View>();

    private String citys[] = {"不限", "不限", "不限", "不限", "不限", "不限", "不限", "不限", "不限", "不限", "不限", "不限", "不限"};
    private String ages[] = {"不限", "不限", "不限", "不限", "不限"};
    private String sexs[] = {"不限", "男", "女"};
    private String constellations[] = {"不限", "不限", "不限", "不限", "不限", "不限", "不限", "不限", "不限", "不限", "不限", "不限", "不限"};

    private int constellationPosition = 0;

    private ListView lvCity;
    private ListView lvAge;
    private ListView lvSex;
    private GridView gridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dropDownMenu = (DropDownMenu) findViewById(R.id.dropDownMenu);

        initView();

    }

    private void initView() {

        lvCity = new ListView(this);
        cityAdapter = new GirdDropDownAdapter(this, Arrays.asList(citys));
        lvCity.setDividerHeight(0);
        lvCity.setAdapter(cityAdapter);

        lvAge = new ListView(this);
        ageAdapter = new ListDropDownAdapter(this, Arrays.asList(ages));
        lvAge.setDividerHeight(0);
        lvAge.setAdapter(ageAdapter);

        lvSex = new ListView(this);
        sexAdaper = new ListDropDownAdapter(this, Arrays.asList(sexs));
        lvSex.setDividerHeight(0);
        lvSex.setAdapter(sexAdaper);

        View constellationView = getLayoutInflater().inflate(R.layout.layout_constellation, null);
        gridView = constellationView.findViewById(R.id.constellation);
        TextView tvOk = constellationView.findViewById(R.id.tv_ok);
        constellationAdapter = new ConstellationAdapter(this, Arrays.asList(constellations));
        gridView.setAdapter(constellationAdapter);

        lvCity.setOnItemClickListener(this);
        lvAge.setOnItemClickListener(this);
        lvSex.setOnItemClickListener(this);
        gridView.setOnItemClickListener(this);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        popViews.add(lvCity);
        popViews.add(lvAge);
        popViews.add(lvSex);
        popViews.add(constellationView);

        TextView tv_context = new TextView(this);
        tv_context.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tv_context.setGravity(Gravity.CENTER);

        try{
            dropDownMenu.setDropDownMenu(Arrays.asList(headers), popViews, tv_context);
        } catch (Exception e){
            e.printStackTrace();
        }


    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
