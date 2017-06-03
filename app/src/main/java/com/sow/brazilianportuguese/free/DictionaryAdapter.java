package com.sow.brazilianportuguese.free;

import android.content.Context;
import android.graphics.Bitmap;
//import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DictionaryWord> resultsArrayList = new ArrayList<>();
    private OnItemClickListener mItemClickListener;

    public DictionaryAdapter(Context context, String stringBuffer) {
        this.context = context;
        resultsArrayList = new DictionaryProvider(context, stringBuffer).getDictionaryWords();
    }

    @Override
    public DictionaryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_result, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DictionaryAdapter.ViewHolder holder, final int position) {
        holder.textView_line1.setText(resultsArrayList.get(position).getWord());
        holder.textView_line2.setText(resultsArrayList.get(position).getType());
        holder.textView_line3.setText(resultsArrayList.get(position).getTranslation());
    }

//    public Palette createPaletteSync(Bitmap bitmap) {
//        Palette p = Palette.from(bitmap).generate();
//        return p;
//    }


    @Override
    public int getItemCount() {
        return resultsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView_line1, textView_line2, textView_line3;

        public ViewHolder(View itemView) {
            super(itemView);
            textView_line1 = (TextView) itemView.findViewById(R.id.textView_line1);
            textView_line2 = (TextView) itemView.findViewById(R.id.textView_line2);
            textView_line3 = (TextView) itemView.findViewById(R.id.textView_line3);
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
