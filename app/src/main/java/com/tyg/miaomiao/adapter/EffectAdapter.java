package com.tyg.miaomiao.adapter;

import android.content.Context;
  import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tyg.miaomiao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * effect 适配器
 *
 * @packageName: cn.tongue.tonguecamera.adapter
 * @fileName: EffectAdapter
 * @date: 2019/4/17  14:33
 * @author: ymc
 * @QQ:745612618
 */

public class EffectAdapter extends RecyclerView.Adapter<EffectAdapter.EffectViewHolder> {
    private LayoutInflater mLayoutInflater;
    private String[] effectArr;

    public EffectAdapter(Context mContext, String[] arr) {
        this.effectArr = arr;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public EffectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EffectViewHolder(mLayoutInflater.inflate(R.layout.item_rv_text, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EffectViewHolder holder, int position) {
        holder.mTextView.setText(effectArr[position]);
    }

    @Override
    public int getItemCount() {
        return effectArr.length;
    }

    public class EffectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.text_view)
        TextView mTextView;

        EffectViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (effectOnItemClickListener != null) {
                effectOnItemClickListener.itemOnClick(getPosition());
            }
        }
    }

    private EffectOnItemClickListener effectOnItemClickListener;

    public interface EffectOnItemClickListener {

        void itemOnClick(int position);

    }

    public void setEffectOnItemClickListener(EffectOnItemClickListener effectOnItemClickListener) {
        this.effectOnItemClickListener = effectOnItemClickListener;
    }
}
