package com.news.yazhidao.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by fengjigang on 15/3/17.
 */
@DatabaseTable(tableName = "tb_news_root")
public class NewsRoot {
    public NewsRoot(){}
    public NewsRoot(String root_id,String root_name,String root_alias){
        this.root_id=root_id;
        this.root_name=root_name;
        this.root_alias=root_alias;
    }
    @DatabaseField(id = true)
    private String root_id;
    @DatabaseField
    private String root_name;
    @DatabaseField
    private String root_alias;

    public String getRoot_id() {
        return root_id;
    }

    public void setRoot_id(String root_id) {
        this.root_id = root_id;
    }

    public String getRoot_name() {
        return root_name;
    }

    public void setRoot_name(String root_name) {
        this.root_name = root_name;
    }

    public String getRoot_alias() {
        return root_alias;
    }

    public void setRoot_alias(String root_alias) {
        this.root_alias = root_alias;
    }
}
