package com.shopme.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="roles")
@Data
@NoArgsConstructor
//@RequiredArgsConstructor
@ToString
public class Role {
	@ToString.Exclude
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ToString.Include
	@lombok.NonNull
	@Column(length = 40, nullable = false, unique=true)
	private String name;
	
	@ToString.Exclude
	@lombok.NonNull
	@Column(length = 150, nullable =false)
	private String description;

	public Role(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	@Override
	public String toString() {
		return "" + description + "";
	}
	
	
	
	
	
}
