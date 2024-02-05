package com.shopme.admin.service.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.country.persistence.CountryRepository;
import com.shopme.common.entity.Country;

@Service
public class CountryService {

	@Autowired private CountryRepository crepo;
	
	public List<Country> listAll(){
		List<Country> listCountries = crepo.findAllByOrderByNameAsc();
		return listCountries;
	}
}
