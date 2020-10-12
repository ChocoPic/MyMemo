package com.lyam.mymemo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.Nullable;

public class ColorPicker extends Dialog{

    private static final int [] BtnIDs = {R.id.red, R.id.yellow, R.id.green, R.id.blue, R.id.purple,R.id.gray, R.id.black};
    private static final int COLOR = 7;
    private Context context;
    private int color_code;
    private Dialog dialog;
    private Button[] color_btn;

    public interface DialogListener{
        public void Listener(int colorValue);
    }
    private DialogListener onDialogListener;

    public ColorPicker(Context context, DialogListener onDialogListener){
        super(context);
        this.context = context;
        this.onDialogListener = onDialogListener;
    }

    public void callDialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.color_picker);

        color_btn = new Button[COLOR];

        System.out.println("다이얼로그 열림");

        for(int i=0; i<COLOR; i++){
            color_btn[i] = (Button)dialog.findViewById(BtnIDs[i]);
            color_btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.red:
                            color_code = R.color.myRed;
                            break;
                        case R.id.yellow:
                            color_code = R.color.myYellow;
                            break;
                        case R.id.green:
                            color_code = R.color.myGreen;
                            break;
                        case R.id.blue:
                            color_code = R.color.myBlue;
                            break;
                        case R.id.purple:
                            color_code = R.color.myPurple;
                            break;
                        case R.id.gray:
                            color_code = R.color.myGray;
                            break;
                        case R.id.black:
                            color_code = R.color.myBlack;
                            break;
                    }
                    onDialogListener.Listener(color_code);
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    public int getColor(){
        System.out.println("색은 "+color_code);
        return color_code;
    }

}
