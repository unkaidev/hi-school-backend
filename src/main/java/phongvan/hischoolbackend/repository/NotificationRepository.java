package phongvan.hischoolbackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import phongvan.hischoolbackend.entity.Notification;
import phongvan.hischoolbackend.entity.User;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Integer> {
    List<Notification> findAllByReceiverAndIsRead(User receiver, boolean isRead);

    List<Notification> findAllByReceiver(User user);
}
