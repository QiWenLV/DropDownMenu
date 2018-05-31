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
public class GirdDropDownAdapter extends BaseAdapter {

    private Context context;
    private List<String> data;
    private int checkItemPosition = 0;

    public void setCHeckItem(int i){
        checkItemPosition = i;
        notifyDataSetChanged();
    }

    public GirdDropDownAdapter(Context context, List<String> data){
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
            view = LayoutInflater.from(context).inflate(R.layout.item_list_drop_down, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_city = view.findViewById(R.id.tv_city);
            view.setTag(viewHolder);
        }
        fillValue(i, viewHolder);
        return view;
    }

    class ViewHolder{
        TextView tv_city;
    }

    private void fillValue(int i, ViewHolder viewHolder){
        viewHolder.tv_city.setText(data.get(i));

        if(checkItemPosition != -1){
            viewHolder.tv_city.setTextColor(context.getResources().getColor(R.color.drop_down_selected));
            viewHolder.tv_city.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.drop_down_selected), null);

        }else {
            viewHolder.tv_city.setTextColor(context.getResources().getColor(R.color.drop_down_unselected));
            viewHolder.tv_city.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

        }



    }
}
