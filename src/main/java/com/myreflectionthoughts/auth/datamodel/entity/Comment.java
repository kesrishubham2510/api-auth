package com.myreflectionthoughts.auth.datamodel.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Data
@Entity
@Table(name = "comments", schema = "letschat")
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

    @OneToMany(cascade =  CascadeType.REFRESH)
    @JoinColumn(name = "comment_id_fk")
    private List<CommentLike> likes;
}
