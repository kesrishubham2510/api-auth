package com.myreflectionthoughts.auth.datamodel.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Data
@Entity
@Table(
        name = "discussion_groups",
        indexes = {
                @Index(name="idx_group_id", columnList = "group_id", unique = true)
        }
)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "groupId"
)
public class DiscussionGroup {

    @Id
    @UuidGenerator
    @Column(name = "group_id")
    private String groupId;

    @Column(name = "group_name")
    private String groupName;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_group_membership",
            joinColumns = @JoinColumn(name = "group_id_fk", referencedColumnName = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id_fk", referencedColumnName = "user_id")
    )

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy="discussionGroup")
    private List<Post> posts;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "description")
    private String description;
}
