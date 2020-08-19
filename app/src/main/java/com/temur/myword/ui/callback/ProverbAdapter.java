package com.temur.myword.ui.callback;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.temur.myword.R;
import com.temur.myword.model.ProverbList;

import java.util.ArrayList;
import java.util.List;

public class ProverbAdapter extends RecyclerView.Adapter<ProverbAdapter.ProverbViewHolder> implements Filterable {

    private Context context;
    private List<ProverbList.Proverb> list;
    private List<ProverbList.Proverb> fullList;
    OnProverbClickListener onProverbClickListener;

    public ProverbAdapter(Context context, List<ProverbList.Proverb> list, OnProverbClickListener onProverbClickListener) {
        this.context = context;
        this.list = list;
        fullList = new ArrayList<>(list);
        this.onProverbClickListener = onProverbClickListener;
    }

    @NonNull
    @Override
    public ProverbViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_proverb, parent, false);

        return new ProverbViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProverbViewHolder holder, int position) {
        holder.text_proverb.setText(list.get(position).getProverb());
        holder.cardview_proverb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProverbClickListener.onProverbClick(position);
                animateFadeIn(holder.cardview_proverb);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void changeList(List<ProverbList.Proverb>proverbList){
        list.clear();
        list.addAll(proverbList);
        notifyDataSetChanged();
    }

    class ProverbViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout layout_main;
        CardView cardview_proverb;
        TextView text_proverb;

        public ProverbViewHolder(@NonNull View itemView) {
            super(itemView);

            layout_main = itemView.findViewById(R.id.layout_main);
            cardview_proverb = itemView.findViewById(R.id.cardview_proverb);
            text_proverb = itemView.findViewById(R.id.text_proverb);

        }
    }

    @Override
    public Filter getFilter() {
        return listFilter;
    }

    private Filter listFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ProverbList.Proverb> filteredList = new ArrayList<>();
            if(constraint==null || constraint.length()==0){
                filteredList = fullList;
            }else {
                String filteredPattern = constraint.toString().toLowerCase().trim();
                for (ProverbList.Proverb item : fullList){
                    if(item.getProverb().toLowerCase().contains(filteredPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public interface OnProverbClickListener{
        void onProverbClick(int position);
    }

    public void animateFadeIn(View slide){
        YoYo.with(Techniques.FadeIn)
                .duration(400)
                .playOn(slide);
    }
}
