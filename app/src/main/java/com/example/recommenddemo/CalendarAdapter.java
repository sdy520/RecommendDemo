package com.example.recommenddemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recommenddemo.room.HeNanPos;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder>{
    private Context mcontext;
    private List<HeNanPos> posList;


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        TextView textView5;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.pos_img);
            textView1=itemView.findViewById(R.id.pos_name);
            textView2=itemView.findViewById(R.id.pos_level);
            textView3=itemView.findViewById(R.id.pos_desc);
            textView4=itemView.findViewById(R.id.pos_location);
            textView5=itemView.findViewById(R.id.pos_rating);
        }
    }

    public CalendarAdapter(Context context, List<HeNanPos> posList) {
        mcontext=context;
        this.posList = posList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_pos_item,parent,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HeNanPos heNanPos = posList.get(position);
        Glide.with(mcontext)
                .load(heNanPos.getImage_link())
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.mipmap.no_image)
                .into(holder.imageView);
        holder.textView1.setText(heNanPos.getName());
        holder.textView2.setText(heNanPos.getRank());
        holder.textView3.setText(heNanPos.getIntro());
        holder.textView4.setText(heNanPos.getCity_name());
        holder.textView5.setText(String.valueOf(heNanPos.getRating()));
        if(mOnItemClickListener!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, holder.getAdapterPosition());
                    //在longClickListener中return true，消费掉事件，不然会同时触发click事件
                    return true;
                }
            });
        }

    }

    public void removeItem(int position){
        posList.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public int getItemCount() {
        return posList.size();
    }

    //1.定义变量接收接口
    private OnItemClickListener mOnItemClickListener;
    //2.定义接口：点击事件
    public interface OnItemClickListener {
        void onItemClick(View view, int position);//单击
        void onItemLongClick(View view, int position);//长按
    }
    //3.设置接口接收的方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

}
