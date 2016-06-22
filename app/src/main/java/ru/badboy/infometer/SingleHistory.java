package ru.badboy.infometer;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Евгений on 04.06.2016.
 */
public class SingleHistory {
    private static final String FILENAME = "history.json";

    private ArrayList<HistoryItem> mHistoryItems;
    private InfoJSONSerializer mSerializer;

    private static SingleHistory sHistory;
    private Context mAppContext;

    private SingleHistory(Context appContext){
        mAppContext = appContext;
        mSerializer = new InfoJSONSerializer(mAppContext, FILENAME);
        try {
            mHistoryItems = mSerializer.loadHistory();
        }
        catch (Exception e) {
            mHistoryItems = new ArrayList<>();
        }
    }

    public static SingleHistory get(Context c){
        if (sHistory == null){
            sHistory = new SingleHistory(c.getApplicationContext());
        }
        return sHistory;
    }

    public void addHistoryItem(HistoryItem item){
        for (HistoryItem mItem : mHistoryItems){
            if (mItem.getQuestion().equals(item.getQuestion())){
                mHistoryItems.remove(mItem);
                break;
            }
        }
        mHistoryItems.add(0,item);
    }

    public void deleteHistoryItem(int position){
        mHistoryItems.remove(position);
    }

    public void clearHistory(){
        mHistoryItems.clear();
    }

    public ArrayList<HistoryItem> getHistoryItems() {
        return mHistoryItems;
    }

    public boolean saveHistory() {
        try {
            mSerializer.saveInfo(mHistoryItems);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
