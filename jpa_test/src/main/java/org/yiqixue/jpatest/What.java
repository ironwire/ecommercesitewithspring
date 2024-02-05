package org.yiqixue.jpatest;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tbl_what")
public class What {
	
	private enum Gender{MALE, FEMALE};
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String name;
	
	private Gender gender;
	
	@Column(name = "tbl_description", length = 128, unique = false, nullable = true)
	private String description;

}
