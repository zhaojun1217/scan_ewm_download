package com.zhaoj.scan_ewm_download.custom_widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zhaoj.scan_ewm_download.R;
import com.zhaoj.scan_ewm_download.utils.ScreenUtil;
import com.zhaoj.scan_ewm_download.utils.SystemUtil;


/**
 * @version 1.0.0
 */

public class CommonToolBar extends RelativeLayout {

    private Context context;
    private View mView;
    private LayoutInflater mInflater;
    private TextView centerTitle;

    private RelativeLayout rlToolbarRoot, toolbarLeftbutton;
    private ImageView leftIcon;
    private TextView toolbarTitle;
    private String rightButtonContent;
    private boolean isShowRightButton;
    private int textTitleColor;
    private int rootViewBgId;
    private int leftIconId;
    private String titleContent;
    private String centerTitleText;
    private boolean showRightTwoIconBol;
    private boolean showSaveAndDelete;
    private boolean fixActionBar;
    private boolean showLeftIcon;

    public CommonToolBar(Context context) {
        super(context);
    }

    public CommonToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LetToolBar);
        leftIconId = ta.getResourceId(R.styleable.LetToolBar_leftButtonIcon, R.mipmap.icon_fanhui);
        textTitleColor = ta.getColor(R.styleable.LetToolBar_titleColor, context.getResources().getColor(R.color.white));
        rootViewBgId = ta.getResourceId(R.styleable.LetToolBar_rootViewBgColor, R.color.transparent);
        titleContent = ta.getString(R.styleable.LetToolBar_myTitleContent);
        showRightTwoIconBol = ta.getBoolean(R.styleable.LetToolBar_showRightTwoIcon, false);
        showSaveAndDelete = ta.getBoolean(R.styleable.LetToolBar_showSaveAndDelete, false);
        isShowRightButton = ta.getBoolean(R.styleable.LetToolBar_showRightSingleButton, false);
        fixActionBar = ta.getBoolean(R.styleable.LetToolBar_fixActionBar, false);
        showLeftIcon = ta.getBoolean(R.styleable.LetToolBar_showLeftIcon, true);
        centerTitleText = ta.getString(R.styleable.LetToolBar_centerTitleText);

        if (isShowRightButton) {
            rightButtonContent = ta.getString(R.styleable.LetToolBar_rightButtonText);
        }
        initView();
        ta.recycle();  //注意回收
    }

    public CommonToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        this.context = context;
    }

    private void initView() {
        //初始化
        mInflater = LayoutInflater.from(getContext());
        initTypeZeroView();
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addView(mView, lp);
    }

    private void initTypeZeroView() {
        //添加布局文件
        mView = mInflater.inflate(R.layout.toolbar_type_one, null);
        centerTitle = mView.findViewById(R.id.center_title);
        rlToolbarRoot = mView.findViewById(R.id.rl_toolbar_root);
        toolbarLeftbutton = mView.findViewById(R.id.toolbar_leftbutton);
        leftIcon = mView.findViewById(R.id.left_icon);
        toolbarTitle = mView.findViewById(R.id.toolbar_title);
        setViewContentZero();
    }


    private void setViewContentZero() {
        centerTitle.setText(centerTitleText);
        centerTitle.setTextColor(textTitleColor);
        ViewGroup.LayoutParams layoutParams1 = rlToolbarRoot.getLayoutParams();
        if (fixActionBar) {
            layoutParams1.height = ScreenUtil.getStatusBarHeight(getContext()) + SystemUtil.dp2px(50);
        } else {
            layoutParams1.height = SystemUtil.dp2px(50);
        }
        rlToolbarRoot.setLayoutParams(layoutParams1);
        rlToolbarRoot.setBackgroundResource(rootViewBgId);
        if (showLeftIcon) {
            leftIcon.setVisibility(VISIBLE);
            leftIcon.setImageResource(leftIconId);
        } else {
            leftIcon.setVisibility(GONE);
        }
        toolbarTitle.setTextColor(textTitleColor);
        toolbarTitle.setText(titleContent);
    }

}
