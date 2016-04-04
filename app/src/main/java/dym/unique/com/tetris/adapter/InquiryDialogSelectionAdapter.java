package dym.unique.com.tetris.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import dym.unique.com.tetris.R;

/**
 * Created by daiyiming on 2016/1/7.
 */
public class InquiryDialogSelectionAdapter extends BaseAdapter {

    private List<String> itemList = null;
    private Context context = null;

    public InquiryDialogSelectionAdapter(Context context, List<String> selections) {
        this.context = context;
        itemList = selections;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public String getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_inquiry_dialog_selection_item, parent, false);
        }
        TextView tv_selection = (TextView) convertView;
        tv_selection.setText(itemList.get(position));
        return convertView;
    }

}
