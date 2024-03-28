package org.yiqixue.admin.persistence.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yiqixue.common.book.Book;
import org.yiqixue.common.book.BookImage;
import java.util.List;


@Repository
public interface BookImageRepository extends JpaRepository<BookImage, Integer> {
	
	List<BookImage> findByBook(Book book);
}
