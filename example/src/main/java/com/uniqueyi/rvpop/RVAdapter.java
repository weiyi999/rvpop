package com.uniqueyi.rvpop;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by
 * author:  cwy.
 * date:    2017/9/29.  10:17.
 * prj:     RVPop.
 */

class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private List<String> list;
    private MClick mClick;

    public void setmClick(MClick mClick) {
        this.mClick = mClick;
    }

    public RVAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textView.setText(list.get(position));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mClick != null) {
                    mClick.longClick(holder.itemView, position, list.get(position));
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv);
        }
    }

    public interface MClick {
        void longClick(View v, int pos, String title);
    }

}
