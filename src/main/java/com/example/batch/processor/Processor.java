package com.example.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.batch.entity.User;

@Component
public class Processor implements ItemProcessor<User, User> {

	@Override
	public User process(User user) throws Exception {
		System.out.println("START processing");
		if (user.getPurchasedPackage().equalsIgnoreCase("master")) {
			return null;
		}
		return user;
	}

}
