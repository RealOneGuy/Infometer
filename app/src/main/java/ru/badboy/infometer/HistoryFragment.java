package ru.badboy.infometer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Евгений on 04.06.2016.
 */
public class HistoryFragment extends Fragment {
    private RecyclerView mContactRecycler;
    private HistoryAdapter mAdapter;

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, parent, false);

        mContactRecycler = (RecyclerView)v.findViewById(R.id.contacts_recyclerView);

        mAdapter = new HistoryAdapter(getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mContactRecycler.setLayoutManager(mLayoutManager);
        mContactRecycler.setItemAnimator(new DefaultItemAnimator());
        mContactRecycler.setAdapter(mAdapter);

        registerForContextMenu(mContactRecycler);

        return v;
    }

    public void wakeUpAdapter(){
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause(){
        super.onPause();
        SingleHistory.get(getActivity()).saveHistory();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_history_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear_history:
                AlertDialog.Builder clearHistoryDialog = new AlertDialog.Builder(getActivity());
                clearHistoryDialog.setTitle(getString(R.string.dialog_clear_history));

                clearHistoryDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SingleHistory.get(getActivity()).clearHistory();
                        mAdapter.notifyDataSetChanged();
                    }
                });

                clearHistoryDialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                clearHistoryDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        getActivity().getMenuInflater().inflate(R.menu.fragment_history_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position;
        try {
            position = mAdapter.getPosition();
        } catch (Exception e) {
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.menu_item_delete_history_item:
                SingleHistory.get(getActivity()).deleteHistoryItem(position);
                mAdapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
    }
}
