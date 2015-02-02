package com.news.yazhidao.utils.helper;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.news.yazhidao.constant.CommonConstant;
import com.news.yazhidao.constant.HttpConstant;
import com.news.yazhidao.entity.NewsFeed;
import com.news.yazhidao.entity.User;
import com.news.yazhidao.net.JsonCallback;
import com.news.yazhidao.net.MyAppException;
import com.news.yazhidao.net.NetworkRequest;
import com.news.yazhidao.utils.DeviceInfoUtil;
import com.news.yazhidao.utils.Logger;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.utils.OauthHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fengjigang on 15/2/1.
 */
public class UmengShareHelper {
    public static final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);
    private static final String TAG = "UmengShareHelper";
    private static Handler mHandler = new Handler();


    public static void oAuthSina(final Context mContext) {
            mController.getConfig().setSsoHandler(new SinaSsoHandler());
            mController.doOauthVerify(mContext, SHARE_MEDIA.SINA, new SocializeListeners.UMAuthListener() {
                @Override
                public void onStart(SHARE_MEDIA platform) {
                    Toast.makeText(mContext, "授权开始", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(SocializeException e, SHARE_MEDIA platform) {
                    Toast.makeText(mContext, "授权失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onComplete(Bundle value, SHARE_MEDIA platform) {
                    mController.getPlatformInfo(mContext, platform, new SocializeListeners.UMDataListener() {

                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onComplete(int i, Map<String, Object> value) {
                            HashMap<String, Object> params = new HashMap<String, Object>();
                            params.put("UUID", DeviceInfoUtil.getUUID(mContext));
                            params.put("sinaId", String.valueOf(value.get("uid")));
                            params.put("sinaToken", String.valueOf(value.get("access_token")));
                            params.put("sinaProfileImageUrl", value.get("profile_image_url"));
                            params.put("gender", value.get("gender"));
                            params.put("screenName", value.get("screen_name"));
                            Logger.i("sina auth", value.toString());
                            NetworkRequest request = new NetworkRequest(HttpConstant.URL_USER_LOGIN, NetworkRequest.RequestMethod.GET);
                            request.getParams = params;
                            request.setCallback(new JsonCallback<User>() {

                                @Override
                                public void success(User user) {
                                    //TODO 保存用户信息并修改用户登陆的头像信息等
                                    UserDataManager.saveUser(user);
                                }

                                @Override
                                public void failed(MyAppException exception) {
                                    Logger.d(TAG, "login failed " + exception.getMessage());
                                }
                            }.setReturnType(new TypeToken<User>() {
                            }.getType()));
                            Toast.makeText(mContext, value.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onCancel(SHARE_MEDIA platform) {
                    Toast.makeText(mContext, "授权取消", Toast.LENGTH_SHORT).show();
                }
            });

            mHandler.postDelayed(new Runnable() { //防止网络异常
                @Override
                public void run() {
                }
            }, 10000);
        }


    /**
     * 删除新浪微博授权
     *
     * @param mContext
     */
    public static void deleteShareSina(final Context mContext) {
        mController.deleteOauth(mContext, SHARE_MEDIA.SINA, new SocializeListeners.SocializeClientListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(int status, SocializeEntity arg1) {
                if (status == 200) {
                    Logger.d(TAG, "删除新浪授权成功");
                } else {
                    deleteShareSina(mContext); //如果失败继续删除
                    Logger.d(TAG, "删除新浪授权失败");
                }
            }
        });
    }

    /**
     * 分享到新浪微博  授权成功才可成功分享
     */
    public static void shareSina(final Context mContext, NewsFeed.Element news) {
        if (!isAuthenticated(mContext, SHARE_MEDIA.SINA)) {
            Toast.makeText(mContext, "新浪还未授权", Toast.LENGTH_SHORT).show();
            oAuthSina(mContext);
        } else {
            shareToPlatform(mContext, news, SHARE_MEDIA.SINA);
        }
    }

    private static void shareToPlatform(final Context mContext, NewsFeed.Element news, SHARE_MEDIA SM) {
        mController.setShareContent(news.sourceUrl);
        mController.postShare(mContext, SM, new SocializeListeners.SnsPostListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(SHARE_MEDIA arg0, int code, SocializeEntity arg2) {
                if (code == 200) {
                    Logger.d(TAG, "分享成功");
                } else {
                    String eMsg = "";
                    if (code == -101) {
                        eMsg = "没有授权";
                    }
                    Toast.makeText(mContext, "分享失败[" + code + "] " + eMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static boolean isAuthenticated(Context context, SHARE_MEDIA platform) {
        if (platform == SHARE_MEDIA.SINA) {
            String set = SettingHelper.get(CommonConstant.UserInfoConstant.SETTING_FILE, CommonConstant.UserInfoConstant.KEY_USER_ID);
            if (OauthHelper.isAuthenticated(context, SHARE_MEDIA.SINA) && set != null && !TextUtils.isEmpty(set)) {
                return true;
            } else {
                return false;
            }
        }
        if (platform == SHARE_MEDIA.RENREN) {
            if (OauthHelper.isAuthenticated(context, SHARE_MEDIA.RENREN)) {
                return true;
            } else {
                return false;
            }
        }
        if (platform == SHARE_MEDIA.WEIXIN) {
            return true;
        }
        if (platform == SHARE_MEDIA.WEIXIN_CIRCLE) {
            return true;
        }
        return false;
    }
}
