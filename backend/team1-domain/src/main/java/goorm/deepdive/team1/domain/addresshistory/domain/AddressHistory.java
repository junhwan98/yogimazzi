package goorm.deepdive.team1.domain.addresshistory.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import goorm.deepdive.team1.domain.address.domain.Address;
import goorm.deepdive.team1.domain.common.BaseTimeEntity;
import goorm.deepdive.team1.domain.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Table(name = "address_history", indexes = {
	@Index(name = "idx_user_id", columnList = "user_id")
})
public class AddressHistory extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "address_id", nullable = false)
	private Address address;

	public static AddressHistory create(User user) {
		return AddressHistory.builder()
			.user(user)
			.address(user.getAddress())
			.build();
	}
}
