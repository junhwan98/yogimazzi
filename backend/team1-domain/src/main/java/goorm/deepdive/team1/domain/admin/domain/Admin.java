package goorm.deepdive.team1.domain.admin.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Admin {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Enumerated(STRING)
	@Column(nullable = false)
	private Role role;

	public static Admin create(String email, String password, Role role) {
		return Admin.builder()
			.email(email)
			.password(password)
			.role(role)
			.build();
	}
	public void updatePassword(String password) {
		this.password = password;
	}

	public void updateRole(Role role) { this.role = role;}
}
