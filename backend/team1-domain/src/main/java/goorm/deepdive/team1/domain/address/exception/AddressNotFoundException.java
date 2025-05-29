package goorm.deepdive.team1.domain.address.exception;

import static goorm.deepdive.team1.domain.address.exception.AddressDomainExceptionCode.ADDRESS_NOT_FOUND;

import goorm.deepdive.team1.common.exception.CustomException;

public class AddressNotFoundException extends CustomException {
	public AddressNotFoundException() {
		super(ADDRESS_NOT_FOUND);
	}
}
