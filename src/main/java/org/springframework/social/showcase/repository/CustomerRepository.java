package org.springframework.social.showcase.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.social.showcase.document.Customer;

public interface CustomerRepository extends CrudRepository<Customer, String>{
	Customer findByUsername(String username);
}
