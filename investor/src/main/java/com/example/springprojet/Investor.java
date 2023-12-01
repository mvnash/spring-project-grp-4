package com.example.springprojet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name="investors")
public class Investor {

  @Id
  @Column(nullable = false)
  private String username;
  @Column(nullable = false)
  private String email;
  @Column(nullable = false)
  private String firstname;
  @Column(nullable = false)
  private String lastname;
  @Column(nullable = false)
  private String birthdate;
  @Column(nullable = false)
  private String password;




}
