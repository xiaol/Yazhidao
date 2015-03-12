package com.news.yazhidao.utils.helper;


import android.text.TextUtils;

import com.news.yazhidao.entity.User;

import static com.news.yazhidao.constant.CommonConstant.UserInfoConstant;

public class UserDataHelper {
	
	public static User getUser(){
		return readUser();
	}
	
	public static synchronized void saveUser(User user){
		String userJsonString = user.toJsonString();
		logout();
		SettingHelper.save(UserInfoConstant.SETTING_FILE, UserInfoConstant.KEY_USER_ID, user.getUuid());
		SettingHelper.save(UserInfoConstant.SETTING_FILE, UserInfoConstant.KEY_USER_INFO, userJsonString);
	}
	
	public static synchronized User readUser(){
		User user = null;
		try {
			String userJsonString = SettingHelper.get(UserInfoConstant.SETTING_FILE, UserInfoConstant.KEY_USER_INFO);
            if (TextUtils.isEmpty(userJsonString)) {
                return new User();
            }
			user = User.parseUser(userJsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public static synchronized void logout(){
		SettingHelper.remove(UserInfoConstant.SETTING_FILE, UserInfoConstant.KEY_USER_ID, UserInfoConstant.KEY_USER_INFO);
	}
	
}
