package com.news.yazhidao.utils.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.news.yazhidao.R;
import com.news.yazhidao.constant.CommonConstant;
import com.news.yazhidao.constant.HttpConstant;
import com.news.yazhidao.entity.NewsFeed;
import com.news.yazhidao.entity.User;
import com.news.yazhidao.net.MyAppException;
import com.news.yazhidao.net.NetworkRequest;
import com.news.yazhidao.net.UserLoginCallBack;
import com.news.yazhidao.pages.UserLoginAty;
import com.news.yazhidao.utils.DeviceInfoUtil;
import com.news.yazhidao.utils.Logger;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.RenrenShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.RenrenSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fengjigang on 15/2/1.
 */
public class UmengShareHelper {
    public static final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);
    private static final String TAG = "UmengShareHelper";
    private static Handler mHandler = new Handler();


    public static void oAuthSina(final Context mContext, final NewsFeed.Element news) {
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
            public void onComplete(Bundle value, final SHARE_MEDIA platform) {
                mController.getPlatformInfo(mContext, platform, new SocializeListeners.UMDataListener() {

                    @Override
                    public void onStart() {
                        System.out.println("aaaaa");
                    }

                    @Override
                    public void onComplete(int i, Map<String, Object> value) {
                        for(Map.Entry<String,Object> entry:value.entrySet()){
                        Logger.i("auth "+platform,"key:"+entry.getKey()+",value="+entry.getValue());
                        }
                        HashMap<String, Object> params = new HashMap<String, Object>();
                        params.put("uuid", DeviceInfoUtil.getUUID());
                        params.put("sinaId", String.valueOf(value.get("uid")));
                        params.put("sinaToken", String.valueOf(value.get("access_token")));
                        params.put("sinaProfileImageUrl", value.get("profile_image_url"));
                        params.put("gender", value.get("gender"));
                        params.put("screenName", value.get("screen_name"));
                        Logger.i("sina auth", value.toString());
                        NetworkRequest request = new NetworkRequest(HttpConstant.URL_USER_LOGIN, NetworkRequest.RequestMethod.GET);
                        request.getParams = params;
                        request.setCallback(new UserLoginCallBack<User>() {

                            @Override
                            public void success(User user) {

                                Logger.i(TAG, "login success " + user);
                                if (user != null) {
                                    Logger.i(TAG, "login success " + user.getSinaToken());
                                }
                                if (news != null) {
                                    shareToPlatform(mContext, news, SHARE_MEDIA.SINA);
                                } else {
                                    mContext.sendBroadcast(new Intent(UserLoginAty.ACTION_USER_LOGIN));

                                }
                                //TODO 保存用户信息并修改用户登陆的头像信息等
                                UserDataManager.saveUser(user);
                            }

                            @Override
                            public void failed(MyAppException exception) {
                                Logger.e(TAG, "login failed " + exception.getMessage());
                            }
                        }.setReturnType(new TypeToken<User>() {
                        }.getType()));
                        request.execute();
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
    public static void shareSina(Context mContext, NewsFeed.Element news) {
        if (!isAuthenticated(mContext, SHARE_MEDIA.SINA)) {
            Toast.makeText(mContext, "新浪还未授权", Toast.LENGTH_SHORT).show();
            oAuthSina(mContext, news);
        } else {
            shareToPlatform(mContext, news, SHARE_MEDIA.SINA);
        }
    }

    private static void shareToPlatform(final Context mContext, NewsFeed.Element news, SHARE_MEDIA SM) {
        mController.setShareContent(news.sourceUrl);
        if (!TextUtils.isEmpty(news.imgUrl)) {
            mController.setShareImage(new UMImage(mContext, news.imgUrl));
        }
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

    /**
     * 所有分享平台的入口
     * @param mActivity
     * @param news
     */
    public static void share(Activity mActivity,NewsFeed.Element news){
        configPlatforms(mActivity,news);
        setShareContent(mActivity,news);
        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
               SHARE_MEDIA.SINA,SHARE_MEDIA.TENCENT, SHARE_MEDIA.DOUBAN,
                SHARE_MEDIA.RENREN);
        mController.openShare(mActivity, false);
    }
    /**
     * 配置分享平台参数</br>
     */
    private static void configPlatforms(Activity mActivity,NewsFeed.Element news) {
        // 添加新浪SSO授权
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        // 添加腾讯微博SSO授权
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
        // 添加人人网SSO授权
        RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(mActivity,
                "475057", "3bcdcc9fd39243caa4a383fbcd52386a",
                "123b54fad3ce42ba816e5686f01bb62a");

        mController.getConfig().setSsoHandler(renrenSsoHandler);
        // 添加QQ、QZone平台
        addQQQZonePlatform(mActivity, news);

        // 添加微信、微信朋友圈平台
        addWXPlatform(mActivity);
    }
    /**
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     * @return
     */
    private static void addQQQZonePlatform(Activity mActivity,NewsFeed.Element news) {
        String appId = "100424468";
        String appKey = "c7394704798a158208a74ab60104f0ba";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mActivity,
                appId, appKey);
        qqSsoHandler.setTargetUrl(news.sourceUrl);
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mActivity, appId, appKey);
        qZoneSsoHandler.addToSocialSDK();
    }
    /**
     * @功能描述 : 添加微信平台分享
     * @return
     */
    private static void addWXPlatform(Activity mActivity) {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = "wxa61ff537677f11ee";
        String appSecret = "2bdc5a8f807ecacc5a5f4250a68b02e6";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(mActivity, appId, appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(mActivity, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }
    /**
     * 根据不同的平台设置不同的分享内容</br>
     */
    private static void setShareContent(Activity mActivity,NewsFeed.Element news) {
        String appName=mActivity.getResources().getString(R.string.app_name);
        // 配置SSO
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mActivity,
                "100424468", "c7394704798a158208a74ab60104f0ba");
        qZoneSsoHandler.addToSocialSDK();
        mController.setShareContent(news.title+"。"+news.sourceUrl);

        // APP ID：201874, API
        // * KEY：28401c0964f04a72a14c812d6132fcef, Secret
        // * Key：3bf66e42db1e4fa9829b955cc300b737.
        RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(mActivity,
                "201874", "28401c0964f04a72a14c812d6132fcef",
                "3bf66e42db1e4fa9829b955cc300b737");
        mController.getConfig().setSsoHandler(renrenSsoHandler);

        UMImage urlImage =null;
        if(!TextUtils.isEmpty(news.imgUrl)){
            urlImage=new UMImage(mActivity,
                news.imgUrl);

        }
        //微信分享
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(news.title+"。"+news.sourceUrl);
        weixinContent.setTitle(mActivity.getResources().getString(R.string.app_name)+"分享-微信");
        weixinContent.setTargetUrl(news.sourceUrl);
        weixinContent.setShareMedia(urlImage);
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(news.title+"。"+news.sourceUrl);
        circleMedia.setTitle(appName+"分享-朋友圈");
        if(urlImage!=null){
        circleMedia.setShareMedia(urlImage);
        }
        circleMedia.setTargetUrl(news.sourceUrl);
        mController.setShareMedia(circleMedia);

        // 设置renren分享内容
        RenrenShareContent renrenShareContent = new RenrenShareContent();
        renrenShareContent.setShareContent(news.title+"。"+news.sourceUrl);
        if(urlImage!=null){
        renrenShareContent.setShareImage(urlImage);
        }
        //人人网分享时，如果不设置website，点击¨应用名称¨或者¨图片¨将跳转到人人网主页
        renrenShareContent.setAppWebSite(news.sourceUrl);
        mController.setShareMedia(renrenShareContent);


        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(news.title+"。"+news.sourceUrl);
        qzone.setTargetUrl(news.sourceUrl);
        qzone.setTitle(appName+"分享-QZone");
        if(urlImage!=null){
        qzone.setShareMedia(urlImage);
        }
        mController.setShareMedia(qzone);

        //QQ分享
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(news.title+"。"+news.sourceUrl);
        qqShareContent.setTitle(appName+"分享-QQ");
        if(urlImage!=null){
        qqShareContent.setShareMedia(urlImage);
        }
        qqShareContent.setTargetUrl(news.sourceUrl);
        mController.setShareMedia(qqShareContent);

        //腾讯微博
        TencentWbShareContent tencent = new TencentWbShareContent();
        tencent.setShareContent(news.title+"。"+news.sourceUrl);
        tencent.setTitle(appName + "分享-QQ微博");
        if(urlImage!=null){
        tencent.setShareImage(urlImage);
        }
        // 设置tencent分享内容
        mController.setShareMedia(tencent);

        //新浪微博
        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setShareContent(news.title+"。"+news.sourceUrl);
        sinaContent.setTargetUrl(news.sourceUrl);
        sinaContent.setTitle(appName+"分享-新浪微博");
        if(urlImage!=null){
            sinaContent.setShareImage(urlImage);
        }
        mController.setShareMedia(sinaContent);



    }
}
