package phongvan.hischoolbackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Blob;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "_parent_id")
    @JsonManagedReference
    private Parent parent;

    @ManyToOne
    @JoinColumn(name = "_school_id")
    @JsonManagedReference
    private School school;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonBackReference
    private Collection<Transcript> transcripts;

    @Lob
    @Column(name = "_avatar")
    private Blob avatar;

    private String firstname;
    private String lastname;
    private Date dateOfBirth;
    private String nationality;
    private String ethnicity;
    private String citizenId;
    private Date issuedDate;
    private String issuedPlace;
    private String permanentAddress;
    private String contactAddress;

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
    private Collection<SchoolClass> classes;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private Collection<Score> scores;


}
