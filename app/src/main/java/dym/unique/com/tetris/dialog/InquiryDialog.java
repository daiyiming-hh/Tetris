package dym.unique.com.tetris.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dym.unique.com.tetris.R;
import dym.unique.com.tetris.adapter.InquiryDialogSelectionAdapter;


/**
 * Created by daiyiming on 2016/1/7.
 */
public class InquiryDialog extends Dialog implements AdapterView.OnItemClickListener {

    private String title = "";
    private List<String> selections = null;
    private OnSelectionClickListener listener = null;
    private boolean isBackKeyEnable = true;

    public interface OnSelectionClickListener {
        void OnSelectionClicked(int position);
    }

    public InquiryDialog(Context context, String title, String[] selections, boolean isBackKeyEnable, OnSelectionClickListener listener) {
        super(context, R.style.DialogTranslationTheme);

        this.title = title;
        this.selections = new ArrayList<>();
        for (int i = 0; i < selections.length; i ++) {
            this.selections.add(selections[i]);
        }
        this.isBackKeyEnable = isBackKeyEnable;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_inquiry);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(title);

        ListView lv_selection = (ListView) findViewById(R.id.lv_selection);
        InquiryDialogSelectionAdapter adapter = new InquiryDialogSelectionAdapter(getContext(), selections);
        lv_selection.setAdapter(adapter);
        lv_selection.setOnItemClickListener(this);

    }

    @Override
    public void onBackPressed() {
        if (isBackKeyEnable) {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listener != null) {
            listener.OnSelectionClicked(position);
        }
        //点击后取消对话框
        this.dismiss();
    }

}
