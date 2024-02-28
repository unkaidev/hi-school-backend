package phongvan.hischoolbackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

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

    private String firstname;

    private String lastname;

    @OneToOne
    private User user;

    private String citizenId;
    private Date issuedDate;
    @OneToOne
    private IssuedPlace issuedPlace;
    @ManyToOne
    @JsonManagedReference
    private Address permanentAddress;
    @ManyToOne
    @JsonManagedReference
    private Address contactAddress;
}
