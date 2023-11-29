package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ToString.Exclude
	private Integer id;
	
	@lombok.NonNull
	@Column(length = 128, nullable = false, unique = true)
	private String email;
	
	@ToString.Exclude
	@lombok.NonNull
	@Column(length = 64, nullable = false)
	private String password;
	
	@lombok.NonNull
	@Column(name = "firtName", length = 45, nullable = false)
	private String firstName;
	
	@lombok.NonNull
	@Column(name = "lastName", length = 45, nullable = false)
	private String lastName;
	
	@ToString.Exclude
	@Column(length = 64)
	private String photos;
	
	@ToString.Exclude
	private boolean enabled;
	
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name ="user_id"),
			inverseJoinColumns=@JoinColumn(name="role_id")
			)
	private Set<Role> roles = new HashSet<>();
	
	public void addRole(Role role) {
		this.roles.add(role);
	}
	
	@Transient
	public String getPhotosImagePath() {
		if(id == null || photos == null) return "/images/default-user.png";
		return "/user-photos/"+this.id +"/"+this.photos;
	}
	
	@Transient
	public String getFullName() {
		return firstName + " " +lastName;
	}

}
