package com.sow.brazilianportuguese.free;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CategoryProvider.Category> categoryArrayList = new ArrayList<>();
    private OnItemClickListener mItemClickListener;

    public CategoryAdapter(Context context) {
        this.context = context;
        categoryArrayList = new CategoryProvider(context).getCategoryArrayList();
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_category, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CategoryAdapter.ViewHolder holder, final int position) {
        holder.textView_title.setText(categoryArrayList.get(position).getTitle());
        holder.textView_subtitle.setText(categoryArrayList.get(position).getSubtitle());
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView_title, textView_subtitle;
        RelativeLayout relativeLayout_category;

        public ViewHolder(View itemView) {
            super(itemView);
            relativeLayout_category = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_category);
            textView_title = (TextView) itemView.findViewById(R.id.textView_row_category_title);
            textView_subtitle = (TextView) itemView.findViewById(R.id.textView_row_category_subtitle);
            relativeLayout_category.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
