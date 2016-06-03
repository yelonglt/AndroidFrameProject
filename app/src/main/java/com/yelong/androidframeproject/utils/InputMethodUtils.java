package com.yelong.androidframeproject.utils;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 软键盘管理工具类
 * Created by eyetech on 16/5/26.
 * mail:354734713@qq.com
 */
public class InputMethodUtils {

    /**
     * 为给定的编辑器开启软键盘
     *
     * @param editText 给定的编辑器
     */
    public static void openSoftKeyboard(Context context, EditText editText) {
        editText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        setEditTextSelectionToEnd(editText);
    }


    /**
     * 关闭软键盘
     */
    public static void closeSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager
                = (InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        //如果软键盘已经开启
        if (inputMethodManager.isActive() && null != activity.getCurrentFocus()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * 切换软键盘的状态
     */
    public static void toggleSoftKeyboardState(Context context) {
        ((InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
                InputMethodManager.SHOW_IMPLICIT,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 判断隐藏软键盘是否弹出,弹出就隐藏
     *
     * @param activity
     * @return
     */
    public boolean keyBoxIsShow(Activity activity) {
        if (activity.getWindow().getAttributes().softInputMode ==
                WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
            //隐藏软键盘
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置输入框的光标到末尾
     *
     * @param editText
     */
    public static void setEditTextSelectionToEnd(EditText editText) {
        Editable editable = editText.getEditableText();
        Selection.setSelection(editable, editable.toString().length());
    }

}
