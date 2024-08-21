package com.gtech.springboot_blog_rest_api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.yaml.snakeyaml.tokens.CommentToken;

import java.util.HashSet;
import java.util.Set;

//@Data     //  showed some error in  sec-10, 3rd part , as toString() is causing stackoverflowError... restricting Set<CommentDto> attribute to have its values from RDBMS
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "posts",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})}
)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(nullable = false, name = "content-value")
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

}
