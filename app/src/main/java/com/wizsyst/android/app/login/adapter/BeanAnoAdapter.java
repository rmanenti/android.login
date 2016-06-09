package com.wizsyst.android.app.login.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wizsyst.android.app.login.R;
import com.wizsyst.sigem.mobile.sleo.beans.BeanAno;

import java.util.List;

/**
 * Created by rmanenti on 16/05/2016.
 */
public class BeanAnoAdapter extends BaseAdapter {

    private AppCompatActivity activity;
    private LayoutInflater inflater;
    private List<BeanAno> list;

    public BeanAnoAdapter( AppCompatActivity activity, List<BeanAno> list) {

        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {

        if ( list != null ) {
            return list.size();
        }

        return 0;
    }

    @Override
    public Object getItem(int location) {

        if ( list != null ) {
            return list.get(location);
        }

        return null;
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

        BeanAno ano = list.get( position );

        TextView item = ( TextView ) convertView.findViewById( R.id.default_list_item );
        item.setText( ano.getAno().toString() );

        return convertView;
    }
}
