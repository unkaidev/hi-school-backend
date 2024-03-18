package phongvan.hischoolbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String other;
    private String wardCommune;
    private String district;
    private String province;
    private String city;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(other, address.other) && Objects.equals(wardCommune, address.wardCommune) && Objects.equals(district, address.district) && Objects.equals(province, address.province) && Objects.equals(city, address.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(other, wardCommune, district, province, city);
    }
}
