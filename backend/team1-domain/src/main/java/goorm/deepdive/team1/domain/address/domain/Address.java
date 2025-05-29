package goorm.deepdive.team1.domain.address.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import goorm.deepdive.team1.domain.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
@Table(name = "address", indexes = {
	@Index(name = "idx_region", columnList = "region")
})
public class Address extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Column(nullable = false)
	private double x;

	@Column(nullable = false)
	private double y;

	@Column(nullable = false, unique = true)
	private String regionAddress;

	@Column(nullable = false, unique = true)
	private String roadAddress;

	@Column(nullable = false)
	private String region;

	public static Address create(double x, double y, String regionAddress, String roadAddress, String region) {
		return Address.builder()
			.x(x)
			.y(y)
			.regionAddress(regionAddress)
			.roadAddress(roadAddress)
			.region(region)
			.build();
	}

	public void updateX(double x) {
		this.x = x;
	}

	public void updateY(double y) {
		this.y = y;
	}

	public void updateRegionAddress(String regionAddress) {
		this.regionAddress = regionAddress;
	}

	public void updateRoadAddress(String roadAddress) {
		this.roadAddress = roadAddress;
	}

	public void updateRegion(String region) {this.region = region;}
}
