package com.myreflectionthoughts.auth.datamodel.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Data
@Entity
@Table(
        name = "discussion_groups",
        indexes = {
                @Index(name="idx_group_id", columnList = "group_id", unique = true),
        }
)
public class DiscussionGroup {

    @Id
    @UuidGenerator
    @Column(name = "group_id")
    private String groupId;

    @Column(name = "group_name")
    private String groupName;

//    @JoinColumn(name = "group_id_fk", referencedColumnName = "group_id")
//    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
//    private List<User> users;

//    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
//    private List<Post> posts;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "description")
    private String description;
}
