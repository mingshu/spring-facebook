package org.springframework.social.showcase.service;

import org.springframework.social.showcase.document.Customer;
import org.springframework.social.showcase.exception.UsernameAlreadyInUseException;

public interface CustomerService {
	void register(Customer input) throws UsernameAlreadyInUseException;
	Customer find(String username);
}
