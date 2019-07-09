package com.mosaiker.heanservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "comment")
public class HeanComment {
    @Id
    private String hId;

    @Field(value = "comments")
    private List<Comment> replies;

    public String getHId() {
        return hId;
    }
    public void setHId(String hId) {
        this.hId = hId;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

}
