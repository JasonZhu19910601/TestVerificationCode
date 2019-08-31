package com.example.testverificationcode;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 输入验证码的自定义view
 * @anthor zhujiang
 * @time 2019/7/16 8:52
 */
public class VerificationCodeInput extends LinearLayout implements TextWatcher {
    private static final String TAG = "VerificationCodeInput";
    /**
     * 数字输入类型
     */
    private final static String TYPE_NUMBER = "number";
    /**
     * 普通文本输入类型
     */
    private final static String TYPE_TEXT = "text";
    /**
     * 密码输入类型
     */
    private final static String TYPE_PASSWORD = "password";
    /**
     * 电话号码输入类型
     */
    private final static String TYPE_PHONE = "phone";
    /**
     * EditText数量
     */
    private int box = 6;
    /**
     * EditText宽度
     */
    private int boxWidth = 80;
    /**
     * EditText高度
     */
    private int boxHeight = 80;
    /**
     * EditText水平方向的padding
     */
    private int childHPadding;
    /**
     * EditText竖直方向的padding
     */
    private int childVPadding;
    /**
     * 输入类型
     */
    private String inputType;
    /**
     * EditText获取焦点时的背景
     */
    private Drawable boxBgFocus;
    /**
     * EditText失去焦点时的背景
     */
    private Drawable boxBgNormal;
    /**
     * 验证码输入错误时EditText的背景
     */
    private Drawable boxBgError;
    /**
     * 验证码输入正确时EditText的背景
     */
    private Drawable boxBgSuccess;
    /**
     * 验证码字体颜色
     */
    private int mTextColor;
    /**
     * 监听验证码输入完成的监听器
     */
    private Listener listener;
    private boolean focus = false;
    /**
     * EditText 集合
     */
    private List<EditText> mEditTextList = new ArrayList<>();
    /**
     * 当前获取焦点的EditText的索引
     */
    private int currentPosition = 0;
    /**
     * 拼接当前输入的验证码内容的 StringBuilder
     */
    private StringBuilder mContentSB;

    public VerificationCodeInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.vericationCodeInput);
        box = a.getInt(R.styleable.vericationCodeInput_box, 6);
        childHPadding = (int) a.getDimension(R.styleable.vericationCodeInput_child_h_padding, 0);
        childVPadding = (int) a.getDimension(R.styleable.vericationCodeInput_child_v_padding, 0);
        boxBgFocus = a.getDrawable(R.styleable.vericationCodeInput_box_bg_focus);
        boxBgNormal = a.getDrawable(R.styleable.vericationCodeInput_box_bg_normal);
        boxBgError = a.getDrawable(R.styleable.vericationCodeInput_box_bg_error);
        boxBgSuccess = a.getDrawable(R.styleable.vericationCodeInput_box_bg_success);
        mTextColor = a.getColor(R.styleable.vericationCodeInput_code_text_color, Color.BLACK);
        inputType = a.getString(R.styleable.vericationCodeInput_inputType);
        boxWidth = (int) a.getDimension(R.styleable.vericationCodeInput_child_width, boxWidth);
        boxHeight = (int) a.getDimension(R.styleable.vericationCodeInput_child_height, boxHeight);
        a.recycle();
        initViews();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void initViews() {
        for (int i = 0; i < box; i++) {
            // 加载Layout中设置好样式的EditText
            EditText editText = (EditText) View.inflate(getContext(), R.layout.layout_verification_code_input_edittext, null);
            // 设置布局参数
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(boxWidth, boxHeight);
            layoutParams.bottomMargin = childVPadding;
            layoutParams.topMargin = childVPadding;
            layoutParams.leftMargin = childHPadding;
            layoutParams.rightMargin = childHPadding;
            layoutParams.gravity = Gravity.CENTER;
            // 验证码输入框弹出时默认首位获取焦点
            if (i == 0) {
                setBg(editText, true);
                editText.requestFocus();
            } else {
                setBg(editText, false);
            }
            editText.setTextColor(mTextColor);
            editText.setLayoutParams(layoutParams);
            editText.setGravity(Gravity.CENTER);
            editText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});

            // 设置输入类型
            if (TYPE_NUMBER.equals(inputType)) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (TYPE_PASSWORD.equals(inputType)) {
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else if (TYPE_TEXT.equals(inputType)) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            } else if (TYPE_PHONE.equals(inputType)) {
                editText.setInputType(InputType.TYPE_CLASS_PHONE);

            }
            editText.setId(i);
            editText.setEms(1);
            editText.addTextChangedListener(this);
            // 禁止复制粘贴
            TextWatcherUtil.setDisenableCopyPaster(editText);
            addView(editText, i);
            // 假如挂管理集合
            mEditTextList.add(editText);
        }
    }

    /**
     * 点击删除按钮式移动光标并删除值
     */
    private void backFocusAndDelete() {
        int count = getChildCount();
        EditText editText;
        // 当EditText为Error背景时，需要恢复当前已有内容的EditText的背景为Focus
        for (int i = 0; i < count; i++) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().length() == 1) {
                setBg(mEditTextList.get(i), true);
            } else {
                break;
            }
        }
        // 倒序将内容别删除的EditText的背景设置为normal背景
        for (int i = count - 1; i >= 0; i--) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().length() == 1) {
                editText.requestFocus();
                if (i < count - 1) {
                    setBg(mEditTextList.get(i + 1), false);
                }
                editText.setSelection(1);
                editText.getText().clear();
                currentPosition = i;
                return;
            }
        }
    }

    /**
     * 聚焦当前第一个无内容的EditText
     */
    private void focus() {
        int count = getChildCount();
        EditText editText;
        for (int i = 0; i < count; i++) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().length() < 1) {
                editText.requestFocus();
                return;
            }
        }
    }

    /**
     * 为 EditText 设置背景
     *
     * @param editText 需要设置背景的 EditText
     * @param focus    是否设置为focus时的背景
     */
    private void setBg(EditText editText, boolean focus) {
        if (editText != null) {
            editText.setBackground(focus ? boxBgFocus : boxBgNormal);
            if (focus) {
                // 聚焦时同时设置为Enabled
                editText.setEnabled(true);
            }
        }
    }

    /**
     * 检测当前已输入的验证码内容，当位数达到要求后调用监听提交验证码内容
     */
    private void checkAndCommit() {
        mContentSB = new StringBuilder();
        boolean full = true;
        for (int i = 0; i < box; i++) {
            EditText editText = (EditText) getChildAt(i);
            String content = editText.getText().toString();
            if (content.length() == 0) {
                full = false;
                break;
            } else {
                mContentSB.append(content);
            }
        }
        if (full) {
            if (listener != null) {
                listener.onComplete(mContentSB.toString());
                setEnabled(false);
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.setEnabled(enabled);
        }
    }

    /**
     * 设置验证码输入完成监听器
     *
     * @param listener 传入的监听器
     */
    public void setOnCompleteListener(Listener listener) {
        this.listener = listener;
    }

    @Override

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LinearLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取EditText的数量
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        if (count > 0) {
            View child = getChildAt(0);
            int cHeight = child.getMeasuredHeight();
            int cWidth = child.getMeasuredWidth();
            int maxH = cHeight + 2 * childVPadding;
            // 横向向右排列，最后一个ET不设置RightPadding，所以需要 - childHPadding
            int maxW = (cWidth + childHPadding) * box - childHPadding;
            setMeasuredDimension(resolveSize(maxW, widthMeasureSpec),
                    resolveSize(maxH, heightMeasureSpec));
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.setVisibility(View.VISIBLE);
            int cWidth = child.getMeasuredWidth();
            int cHeight = child.getMeasuredHeight();
            int cl = (i) * (cWidth + childHPadding);
            int cr = cl + cWidth;
            int ct = childVPadding;
            int cb = ct + cHeight;
            child.layout(cl, ct, cr, cb);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            focus();
            checkAndCommit();
        }
    }

    /**
     * 设置当前验证码内容
     *
     * @param content 需要显示到控件上的文本
     */
    public void setText(String content) {
        EditText editText;
        if (!TextUtils.isEmpty(content)) {
            for (int i = 0; i < box; i++) {
                editText = (EditText) getChildAt(i);
                if (i <= content.length() - 1) {
                    String s = Character.toString(content.charAt(i));
                    editText.setText(s);
                    editText.setSelection(1);
                    currentPosition = i;
                    // ET中有内容后，设置后一个et为Focus的下划线
                    if (i < box - 1) {
                        EditText nextEt = (EditText) getChildAt(i + 1);
                        setBg(nextEt, true);
                        nextEt.requestFocus();
                    }
                }
            }
        } else {
            for (int i = 0; i < box; i++) {
                editText = (EditText) getChildAt(i);
                // 输入内容为空时，默认选中首个ET
                if (i == 0) {
                    setBg(editText, true);
                    editText.requestFocus();
                } else {
                    setBg(editText, false);
                }
                editText.getText().clear();
            }
            currentPosition = 0;
        }
    }

    /**
     * 显示验证码错误的下划线
     */
    public void setErrorBg() {
        for (EditText editText : mEditTextList) {
            editText.setBackground(boxBgError);
        }
    }

    /**
     * 显示验证码正确的下划线
     */
    public void setSuccessBg() {
        for (EditText editText : mEditTextList) {
            editText.setBackground(boxBgSuccess);
        }
    }

    /**
     * 获取当前文本
     *
     * @return
     */
    public String getText() {
        return mContentSB.toString();
    }

    public void setCursorVisible(boolean isCursorVisible) {
        if (!isCursorVisible) {
            for (EditText editText : mEditTextList) {
                editText.setOnTouchListener(null);
                editText.setOnFocusChangeListener(null);
            }
        }
    }

    /**
     * 当按下自定义键盘删除键时
     */
    public void delete() {
        backFocusAndDelete();
    }

    /**
     * 验证码输入完成监听器
     */
    public interface Listener {
        /**
         * 当验证码输入位数达到要求后调用
         *
         * @param content 当前已输入的验证码内容
         */
        void onComplete(String content);
    }
}