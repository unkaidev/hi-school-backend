package phongvan.hischoolbackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;


import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "_student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "_parent_id")
    @JsonManagedReference
    private Parent parent;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<Transcript> transcripts;

    @Lob
    @Column(name = "_avatar",columnDefinition = "LONGTEXT")
    private String avatar;

    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String nationality;
    private String ethnicity;
    private String citizenId;
    private String issuedDate;
    @ManyToOne
    @JsonManagedReference
    private IssuedPlace issuedPlace;
    @ManyToOne( cascade = CascadeType.ALL)
    @JsonManagedReference
    private Address permanentAddress;
    @ManyToOne( cascade = CascadeType.ALL)
    @JsonManagedReference
    private Address contactAddress;

    @ManyToMany
            (fetch = FetchType.LAZY,
                    cascade = {
                            CascadeType.PERSIST,
                            CascadeType.DETACH,
                            CascadeType.REFRESH,
                            CascadeType.MERGE})
    @JoinTable(
            name = "_class_student",
            joinColumns = @JoinColumn(name = "_student_id"),
            inverseJoinColumns = @JoinColumn(name = "_class_id")
    )
    @JsonIgnore
    private Collection<SchoolClass> classes;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @JsonBackReference
    private Collection<Score> scores;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
