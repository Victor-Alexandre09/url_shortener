package spring_app.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring_app.model.AccessLog;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {

}
