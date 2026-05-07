package giuseppetavella.demo_login_system.repositories;

import giuseppetavella.demo_login_system.entities.Notification;
import giuseppetavella.demo_login_system.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationsRepository extends JpaRepository<Notification, UUID> {

    /**
     * Find notifications of the given user.
     */
    @Query("""

        SELECT n
        FROM Notification n
        WHERE
            n.user = :user
            AND (
                :filterRead IS NULL 
                OR (:filterRead = true AND n.readAt IS NOT NULL) 
                OR (:filterRead = false AND n.readAt IS NULL)
            )
            AND (
                :notificationType IS NULL 
                OR n.type = :notificationType
            )
            
    """)
    Page<Notification> findNotificationsByUser(
            User user,
            Boolean filterRead,
            String notificationType,
            Pageable pageable
    );
    
}
