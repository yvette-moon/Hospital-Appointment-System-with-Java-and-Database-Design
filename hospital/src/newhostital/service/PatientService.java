package newhostital.service;
import newhostital.DAO.*;
import newhostital.model.*;


public class PatientService {
    public void savePatient(Patient p){ PatientDao.save(p); }
    public static Patient getPatient(String username){ return PatientDao.getPatientByUsername(username); }

}

