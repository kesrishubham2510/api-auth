package com.myreflectionthoughts.auth.datamodel.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Data
@Entity
@Table(name = "posts", schema = "letschat",
    indexes = {
        @Index(name = "idx_group_post_id", columnList = "post_id")
    }
)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "postId"
)
public class Post {

    @Id
    @UuidGenerator
    @Column(name = "post_id")
    private String postId;

    @Column(name = "content")
    private String content;

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id_fk")
    private List<PostLike> likes;

    // I don't want to fetch these details un-necessarily
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id_fk")
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name="discussion_group_id_fk",  referencedColumnName = "group_id", nullable = false)
    private DiscussionGroup discussionGroup;

    @Column(name = "posted_at")
    private String postedAt;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id_fk", referencedColumnName = "user_id")
    private User user;
}
