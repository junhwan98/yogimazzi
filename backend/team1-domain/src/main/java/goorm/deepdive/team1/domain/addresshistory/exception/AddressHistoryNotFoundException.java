package goorm.deepdive.team1.domain.addresshistory.exception;

import static goorm.deepdive.team1.domain.addresshistory.exception.AddressHistoryDomainExceptionCode.ADDRESS_HISTORY_NOT_FOUND;

import goorm.deepdive.team1.common.exception.CustomException;

public class AddressHistoryNotFoundException extends CustomException {
	public AddressHistoryNotFoundException() {
		super(ADDRESS_HISTORY_NOT_FOUND);
	}
}
