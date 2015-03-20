package com.news.yazhidao.widget;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.news.yazhidao.R;
import com.news.yazhidao.utils.DeviceInfoUtil;
import com.news.yazhidao.utils.helper.UmengShareHelper;
import com.umeng.socialize.bean.SHARE_MEDIA;


/**
 * Created by h.yuan on 2015/3/18.
 * login
 */
public class LoginPopupWindow extends PopupWindow {
    private View mMenuView;
    private Context m_pContext;
    RelativeLayout m_pbtnLogin, m_pbtnWXLogin;
    private TextViewExtend m_pbtnTermOfService;
    ImageView m_pbtnCancel;
    private boolean m_pbIsHaveWeiXin;

    public LoginPopupWindow(Context context) {
        super(context);
        m_pContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.show_login_popup_window_layout, null);
        m_pbIsHaveWeiXin = DeviceInfoUtil.isHaveWeixin(m_pContext);
        findViews();
        setListener();
    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        super.setOnDismissListener(onDismissListener);
    }


    private void findViews() {
        m_pbtnLogin = (RelativeLayout) mMenuView.findViewById(R.id.btn_login);
        m_pbtnTermOfService = (TextViewExtend) mMenuView.findViewById(R.id.btn_term_of_service);
        m_pbtnCancel = (ImageView) mMenuView.findViewById(R.id.btn_cancel);
        m_pbtnWXLogin = (RelativeLayout) mMenuView.findViewById(R.id.btn_WXlogin);
        m_pbtnTermOfService.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationAlpha);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
//                int height = mMenuView.findViewById(R.id.login_bg).getTop();
//                int y = (int) event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < height) {
//                        dismiss();
//                    }
//                }
                return true;
            }
        });
    }

//        TSLoginPopupWindow m_pPopWindow = new TSLoginPopupWindow(BUBaseFragmentAcitvity.this);
//        m_pPopWindow.setAnimationStyle(R.style.AnimationAlpha);
//        m_pPopWindow.showAtLocation(getWindow().getDecorView(),  Gravity.CENTER
//                | Gravity.CENTER, 0, 0);


    private void setListener() {
        m_pbtnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UmengShareHelper.oAuthSina(m_pContext, null, SHARE_MEDIA.SINA);
                dismiss();
            }
        });
        m_pbtnWXLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_pbIsHaveWeiXin) {
                    UmengShareHelper.oAuthSina(m_pContext, null, SHARE_MEDIA.WEIXIN);
                } else {
                    Toast.makeText(m_pContext, "还没有安装微信", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });
        m_pbtnTermOfService.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        m_pbtnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
