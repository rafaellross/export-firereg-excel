package au.com.smartplumbingsolutions.fireregister.fireregisterexcel.repository;

import au.com.smartplumbingsolutions.fireregister.fireregisterexcel.entities.Penetration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PenetrationRepository extends JpaRepository<Penetration, Long> {

    List<Penetration> findAllByJobId(Integer job);
}
