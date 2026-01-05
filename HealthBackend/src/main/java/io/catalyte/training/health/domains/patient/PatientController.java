package io.catalyte.training.health.domains.patient;

import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
public class PatientController {
  private final Logger logger = LoggerFactory.getLogger(PatientController.class);

  @Autowired
  private PatientService patientService;

  /**
   * This method retrieves all reservations from the database
   *
   * @return a collection of reservations and 200 status code
   * @throws Exception
   */
  @GetMapping
  public ResponseEntity<List<Patient>> getAllPatients() {
    logger.info(" Get all request received");
    return new ResponseEntity<>(patientService.getAll(), HttpStatus.OK);
  }

  /**
   * This method retrieves a single reservation from the database
   *
   * @return a reservation by the id provided and 200 status code
   * @throws Exception
   */
  @GetMapping("/{id}")
  public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
    logger.info(" Get all request received");
    return new ResponseEntity<>(patientService.getById(id), HttpStatus.OK);
  }

  /**
   * This method creates a new reservation record and saves it to the database
   *
   * @param patient to be created
   * @return created reservation and 201 status code
   * @throws Exception
   */
  @PostMapping
  public ResponseEntity<Patient> createPatient(
      @Valid @RequestBody Patient patient) {
    logger.info(" Post request received");
    return new ResponseEntity<>(
        patientService.createPatient(patient), HttpStatus.CREATED);
  }

  /**
   * This method updates an existing reservation record
   *
   * @param id          of the reservation to be updated
   * @param patient updated reservation information
   * @return updated reservation and 200 status code
   * @throws Exception
   */
  @PutMapping("/{id}")
  public ResponseEntity<Patient> updatePatient(
      @PathVariable Long id, @Valid @RequestBody Patient patient) {
    logger.info(" Put request received");
    return new ResponseEntity<>(
        patientService.updatePatient(id, patient), HttpStatus.OK);
  }

  /**
   * This method deletes an existing reservation record
   *
   * @param id of the reservation to be deleted
   * @return 204 no content
   * @throws Exception
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Patient> deletePatient(@PathVariable Long id) {
    logger.info(" Delete request received");
    patientService.deletePatient(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}



