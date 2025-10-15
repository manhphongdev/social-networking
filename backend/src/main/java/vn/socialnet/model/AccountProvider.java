package vn.socialnet.model;

import jakarta.persistence.*;
import lombok.*;
import vn.socialnet.enums.AuthProvider;

import java.io.Serializable;

@Entity
@Table(
        name = "account_provider",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "provider"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountProvider extends AbstractEntity implements Serializable {


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider; // LOCAL, GOOGLE, ...

    private String providerId; // sub from Google, null when LOCAL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}