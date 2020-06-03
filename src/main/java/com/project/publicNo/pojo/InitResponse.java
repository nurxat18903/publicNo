package com.project.publicNo.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;

@Data
@ToString
public class InitResponse extends Response{
   private Integer readPeas;
   private Integer articleCount;
   private Integer waitReadCount;
   private ArrayList<RankData> rankList;
}