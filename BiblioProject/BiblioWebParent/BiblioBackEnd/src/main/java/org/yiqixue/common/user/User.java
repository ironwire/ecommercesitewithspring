package org.yiqixue.common.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
import lombok.NonNull;
import lombok.ToString;

@Entity
@Table(name="users")
public class User implements UserDetails {
	

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
	@Column(name = "first_name", length = 45, nullable = false)
	private String firstName;
	
	@lombok.NonNull
	@Column(name = "last_name", length = 45, nullable = false)
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
	
	

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
 
    @Override
    public String getUsername() {
        return this.email;
    }
 
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
 
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
 
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
 
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
	
	
	
	
	public User() {}
	

	public User(@NonNull String email, @NonNull String password, @NonNull String firstName, @NonNull String lastName,
			boolean enabled) {
		super();
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.enabled = enabled;

	}

	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
//	@Transient
//	public String getPhotosImagePath() {
//		if(id == null || photos == null) return "/images/default-user.png";
//		return "/user-photos/"+this.id +"/"+this.photos;
//	}
//	
//	@Transient
//	public String getFullName() {
//		return firstName + " " +lastName;
//	}

}
