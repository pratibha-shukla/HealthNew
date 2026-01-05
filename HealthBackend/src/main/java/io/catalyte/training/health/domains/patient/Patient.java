package io.catalyte.training.health.domains.patient;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * This class is a representation of a sports apparel product.
 */
@Entity
@Table(name = "patient")
public class Patient {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;
  String firstName;
  String lastName;
  String password;
  String ssn;
  String email;
  String street;
  String city;
  String state;
  String postal;
  Integer age;
  Integer height;
  Integer weight;
  String insurance;
  String gender;



}
