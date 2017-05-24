package com.hainantaxi.ui.citysearchpoi.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.hainantaxi.R;
import com.hainantaxi.ui.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by develop on 2017/5/25.
 */

public class CitySearchAdapter extends BaseRecyclerAdapter<Tip> {
    @Override
    protected int getLayoutResId() {
        return R.layout.item_city_input_tips;
    }

    @Override
    protected RecyclerView.ViewHolder createViewHolder(View itemView) {
        return new CitySearchViewHolder(itemView);
    }

    @Override
    protected void bind(RecyclerView.ViewHolder holder, int position, Tip itemData) {
        ((CitySearchViewHolder) holder).setName(itemData.getName()).setEvent(mOnItemClickListener,itemData,position);
    }

    class CitySearchViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.container)
        LinearLayout mContainer;

        public CitySearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public CitySearchViewHolder setName(String name) {
            mTvName.setText(name);
            return this;
        }

        public CitySearchViewHolder setEvent(OnItemClickListener listener, Tip tip, int position) {
            mContainer.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(tip, position);
                }
            });
            return this;
        }
    }
}
