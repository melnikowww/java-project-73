package hexlet.code.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.GenerationType;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


import java.sql.Timestamp;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1)
    private String firstName;

    @Size(min = 1)
    private String lastName;

    @Email
    @Column(unique = true)
    private String email;

    @Size(min = 3)
    @JsonIgnore
    private String password;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;

}
