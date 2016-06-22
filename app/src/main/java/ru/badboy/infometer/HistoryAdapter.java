package ru.badboy.infometer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Евгений on 01.06.2016.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder>{
    private Context mAppContext;

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public HistoryAdapter(Context appContext){
        super();
        mAppContext = appContext;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        public TextView question, answer;

        public MyViewHolder(View view) {
            super(view);
            question = (TextView) view.findViewById(R.id.question_text);
            answer = (TextView) view.findViewById(R.id.answer_text);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, R.id.menu_item_delete_history_item,
                    Menu.NONE, R.string.remove_history_item);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        HistoryItem item = SingleHistory.get(mAppContext.getApplicationContext()).getHistoryItems().get(position);
        holder.question.setText(item.getQuestion());
        holder.answer.setText(item.getAnswer());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getPosition());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return SingleHistory.get(mAppContext.getApplicationContext()).getHistoryItems().size();
    }
}
