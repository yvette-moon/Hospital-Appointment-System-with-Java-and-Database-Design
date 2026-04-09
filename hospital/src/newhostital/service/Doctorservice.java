package newhostital.service;

import newhostital.DAO.AppointmentDao;
import newhostital.DAO.DoctorDao;
import newhostital.model.Doctor;
import java.util.List;

public class Doctorservice {


    public void saveDoctor(Doctor p) {
        DoctorDao.save(p);
    }

    public void updateAppointmentStatus(int appointmentId, String status) {
        AppointmentDao.updateStatus(appointmentId, status);
    }

    public static List<Doctor> getDoctorsByDeptId(int deptId) {
        return new DoctorDao().getDoctorsByDeptId(deptId);

    }

}
