package com.myreflectionthoughts.auth.datamodel.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;


@Data
@Entity
@Table( name = "post_like",
        schema = "letschat",
        indexes = {
                @Index(name="idx_comment_id", columnList = "commentid")
        }
)
public class CommentLike {
    @Id
    @UuidGenerator
    @Column(name="likeid")
    private String likeId;

    @Column(name="userid")
    private String userId;

    @Column(name="commentid")
    private String commentId;

    @Column(name = "likedat")
    private String likedAt;
}
