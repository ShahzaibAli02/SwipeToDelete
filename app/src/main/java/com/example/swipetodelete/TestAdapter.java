package com.example.swipetodelete;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class TestAdapter extends RecyclerView.Adapter {

    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec


    Context context;
    List<String> items;
    List<String> itemsPendingRemoval;
    int lastInsertedIndex; // so we can add some more items for testing purposes
    boolean undoOn; // is undo on, you can turn it on from the toolbar menu

    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<String, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be

    public TestAdapter(Context context)
    {
        this.context=context;
        items = new ArrayList<>();
        itemsPendingRemoval = new ArrayList<>();
        // let's generate some items
        lastInsertedIndex = 15;
        // this should give us a couple of screens worth
        for (int i=1; i<= lastInsertedIndex; i++) {
            items.add("Item " + i);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TestViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TestViewHolder viewHolder = (TestViewHolder)holder;
        // we need to show the "normal" state
        viewHolder.itemView.setBackgroundColor(Color.WHITE);
        viewHolder.titleTextView.setVisibility(View.VISIBLE);
        viewHolder.titleTextView.setText("item");
        viewHolder.undoButton.setVisibility(View.GONE);
        viewHolder.undoButton.setOnClickListener(null);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void addItems(int howMany){
        if (howMany > 0)
        {
            for (int i = lastInsertedIndex + 1; i <= lastInsertedIndex + howMany; i++) {
                items.add("Item " + i);
                notifyItemInserted(items.size() - 1);
            }
            lastInsertedIndex = lastInsertedIndex + howMany;
        }
    }


    public Context getContext() {

        return context;
    }

    public void deleteTask(int position) {
        items.remove(position);
        notifyDataSetChanged();

    }
}

/**
 * ViewHolder capable of presenting two states: "normal" and "undo" state.
 */
 class TestViewHolder extends RecyclerView.ViewHolder {

    TextView titleTextView;
    Button undoButton;

    public TestViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent, false));
        titleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
        undoButton = (Button) itemView.findViewById(R.id.undo_button);
    }

}