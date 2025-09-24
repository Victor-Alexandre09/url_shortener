package spring_app.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring_app.model.CustomUrl;

import java.util.Optional;

@Repository
public interface CustomUrlRepository extends JpaRepository<CustomUrl, String> {
    Optional<CustomUrl> findByOriginalUrl(String originalUrl);
}
