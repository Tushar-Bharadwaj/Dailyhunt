package dailyhunt.internship.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dailyhunt.internship.entities.newscomponents.Genre;
import dailyhunt.internship.entities.newscomponents.Language;
import dailyhunt.internship.entities.newscomponents.Locality;
import dailyhunt.internship.entities.newscomponents.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    @Column(columnDefinition="TEXT")
    private String title;

    @Column(columnDefinition="TEXT")
    private String text;
    @Column(columnDefinition="TEXT")
    private String shortText;

    @ManyToMany
    @JoinTable(name = "news_tags",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<Tag> tags;

    @ManyToMany
    @JoinTable(name = "news_locality",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "locality_id"))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<Locality> localities;

    @ManyToMany
    @JoinTable(name = "news_genre",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<Genre> genres;

    @ManyToMany
    @JoinTable(name = "news_language",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id"))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<Language> language;
    private Date createdAt;
    private Date updatedAt;
    private Boolean approved;
    private Boolean published;

    private String imagePath;

    @ManyToOne
    private User approvedBy;

    private Boolean trending;
}
