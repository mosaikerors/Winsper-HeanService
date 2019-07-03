package com.mosaiker.heanservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Picture {
    @Id
    private String pId;
    private Binary content; // 文件内容
    private String contentType; // 文件类型
    private long size; // 文件大小
}
