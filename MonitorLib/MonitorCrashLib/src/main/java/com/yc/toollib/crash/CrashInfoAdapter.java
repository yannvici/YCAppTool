package com.yc.toollib.crash;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.TextUtils;
import android.widget.TextView;

import com.yc.eastadapterlib.BaseRecycleAdapter;
import com.yc.eastadapterlib.BaseViewHolder;
import com.yc.toollib.R;

import java.io.File;

public class CrashInfoAdapter extends BaseRecycleAdapter<File> {

    public CrashInfoAdapter(Context context) {
        super(context, R.layout.item_crash_view);
    }

    @Override
    protected void bindData(BaseViewHolder holder, File file) {
        TextView tvTitle = holder.getView(R.id.tv_title);
        TextView tvPath = holder.getView(R.id.tv_path);
        tvPath.setText(file.getAbsolutePath());
        //动态修改颜色
        String fileName = file.getName().replace(".txt", "");
        String[] splitNames = fileName.split("_");
        Spannable spannable = null;
        if (splitNames.length == 3) {
            String errorMsgType = splitNames[2];
            if (!TextUtils.isEmpty(errorMsgType)) {
                spannable = Spannable.Factory.getInstance().newSpannable(fileName);
                spannable = CrashHelperUtils.addNewSpan(context, spannable, fileName, errorMsgType,
                        Color.parseColor("#FF0006"), 0);
            }
        }
        if (spannable != null) {
            tvTitle.setText(spannable);
        } else {
            tvTitle.setText(fileName);
        }
    }

}
