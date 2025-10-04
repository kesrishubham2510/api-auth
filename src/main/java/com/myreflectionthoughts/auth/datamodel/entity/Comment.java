package com.myreflectionthoughts.auth.datamodel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Data
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @UuidGenerator
    @Column(name = "comment_id")
    private String commentId;

    @Column(name ="comment_text")
    private String commentText;

    @Column(name ="commented_by")
    private String commentedBy;

    @Column(name="commented_at")
    private String commentedAt;
}
