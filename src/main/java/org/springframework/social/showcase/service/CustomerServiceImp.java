package org.springframework.social.showcase.service;

import javax.inject.Inject;

import org.springframework.social.showcase.document.Customer;
import org.springframework.social.showcase.exception.UsernameAlreadyInUseException;
import org.springframework.social.showcase.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImp implements CustomerService {

	private CustomerRepository customerRepository;

	@Inject
	public CustomerServiceImp(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public void register(Customer input) throws UsernameAlreadyInUseException {

		Customer existedCustomer = customerRepository.findByUsername(input.getUsername());
		if (existedCustomer == null) {
			customerRepository.save(input);
			return;
		}

		throw new UsernameAlreadyInUseException(input.getUsername());
	}

	@Override
	public Customer find(String username) {
		return customerRepository.findByUsername(username);
	}

}
