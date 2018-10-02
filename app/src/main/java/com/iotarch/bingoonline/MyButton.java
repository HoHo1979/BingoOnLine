package com.iotarch.bingoonline;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class MyButton extends android.support.v7.widget.AppCompatButton {

    int number;

    Boolean isSelected=false;

    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
