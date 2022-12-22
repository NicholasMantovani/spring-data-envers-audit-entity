package rz.demo.boot.data.envers.book;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.envers.repository.support.DefaultRevisionMetadata;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import rz.demo.boot.data.envers.audit.AuditRevisionEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rashidi Zin
 */
@SpringBootTest
class BookRepositoryRevisionsTest {

    @Autowired
    private BookRepository repository;

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;


    private Book book;

    private final String author = "Rudyard Kipling";

    @BeforeEach
    public void save() {
        repository.deleteAll();

        book = repository.save(
                Book.builder().author(author).title("Jungle Book").build()
        );
        repository.save(book);
    }

    @Test
    void initialRevision() {
        Revisions<Integer, Book> revisions = repository.findRevisions(book.getId());

        assertThat(revisions)
                .isNotEmpty()
                .allSatisfy(revision -> assertThat(revision.getEntity())
                        .extracting(Book::getId, Book::getAuthor, Book::getTitle)
                        .containsExactly(book.getId(), book.getAuthor(), book.getTitle())
                )
                .allSatisfy(revision -> {
                            DefaultRevisionMetadata metadata = (DefaultRevisionMetadata) revision.getMetadata();
                            AuditRevisionEntity revisionEntity = metadata.getDelegate();

                            assertThat(revisionEntity.getUsername()).isEqualTo("wade.wilson");
                        }
                );
    }

    @Test
//    @Transactional(propagation = Propagation.NEVER)
    void updateIncreasesRevisionNumber() {
        book.setTitle("If");
        repository.save(book);

//        entityManager.getTransaction().begin();
        repository.findRevisions(book.getId());

        Revisions<Integer, Book> revision = repository.findRevisions(book.getId());

        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        String userModified = ((AuditRevisionEntity) revision.getContent().get(0).getMetadata().getDelegate()).getUsername();

        assertThat(userModified).isEqualTo(author);
//        List results = auditReader.createQuery()
//                .forRevisionsOfEntityWithChanges(Book.class, false)
//                .add(AuditEntity.property("author").eq(book.getAuthor()))
//                .getResultList();

        AuditQuery q = auditReader.createQuery().forRevisionsOfEntity(Book.class, false, true);
        q.add(AuditEntity.property("author").eq(book.getAuthor()));
        List<Object[]> audit = q.getResultList();
       List<RevisionDto<Book>> revisionDtos =  audit.stream().map(objects -> new RevisionDto<>((Book) objects[0], (AuditRevisionEntity) objects[1], (RevisionType) objects[2])).toList();


//        objList.forEach(o -> {
//            Object[] objArray = (Object[]) o;  )});
//        Object[] objArray = (Object[]) objList.get(0);
//        entityManager.getTransaction().commit();
//        results.isEmpty();

//        assertThat(revision)
//                .isPresent()
//                .hasValueSatisfying(rev ->
//                        assertThat(rev.getRevisionNumber()).hasValue(3)
//                )
//                .hasValueSatisfying(rev ->
//                        assertThat(rev.getEntity())
//                                .extracting(Book::getTitle)
//                                .isEqualTo("If")
//                );
        List<Object> revisionsTimestamp = repository.getRevisions();
        repository.deleteRevisions(Instant.now().minus(30, ChronoUnit.DAYS).toEpochMilli());
        List<Object> revisionsTimestampAfterDelete = repository.getRevisions();
        String.valueOf(RevisionType.ADD);
    }

    @Test
    void deletedItemWillHaveRevisionRetained() {
        repository.delete(book);

        Revisions<Integer, Book> revisions = repository.findRevisions(book.getId());

        assertThat(revisions).hasSize(2);

        Iterator<Revision<Integer, Book>> iterator = revisions.iterator();

        Revision<Integer, Book> initialRevision = iterator.next();
        Revision<Integer, Book> finalRevision = iterator.next();

        assertThat(initialRevision)
                .satisfies(rev ->
                        assertThat(rev.getEntity())
                                .extracting(Book::getId, Book::getAuthor, Book::getTitle)
                                .containsExactly(book.getId(), book.getAuthor(), book.getTitle())
                );

        assertThat(finalRevision)
                .satisfies(rev -> assertThat(rev.getEntity())
                        .extracting(Book::getId, Book::getTitle, Book::getAuthor)
                        .containsExactly(book.getId(), null, null)
                );
    }
}
