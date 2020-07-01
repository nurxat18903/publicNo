package com.project.publicNo.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@ToString
@Table(name = "t_article")
public class Article {
    @Id
    @Column(name = "article_id")
    private Integer articleId;
    @Column(name = "title")
    private String title;
    @Column(name = "article_link")
    private String articleLink;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "isTop")
    private Integer isTop;
    @Column(name = "delete_flg")
    //1-删除 0-未删除
    private Integer deleteFlg;
     @Column(name = "article_status")
    //1-已上架 0-已下架
    private Integer articleStatus;

}
