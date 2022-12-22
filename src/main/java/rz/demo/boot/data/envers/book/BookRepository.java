package rz.demo.boot.data.envers.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Rashidi Zin
 */
public interface BookRepository extends JpaRepository<Book, Long>, RevisionRepository<Book, Long, Integer> {

    Stream<Book> findAllByAuthor(String author);

    @Modifying
    @Query("delete from rz.demo.boot.data.envers.book.Book_AUD bookRev where bookRev.originalId.REV.id in (select book.originalId.REV.id from rz.demo.boot.data.envers.book.Book_AUD book where book.originalId.REV.timestamp < :deleteBefore)")
    @Transactional
    void deleteRevisions(Long deleteBefore);

    @Modifying
    @Query("select book.originalId.REV.timestamp from rz.demo.boot.data.envers.book.Book_AUD book")
    @Transactional
    List<Object> getRevisions();

}
