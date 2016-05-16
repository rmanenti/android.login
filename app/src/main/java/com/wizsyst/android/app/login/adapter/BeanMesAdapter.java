package com.wizsyst.android.app.login.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wizsyst.android.app.login.R;
import com.wizsyst.android.app.login.activity.base.BaseActivity;
import com.wizsyst.android.app.login.utilities.temporal.DateUtils;
import com.wizsyst.sigem.mobile.sleo.beans.BeanMes;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by rmanenti on 16/05/2016.
 */
public class BeanMesAdapter extends BaseAdapter {

    private BaseActivity activity;
    private LayoutInflater inflater;
    private List<BeanMes> list;

    public BeanMesAdapter(BaseActivity activity, List<BeanMes> list) {

        this.activity = activity;
        this.list = list;

        Collections.sort(this.list, new Comparator<BeanMes>() {

            @Override
            public int compare(BeanMes lhs, BeanMes rhs) {
                return lhs.getMes() - rhs.getMes();
            }
        });
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int location) {
        return list.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if ( inflater == null ) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if ( convertView == null ) {
            convertView = inflater.inflate( R.layout.adapter_default_list_view, null);
        }

        BeanMes mes = list.get( position );

        TextView item = ( TextView ) convertView.findViewById( R.id.default_list_item );
        item.setText( DateUtils.getMonthName( mes.getMes() -1 ) );

        return convertView;
    }
}
