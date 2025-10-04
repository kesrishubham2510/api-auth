package com.myreflectionthoughts.auth.datamodel.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
@Table( name = "post_like",
        schema = "letschat",
        indexes = {
            @Index(name="idx_post_id", columnList = "post_id")
        }
)
public class PostLike {

    @Id
    @UuidGenerator
    @Column(name="like_id")
    private String likeId;
    @Column(name="user_id")
    private String userId;
    @Column(name="post_id")
    private String postId;
    @Column(name = "liked_at")
    private String likedAt;
}
