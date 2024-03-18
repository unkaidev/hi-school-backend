package phongvan.hischoolbackend.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_teacher")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User user;
    @Lob
    @Column(name = "_avatar", columnDefinition = "LONGTEXT")
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
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    private Address permanentAddress;
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    private Address contactAddress;
    private String firstWorkDate;
    @Column(name = "_group")
    private String group;
    @OneToMany(mappedBy = "teacher",
            cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<TimeTableDetail> timeTableDetails;

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(id, teacher.id) && Objects.equals(firstName, teacher.firstName) && Objects.equals(lastName, teacher.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);
    }
}
