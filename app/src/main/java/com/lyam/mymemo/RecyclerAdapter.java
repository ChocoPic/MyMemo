package com.lyam.mymemo;

import android.app.AlertDialog;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.mViewHolder> {

    private ArrayList<MyMemo> items;
    private SQLdbHelper helper;
    private Context context;
    private int color;

    public class mViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView memoText;
        TextView colorTag;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            colorTag = itemView.findViewById(R.id.colorPic);
            memoText = itemView.findViewById(R.id.memoText);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem edit = menu.add(Menu.NONE, 1, 1, "수정");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "삭제");
            edit.setOnMenuItemClickListener(onMenu);
            delete.setOnMenuItemClickListener(onMenu);
        }

        private MenuItem.OnMenuItemClickListener onMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                color = R.color.myGray;
                switch (item.getItemId()) {
                    case 1:
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        View view = LayoutInflater.from(context).inflate(R.layout.edit_popup, null, false);
                        builder.setView(view);
                        Button save = view.findViewById(R.id.btn1);
                        Button exit = view.findViewById(R.id.btn2);
                        final Button colorPicker = view.findViewById(R.id.color_picker);
                        colorPicker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //컬러피커띄우기
                                ColorPicker colorPicker = new ColorPicker(context, new ColorPicker.DialogListener() {
                                    @Override
                                    public void Listener(int colorValue) {
                                        color = colorValue;
                                    }
                                });
                                colorPicker.callDialog();
                                colorTag.setBackgroundResource(color);
                                System.out.println("고른색상 " + color);
                            }
                        });
                        final EditText memoText = view.findViewById(R.id.editMemoText);
                        memoText.setText(items.get(getAdapterPosition()).getData());
                        final AlertDialog dialog = builder.create();
                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String memo = memoText.getText().toString();
                                int pos = getAdapterPosition();
                                System.out.println("내용은 " + memo + "어댑터포지션은 "+ pos + "색상은 " + color);
                                helper.updateMemo(memo, items.get(pos).getData(), color);
                                items.set(pos, new MyMemo(memo,color));
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });
                        exit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        break;
                    case 2:
                        int pos = getAdapterPosition();
                        String memo = items.get(pos).getData();
                        items.remove(pos);
                        helper.deleteMemo(memo);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, items.size());
                        break;
                }
                return true;
            }
        };
    }
    /*
    int adapterPos = getAdapterPosition();
    if(adapterPos != RecyclerView.NO_POSITION){...}
     */

    public RecyclerAdapter(ArrayList<MyMemo> items, Context context) {
        this.items = items;
        this.context = context;
        this.helper = helper.getInst(context);
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        RecyclerAdapter.mViewHolder holder = new RecyclerAdapter.mViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        String memo = items.get(position).getData();
        int color = items.get(position).getColor();
        holder.memoText.setText(memo);
        holder.colorTag.setBackgroundResource(color);
    }

    @Override
    public int getItemCount() {
        return (items == null) ? 0 : items.size();
    }

}
