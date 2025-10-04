package com.myreflectionthoughts.auth.datamodel.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;


@Data
@Entity
@Table( name = "post_like",
        schema = "letschat",
        indexes = {
                @Index(name="idx_comment_id", columnList = "comment_id")
        }
)
public class CommentLike {
    @Id
    @UuidGenerator
    @Column(name="like_id")
    private String likeId;

    @Column(name="user_id")
    private String userId;

    @Column(name="comment_id")
    private String commentId;

    @Column(name = "liked_at")
    private String likedAt;
}
