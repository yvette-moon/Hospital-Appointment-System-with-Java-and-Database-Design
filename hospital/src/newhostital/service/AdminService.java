package newhostital.service;

import newhostital.DAO.*;
import newhostital.model.*;

import javax.swing.*;
import java.util.List;

public class AdminService {



    // ==== 医生 ====
    public List<Doctor> getDoctors(String keyword) { return DoctorDao.getDoctors(keyword); }
    public void addDoctor(Doctor d) { DoctorDao.save(d); }
    public void updateDoctor(Doctor d) { DoctorDao.update(d); }
    public void deleteDoctor(int id) { DoctorDao.delete(id); }

    // ==== 病人 ====
    public List<Patient> getPatients(String keyword) { return PatientDao.getPatients(keyword); }
    public void addPatient(Patient p) { PatientDao.save(p); }
    public void updatePatient(Patient p) { PatientDao.update(p); }
    public void deletePatient(int id) { PatientDao.delete(id); }

    // ==== 挂号 ====
    public List<Appointment> getAppointments(String keyword) { return AppointmentDao.getappointments(keyword); }
    public void addAppointment(Appointment a) { AppointmentDao.create(a); }
    public void updateAppointment(Appointment a) { AppointmentDao.update(a); }
    public void deleteAppointment(int id) { AppointmentDao.delete(id); }

    // ==== 医嘱 ====
    public List<Prescription> getAllPrescriptions() {
        return PrescriptionDao.getAllPrescriptions();
    }

    public void addPrescription(Prescription p) { PrescriptionDao.save(p); }
    public void updatePrescription(Prescription p) { PrescriptionDao.update(p); }
    public void deletePrescription(int id) { PrescriptionDao.delete(id); }
    // ==== 用户管理 ====
    public List<User> getUsers(String keyword) {
        return UserDao.getUsers(keyword);
    }

    public void addUser(User user) {
        UserDao.register(user.getUsername(), user.getPassword(), user.getRole());
    }
    public void updateUser(User user) {
        UserDao.update(user);
    }

    public void deleteUser(int userId) {
        UserDao.delete(userId);
    }

}
