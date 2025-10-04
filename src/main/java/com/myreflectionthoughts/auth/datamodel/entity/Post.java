package com.myreflectionthoughts.auth.datamodel.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
@Table(name = "posts",
    indexes = {
        @Index(name = "idx_group_post_id", columnList = "postid")
    }
)
public class Post {

    @Id
    @UuidGenerator
    @Column(name = "post_id")
    private String postId;

    @Column(name = "content")
    private String content;

//    @JoinColumn(name="like", referencedColumnName = "userId")
//    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
//    private List<User> likes;
//
//    // I don't want to fetch these details un-necessarily
//    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
//    private List<Comment> comments;

    @Column(name = "posted_at")
    private String postedAt;

    @Column(name = "posted_by")
    private String postedBy;
}
