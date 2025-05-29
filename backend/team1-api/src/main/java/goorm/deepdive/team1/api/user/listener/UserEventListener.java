package goorm.deepdive.team1.api.user.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import goorm.deepdive.team1.domain.addresshistory.application.AddressHistoryCommandService;
import goorm.deepdive.team1.domain.user.event.UserCreatedEvent;
import goorm.deepdive.team1.domain.user.event.UserDeletedEvent;
import goorm.deepdive.team1.domain.user.event.UserUpdatedEvent;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserEventListener {
	private final AddressHistoryCommandService addressHistoryCommandService;

	@EventListener
	public void handleUserCreatedEvent(UserCreatedEvent event) {
		addressHistoryCommandService.create(event.user());
	}

	@EventListener
	public void handleUserUpdatedEvent(UserUpdatedEvent event) {
		addressHistoryCommandService.update(event.user());
	}

	@EventListener
	public void handleUserDeletedEvent(UserDeletedEvent event) {
		addressHistoryCommandService.cleanUpDeletedAddressHistories(event.userIds());
	}
}
