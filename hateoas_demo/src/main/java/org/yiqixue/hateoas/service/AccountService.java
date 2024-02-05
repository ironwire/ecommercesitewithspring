package org.yiqixue.hateoas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yiqixue.hateoas.AccountRepository;
import org.yiqixue.hateoas.entity.Account;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AccountService {
	
	@Autowired
	private AccountRepository repo;
	
	public List<Account> listAll(){
		return repo.findAll();
	}
	
	public Account get(Integer id) {
		return repo.findById(id).get();
	}
	
	public Account save(Account account) {
		return repo.save(account);
	}
	
	public Account deposit(float amount, Integer id) {
		repo.depoist(amount, id);
		return repo.findById(id).get();
	}
	
	public Account withdraw(float amount, Integer id) {
		repo.withdraw(amount, id);
		return repo.findById(id).get();
	}
	
	public void delete(Integer id) {
		repo.deleteById(id);
	}
	
	
}













