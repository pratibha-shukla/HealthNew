package io.catalyte.training.health.domains.encounter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

/**
 * This class is a representation of a sports apparel product.
 */
@Entity
@Table(name = "encounter")
public class Encounter {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;
  Long patientId;
  String notes;
  String visitCode;
  String provider;
  String billingCode;
  String icd10;
  Integer totalCost;
  Integer copay;
  String chiefComplaint;
  Integer pulse;
  Integer systolic;
  Integer diastolic;
  Date date;

}
