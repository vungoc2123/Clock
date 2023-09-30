package com.example.callback;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapter.internationalTimeAdapter;
import com.example.clock.R;

public class SwipeToDeleteCallBack extends ItemTouchHelper.Callback {
    private internationalTimeAdapter adapter;

    public SwipeToDeleteCallBack(internationalTimeAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
//        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END; // Xác định hướng swipe
//        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN; // Xác định hướng kéo
        return makeMovementFlags(ItemTouchHelper.END , ItemTouchHelper.START);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }


    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;

        viewHolder.itemView.setOnClickListener(view -> {

        });
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Xác định hướng vuốt (trái hoặc phải)
            if (dX > 0) {

            } else {
                // Vuốt sang trái
                // Tùy chỉnh hiển thị (ví dụ: thay đổi màu nền hoặc hiển thị nút xóa
                itemView.setTranslationX(Math.max(dX, -itemView.findViewById(R.id.tv_itn_delete).getWidth()));
            }
            if(!isCurrentlyActive){
                TextView textView = viewHolder.itemView.findViewById(R.id.tv_itn_delete);
                textView.setOnClickListener(view -> {
                    System.out.println("ihi");
                });
            }
        }
    }
}
