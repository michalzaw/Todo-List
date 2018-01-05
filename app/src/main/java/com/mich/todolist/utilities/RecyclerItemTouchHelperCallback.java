package com.mich.todolist.utilities;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.mich.todolist.adapters.TasksAdapter;

/**
 * Created by Michal on 04.01.2018.
 */

public class RecyclerItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {

    private RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelperCallback(RecyclerItemTouchHelperListener listener,
                                           int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);

        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (listener != null) {
            listener.onItemSwiped(viewHolder, direction);
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            View foreground = ((TasksAdapter.TasksViewHolder)viewHolder).getForegroundView();
            getDefaultUIUtil().onSelected(foreground);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        TasksAdapter.TasksViewHolder holder = (TasksAdapter.TasksViewHolder) viewHolder;

        View foreground = holder.getForegroundView();
        getDefaultUIUtil().onDrawOver(c, recyclerView, foreground, dX, dY, actionState, isCurrentlyActive);

        holder.setDeleteLabelVisibility(dX > 0 ? TasksAdapter.TasksViewHolder.LEFT : TasksAdapter.TasksViewHolder.RIGHT);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        View foreground = ((TasksAdapter.TasksViewHolder)viewHolder).getForegroundView();
        getDefaultUIUtil().clearView(foreground);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        TasksAdapter.TasksViewHolder holder = (TasksAdapter.TasksViewHolder) viewHolder;

        View foreground = holder.getForegroundView();
        getDefaultUIUtil().onDraw(c, recyclerView, foreground, dX, dY, actionState, isCurrentlyActive);
    }

    public interface RecyclerItemTouchHelperListener {
        void onItemSwiped(RecyclerView.ViewHolder viewHolder, int direction);
    }
}
