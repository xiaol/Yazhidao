package com.news.yazhidao.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.news.yazhidao.entity.NewsChannel;
import com.news.yazhidao.entity.NewsElement;
import com.news.yazhidao.entity.NewsRoot;
import com.news.yazhidao.utils.Logger;

import java.sql.SQLException;

/**
 * Created by fengjigang on 15/3/13.
 */
public class NewsSqliteHelperOrmlite extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME="yazhidao_news.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "NewsSqliteOpenHelper";

    public NewsSqliteHelperOrmlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, NewsElement.class);
            TableUtils.createTable(connectionSource, NewsChannel.class);
            TableUtils.createTable(connectionSource, NewsRoot.class);
            Logger.e(TAG, "------create database success");
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.e(TAG,"------create database fail");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource,NewsElement.class,true);
            onCreate(database,connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.e(TAG,"onUpgrade "+e.getMessage());
        }
    }

}
