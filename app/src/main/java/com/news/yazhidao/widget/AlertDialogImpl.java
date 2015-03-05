package com.news.yazhidao.widget;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.news.yazhidao.R;


public class AlertDialogImpl extends DialogFragment{
	
	public static class Builder {
		
		private FragmentActivity mActivity;
		
		public Builder(FragmentActivity activity) {
			this.mActivity = activity;
		}
		
		public Builder setIcon(int iconId) {
			Parameters.icon = iconId;
			return this;
		}
		
		public Builder setTitle(String title) {
			Parameters.title = title;
			return this;
		}
		
		public Builder setMessage(String message) {
			Parameters.message = message;
			return this;
		}
		
		public Builder setView(View view) {
			Parameters.view = view;
			return this;
		}
		/* 注意此处监听为onclicklistener */
		public Builder setPositive(CharSequence chars, OnClickListener mpListener) {
			Parameters.possitiveChars = chars;
			Parameters.mpListener = mpListener;
			return this;
		}
		
		public Builder setNegative(CharSequence chars, OnClickListener mnListener) {
			Parameters.negativeChars = chars;
			Parameters.mnListener = mnListener;
			return this;
		}
		
		public AlertDialogImpl show() {
			AlertDialogImpl impl = new AlertDialogImpl();
			impl.show(mActivity.getSupportFragmentManager(), "DialogBuilder");
			return impl;
		}
		
		public static class Parameters {
			static String title = "";
			static int icon = -1;
			static String message = "";
			static View view;
			static CharSequence possitiveChars = "", negativeChars = "";
			static OnClickListener mpListener, mnListener;
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Log.e("tag", "onattach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.e("tag", "onCreate");
		setStyle(0, R.style.style_self_dialog_toast);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.e("tag", "onCreateview");
		
		View view = inflater.inflate(R.layout.common_custom_dialog, container, false);
		ImageView icon = (ImageView)view.findViewById(R.id.dialog_icon);
		if(Builder.Parameters.icon == -1) {
			icon.setVisibility(View.GONE);
		}else {
			icon.setBackgroundResource(Builder.Parameters.icon);
		}
		
		
		TextView title = (TextView)view.findViewById(R.id.dialog_title);
		int visibility = Builder.Parameters.title == null || Builder.Parameters.title.equals("") ? View.GONE : View.VISIBLE;
		LinearLayout titlLinear = (LinearLayout)view.findViewById(R.id.dialog_title_linear);
		titlLinear.setVisibility(visibility);
		CharSequence charSequence = Builder.Parameters.title == null ? "" : Builder.Parameters.title;
		title.setText(charSequence);
		
		TextView message = (TextView)view.findViewById(R.id.dialog_message);
		message.setText(Builder.Parameters.message);
		
		FrameLayout mView = (FrameLayout)view.findViewById(R.id.dialog_middle_view);
		if(Builder.Parameters.view == null) {
			mView.setVisibility(View.GONE);
		}else {
			mView.addView(Builder.Parameters.view);
		}
		
		Button btnPossitive = (Button)view.findViewById(R.id.dialog_sure);
		Button btnNegavite = (Button)view.findViewById(R.id.dialog_cancel);
			
		if(Builder.Parameters.mpListener == null) {
			btnPossitive.setVisibility(View.GONE);
		}else {
			btnPossitive.setText(Builder.Parameters.possitiveChars);
			btnPossitive.setOnClickListener(Builder.Parameters.mpListener);
		}
		if(Builder.Parameters.mnListener == null) {
			btnNegavite.setVisibility(View.GONE);
		}else {
			btnNegavite.setText(Builder.Parameters.negativeChars);
			btnNegavite.setOnClickListener(Builder.Parameters.mnListener);
		}
		if(Builder.Parameters.mpListener == null && Builder.Parameters.mnListener == null) {
			((LinearLayout)view.findViewById(R.id.dialog_linear_bottom)).setVisibility(View.GONE);
		}
		return view;
	}
}