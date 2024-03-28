package org.yiqixue.admin.persistence.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.yiqixue.common.book.Book;
import org.yiqixue.common.book.BookImage;


@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
	@Query("SELECT b FROM Book b WHERE b.title LIKE %?1%")
	public Page<Book> listBooksByPage(String keyword, Pageable pageable);
	
}
