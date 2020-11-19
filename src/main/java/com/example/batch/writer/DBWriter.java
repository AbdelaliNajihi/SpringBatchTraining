package com.example.batch.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.example.batch.dao.IUserRepository;
import com.example.batch.entity.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DBWriter implements ItemWriter<User> {
	private final IUserRepository userRepository;
	
	@Override
	public void write(List<? extends User> users) throws Exception {
		System.out.println("START writing into databse");
		userRepository.saveAll(users);
	}

}
