package com.zqw.dropadownmenu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zqw.dropadownmenu.R;

import java.util.List;

/**
 * Created by 启文 on 2018/5/21.
 */
public class ListDropDownAdapter extends BaseAdapter {

    private Context context;
    private List<String> data;
    private int checkItemPosition = 0;

    public void setCHeckItem(int i){
        checkItemPosition = i;
        notifyDataSetChanged();
    }

    public ListDropDownAdapter(Context context, List<String> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;

        if(view != null){
            viewHolder = (ViewHolder) view.getTag();
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.item_default_drop_down, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_age = view.findViewById(R.id.tv_age);
            view.setTag(viewHolder);
        }

        fillValue(i, viewHolder);
        return view;
    }

    class ViewHolder{
        TextView tv_age;
    }

    private void fillValue(int i, ViewHolder viewHolder){
        viewHolder.tv_age.setText(data.get(i));

        if(checkItemPosition != -1){
            viewHolder.tv_age.setTextColor(context.getResources().getColor(R.color.drop_down_selected));
            viewHolder.tv_age.setBackgroundResource(R.color.check_bg);

        }else {
            viewHolder.tv_age.setTextColor(context.getResources().getColor(R.color.drop_down_unselected));
            viewHolder.tv_age.setBackgroundResource(R.color.white);

        }



    }
}
