package rz.demo.boot.data.envers.audit;

import org.hibernate.envers.RevisionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import rz.demo.boot.data.envers.book.TestService;

/**
 * @author Rashidi Zin
 */
public class AuditRevisionListener implements RevisionListener {

    private final TestService testService;

    public AuditRevisionListener(@Autowired @Lazy TestService testService) {
        this.testService = testService;
    }


    @Override
    public void newRevision(Object revisionEntity) {
        AuditRevisionEntity auditRevisionEntity = (AuditRevisionEntity) revisionEntity;
//        String newUser = Objects.requireNonNull(ContextLookup.getBean(TestService.class)).getNewUser();
        String newUser = testService.getNewUser();
        auditRevisionEntity.setUsername(newUser);
    }

}
