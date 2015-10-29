/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.showcase.signup;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.showcase.document.Customer;
import org.springframework.social.showcase.exception.UsernameAlreadyInUseException;
import org.springframework.social.showcase.message.Message;
import org.springframework.social.showcase.message.MessageType;
import org.springframework.social.showcase.service.CustomerService;
import org.springframework.social.showcase.signin.SignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

@Controller
public class SignupController {

	private final ProviderSignInUtils providerSignInUtils;
	private final CustomerService customerService;

	@Inject
	public SignupController(CustomerService customerService, ConnectionFactoryLocator connectionFactoryLocator,
			UsersConnectionRepository connectionRepository) {
		this.customerService = customerService;
		this.providerSignInUtils = new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public SignupForm signupForm(WebRequest request) {
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
		if (connection != null) {
			request.setAttribute("message",
					new Message(MessageType.INFO,
							"Your " + StringUtils.capitalize(connection.getKey().getProviderId())
									+ " account is not associated with a Spring Social Showcase account. If you're new, please sign up."),
					WebRequest.SCOPE_REQUEST);
			return SignupForm.fromProviderUser(connection.fetchUserProfile());
		} else {
			return new SignupForm();
		}
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(@Valid SignupForm form, BindingResult formBinding, WebRequest request) {
		if (formBinding.hasErrors()) {
			return null;
		}
		Customer customer = createAccount(form, formBinding);
		if (customer != null) {
			SignInUtils.signin(customer.getUsername());
			providerSignInUtils.doPostSignUp(customer.getUsername(), request);
			return "redirect:/";
		}
		return null;
	}

	// internal helpers

	private Customer createAccount(SignupForm form, BindingResult formBinding) {
		try {
			Customer customer = new Customer();
			customer.setUsername(form.getUsername());
			customer.setLastName(form.getLastName());
			customer.setPassword(form.getPassword());
			customer.setFirstName(form.getFirstName());
			customerService.register(customer);
			return customer;
		} catch (UsernameAlreadyInUseException e) {
			formBinding.rejectValue("username", "user.duplicateUsername", "already in use");
			return null;
		}
	}

}
