package diplom.by.robot.repository;

import diplom.by.robot.model.ParticipationRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequestEntity, Integer> {
}
