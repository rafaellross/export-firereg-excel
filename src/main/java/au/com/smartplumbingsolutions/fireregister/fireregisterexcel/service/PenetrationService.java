package au.com.smartplumbingsolutions.fireregister.fireregisterexcel.service;

import au.com.smartplumbingsolutions.fireregister.fireregisterexcel.entities.Penetration;
import au.com.smartplumbingsolutions.fireregister.fireregisterexcel.repository.PenetrationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PenetrationService {

    private PenetrationRepository repository;

    public PenetrationService(PenetrationRepository repository) {
        this.repository = repository;
    }

    public List<Penetration> findAll(){
        return this.repository.findAll();
    }

    public List<Penetration> findAllByJob(Integer jobId){
        return this.repository.findAllByJobId(jobId);
    }

}
