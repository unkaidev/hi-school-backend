package phongvan.hischoolbackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String email;

    private String phone;

    private String password;

    private String gender;

    @ManyToMany
    @JsonManagedReference
    private Set<Role> roles = new HashSet<>();
//
//    @OneToOne
//    @JoinColumn(name = "_student_id")
//    private Student student;
//
//    @OneToOne
//    @JoinColumn(name = "_teacher_id")
//    private Teacher teacher;
//
//    @OneToOne
//    @JoinColumn(name = "_parent_id")
//    private Parent parent;

    private boolean active;
}
