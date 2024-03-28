package org.yiqixue.admin.persistence.store;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.yiqixue.common.store.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {
	public Store findByEmail(String email);

	public Store findByVerificationCode(String verificationCode);
	
	@Query("UPDATE Store s SET s.emailConfirmed = true, s.verificationCode = null WHERE s.id = ?1")
	@Modifying
	public void emailConfirm(Integer id);
	
	@Query("UPDATE Store s SET s.enabled = true WHERE s.id = ?1")
	@Modifying
	public void enable(Integer id);

}
