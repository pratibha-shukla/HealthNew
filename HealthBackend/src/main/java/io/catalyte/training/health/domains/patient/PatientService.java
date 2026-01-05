package io.catalyte.training.health.domains.patient;

import jakarta.validation.Valid;
import java.util.List;

public interface PatientService {


  List<Patient> getAll();

  Patient getById(Long id);

  Patient createPatient(@Valid Patient patient);

  Patient updatePatient(Long id, @Valid Patient patient);

  void deletePatient(Long id);
}
