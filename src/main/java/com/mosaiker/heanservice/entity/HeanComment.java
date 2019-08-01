package com.mosaiker.heanservice.entity;

import java.util.ArrayList;
import javax.persistence.ElementCollection;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.List;

@Document(collection = "comment")
public class HeanComment {

  @Id
  private String CommmentId;
  private String hId;
  private Long uId;
  private String content;
  private Long commentTime;
  private String targetCommentId;


  @ElementCollection
  private List<String> replies;


  public HeanComment() {
    this.targetCommentId = null;
    this.replies = new ArrayList<>();
  }

  public HeanComment(String hId, Long uId, String content, Long commentTime,
      String targetCommentId, List<String> replies) {
    this.hId = hId;
    this.uId = uId;
    this.content = content;
    this.commentTime = commentTime;
    this.targetCommentId = targetCommentId;
    this.replies = replies;
  }

  public String getCommmentId() {
    return CommmentId;
  }

  public void setCommmentId(String commmentId) {
    CommmentId = commmentId;
  }

  public Long getuId() {
    return uId;
  }

  public void setuId(Long uId) {
    this.uId = uId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String text) {
    this.content = text;
  }

  public Long getCommentTime() {
    return commentTime;
  }

  public void setCommentTime(Long commentTime) {
    this.commentTime = commentTime;
  }

  public List<String> getReplies() {
    return replies;
  }

  public void setReplies(List<String> replies) {
    this.replies = replies;
  }

  public String getHId() {
    return hId;
  }

  public void setHId(String hId) {
    this.hId = hId;
  }

  public String getTargetCommentId() {
    return targetCommentId;
  }

  public void setTargetCommentId(String targetCommentId) {
    this.targetCommentId = targetCommentId;
  }

  public void addReply(String commmentId) {
    this.replies.add(commmentId);
  }

}
