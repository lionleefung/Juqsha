package lightcone.com.pack.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import lightcone.com.pack.R;

public class SmallRulerAdapter  extends RecyclerView.Adapter {
    private List<String> name;

    public SmallRulerAdapter(List<String> name){
        this.name = name;
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public static  class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;

        public  ViewHolder(View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.text_item_my_role);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_small_character, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder holder1 = (ViewHolder) holder;
        holder1.textView.setText(name.get(position));
        View itemView = holder1.itemView;
        if (mOnItemClickListener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder1.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder1.itemView, position);
                }
            });
        }
    }

    private OnItemClickedListener mOnItemClickListener;//声明接口

    public void setOnItemClickListener(OnItemClickedListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

}
