package com.example.fadebook.ui.MainActivity.SelectLang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fadebook.R;
import com.example.fadebook.pojo.modules.LangInfo;

public class LanguagesAdapter extends RecyclerView.Adapter<LanguagesAdapter.LanguagesViewHolder> {

    private Context mContext;
    private LangInfo langInfo;

    public LanguagesAdapter(Context mContext, LangInfo langInfo) {
        this.mContext = mContext;
        this.langInfo = langInfo;
    }

    @NonNull
    @Override
    public LanguagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.lang_row, parent, false);
        LanguagesViewHolder viewHolder = new LanguagesViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LanguagesViewHolder holder, int position) {
        String currentLang = langInfo.getLang().get(position);
        int currentImg = langInfo.getLangFlag().get(position);
        holder.txtLang.setText(currentLang);
        holder.imgCountry.setImageResource(currentImg);

        boolean[] isClicked = new boolean[1];
        isClicked[0] = false;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isClicked[0] = !isClicked[0];
                if (isClicked[0]) {
                    holder.imgChecked.setVisibility(View.VISIBLE);
                }else{
                    holder.imgChecked.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return langInfo.getLang().size();
    }

    class LanguagesViewHolder extends RecyclerView.ViewHolder {
        TextView txtLang;
        ImageView imgCountry, imgChecked;

        public LanguagesViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLang = itemView.findViewById(R.id.txt_lang);
            imgChecked = itemView.findViewById(R.id.img_checked);
            imgCountry = itemView.findViewById(R.id.img_flag);
        }
    }

}
