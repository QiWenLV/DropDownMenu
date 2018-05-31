package com.zqw.dropadownmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by 启文 on 2018/5/21.
 *
 * 自定义控件——仿美团下拉菜单
 */
public class DropDownMenu extends LinearLayout {

    //顶部菜单布局
    private LinearLayout tabMenuView;

    //底部容器 (包含内容区域，遮罩区域， 菜单弹出区域)
    private FrameLayout contaninerView;

    //zhi
    private TextView tBg;

    //遮罩区域
    private View maskView;

    //弹出区域
    private FrameLayout popupMenuView;


    private int dividerColor = 0xffcccccc;    //分割线颜色
    private int textSelectColor = 0xff890c85;    //文本选中的颜色
    private int textUnSelectColor = 0xff11111;  //文本未选中的颜色
    private int menuBackgroundColor = 0xffffffff;    //菜单背景颜色
    private int maskColor = 0x88888888;     //遮罩颜色
    private int underlineColor = 0xffcccccc; //水平分割线

    private int menuTextSize = 14;      //菜单文本的大小
    private int menuSelectedIcon;   //菜单项选中图标
    private int menuUnSelectedIcon; //菜单未项选中图标


    private int currentTabPosition = -1; //记录上一次菜单项的位置

    public DropDownMenu(Context context) {
        this(context, null);
    }

    public DropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);

        //读取自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.dropDownMenu);

        underlineColor = a.getColor(R.styleable.dropDownMenu_underlineColor, underlineColor);       //水平分割线
        dividerColor = a.getColor(R.styleable.dropDownMenu_dividerColor, dividerColor);             //分割线颜色
        textSelectColor = a.getColor(R.styleable.dropDownMenu_textSelectColor, textSelectColor);             //文本选中的颜色
        textUnSelectColor = a.getColor(R.styleable.dropDownMenu_textUnSelectColor, textUnSelectColor);             //文本未选中的颜色
        menuBackgroundColor = a.getColor(R.styleable.dropDownMenu_menuBackgroundColor, menuBackgroundColor);             //菜单背景颜色
        maskColor = a.getColor(R.styleable.dropDownMenu_maskColor, maskColor);

        menuTextSize = a.getDimensionPixelSize(R.styleable.dropDownMenu_menuTextSize, menuTextSize);             //菜单文本的大小
        menuSelectedIcon = a.getResourceId(R.styleable.dropDownMenu_menuSelectedIcon, menuSelectedIcon);             //菜单项选中图标
        menuUnSelectedIcon = a.getResourceId(R.styleable.dropDownMenu_menuUnSelectedIcon, menuUnSelectedIcon);             //菜单未项选中图标

        a.recycle();


        initViews(context);
    }

    private void initViews(Context context) {


        /**
         * 创建顶部菜单
         */
        tabMenuView = new LinearLayout(context);

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, WRAP_CONTENT);   //设置tabMenuView的大小
        tabMenuView.setOrientation(HORIZONTAL);
        tabMenuView.setLayoutParams(lp);

        addView(tabMenuView, 0);        //顶部菜单布局作为第一项被添加


        /**
         * 创建下划线
         */
        View underlineView = new View(context);
        underlineView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, Tools.dp2px(context, 1.0f)));
        underlineView.setBackgroundColor(underlineColor);       //设置下划线颜色
        addView(underlineView, 1);    //下划线作为第二项被添加

        /**
         * 创建内容区域
         */
        contaninerView = new FrameLayout(context);
        contaninerView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(contaninerView, 2);    //下划线作为第三项被添加



    }


    /**
     * 初始化DropDownMenu 现实具体的内容(给MainActivity调用)
     */

    public void setDropDownMenu(List<String> tabTexts, List<View> popuViews, TextView contentView){

        Log.i("TAG", "!"+tabTexts.size());
        if(tabTexts.size() != popuViews.size()){
            throw new IllegalArgumentException("错误");   //手动抛异常
        }

        for(int i = 0; i < tabTexts.size(); i++){

            addTab(tabTexts, i);
        }

        this.tBg = contentView; //内容区域
        contaninerView.addView(tBg, 0);

        /**
         * 创建灰色蒙版区域
         */
        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击蒙版区域会关闭菜单
                closeMenu();
            }
        });
        maskView.setVisibility(View.GONE);  //初始是隐藏的
        contaninerView.addView(maskView, 1);

        /**
         * 创建弹出区域
         */
        popupMenuView = new FrameLayout(getContext());
        popupMenuView.setVisibility(View.GONE);
        for(int i=0; i < popuViews.size(); i++){
            popuViews.get(i).setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            popupMenuView.addView(popuViews.get(i), i);
        }

        contaninerView.addView(popupMenuView, 2);
    }



    /**
     * 添加顶部菜单中的菜单项
     * @param tabTexts
     * @param index
     */
    private void addTab(List<String> tabTexts, int index){

//        final TextView tab = new TextView(getContext());
//        tab.setSingleLine();    //单行现实
//        tab.setEllipsize(TextUtils.TruncateAt.END);     //文字过长显示省略号
//        tab.setGravity(Gravity.CENTER);     //剧中显示
//        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
//
//        tab.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));    //均分
//        tab.setTextColor(textUnSelectColor);        //初始时都未被选中
//      //  tab.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuUnSelectedIcon), null);    //TextView右边的图片
//        tab.setText(text.get(index));
//        tab.setPadding(Tools.dp2px(getContext(), 5), Tools.dp2px(getContext(), 12), Tools.dp2px(getContext(), 5), Tools.dp2px(getContext(), 12) );
//        tab.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                switchMenu(tab);    //点击打开关闭下拉选项
//            }
//        });
//
//        if(tabMenuView != null){
//            tabMenuView.addView(tab);
//        }
        final TextView tab = new TextView(getContext());
        tab.setSingleLine();
        tab.setEllipsize(TextUtils.TruncateAt.END);
        tab.setGravity(Gravity.CENTER);
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
        tab.setLayoutParams(new LayoutParams(0, WRAP_CONTENT, 1));
        tab.setTextColor(textUnSelectColor);
        tab.setCompoundDrawablesWithIntrinsicBounds(null, null,
                getResources().getDrawable(menuUnSelectedIcon), null);
        tab.setText(tabTexts.get(index));
        tab.setPadding(Tools.dp2px(getContext(), 5), Tools.dp2px(getContext(), 12), Tools.dp2px(getContext(), 5), Tools.dp2px(getContext(), 12) );
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenu(tab);
            }
        });
        tabMenuView.addView(tab);


        //添加分割线
        if (index < tabTexts.size() - 1) {
            View view = new View(getContext());
            view.setLayoutParams(new LayoutParams(Tools.dp2px(getContext(), 0.5f), LayoutParams.MATCH_PARENT));
            view.setBackgroundColor(underlineColor);
            tabMenuView.addView(view);
        }
    }


    /**
     * 切换菜单(打开关闭菜单)
     * @param tab
     */
    private void switchMenu(View tab){

        for(int i=0; i< tabMenuView.getChildCount(); i = i+2){

            //找到点击项
            if(tab == tabMenuView.getChildAt(i)){

                if(currentTabPosition == i){
                    //关闭(再次点击)
                    closeMenu();
                }else {
                    popupMenuView.setVisibility(VISIBLE);
                    maskView.setVisibility(VISIBLE);
                    //打开
                    if (currentTabPosition == -1) {

                        popupMenuView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));  //动画
                        maskView.setVisibility(View.VISIBLE);
                        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));  //动画
                        popupMenuView.getChildAt(i / 2).setVisibility(VISIBLE);
                    } else {
                        popupMenuView.getChildAt(i / 2).setVisibility(VISIBLE);
                    }

                    currentTabPosition = i;
                    ((TextView) tabMenuView.getChildAt(i)).setTextColor(textSelectColor);
                    ((TextView) tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuSelectedIcon), null);
                }
            } else {
                ((TextView) tabMenuView.getChildAt(i)).setTextColor(textUnSelectColor);
                ((TextView) tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuUnSelectedIcon), null);
                popupMenuView.getChildAt(i / 2).setVisibility(GONE);
            }
        }
    }

    /**
     * 关闭菜单
     */
    private void closeMenu() {

        if(currentTabPosition != -1){

            popupMenuView.setVisibility(GONE);
            popupMenuView.getChildAt(currentTabPosition / 2).setVisibility(GONE);
            TextView tv = (TextView) tabMenuView.getChildAt(currentTabPosition);
            tv.setTextColor(textUnSelectColor);
            tv.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(menuUnSelectedIcon), null);
            maskView.setVisibility(GONE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));
            currentTabPosition = -1;

        }
    }

    public void setTabText(int i, String s) {
        TextView tv = (TextView) tabMenuView.getChildAt(i * 2);
        tv.setText(s);
    }

    public boolean isShowing() {
        return currentTabPosition >= 0;
    }
}


