package com.myreflectionthoughts.auth.datamodel.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
@Table( name = "post_like",
        schema = "letschat",
        indexes = {
            @Index(name="idx_post_id", columnList = "postid")
        }
)
public class PostLike {

    @Id
    @UuidGenerator
    @Column(name="likeid")
    private String likeId;
    @Column(name="userid")
    private String userId;
    @Column(name="postid")
    private String postId;
    @Column(name = "likedat")
    private String likedAt;
}
