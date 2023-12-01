package com.example.springprojet;

import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class InvestorController {

  private final InvestorService service;
  public InvestorController(InvestorService service){
    this.service=service;
  }

  @GetMapping("/investors")
  public Iterable<Investor> getAllInvestors() {
    return service.readAllInvestors();
  }
  @PostMapping("/investors/{username}")
  public ResponseEntity<Investor> createOne(@PathVariable String username, @RequestBody Investor investor){
    if(!Objects.equals(investor.getUsername(),username)||username.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    Investor foundInvestor= service.readOne(username);
    if(foundInvestor!=null) throw new ResponseStatusException(HttpStatus.CONFLICT);
    Investor createdInvestor =service.createOne(investor);
    if(createdInvestor == null) throw new ResponseStatusException(HttpStatus.CONFLICT);
    return new ResponseEntity<>(createdInvestor,HttpStatus.CREATED);
  }

  @GetMapping("/investors/{username}")
  public ResponseEntity<Investor> readOne(@PathVariable String username ){
    if(username.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    Investor investor = service.readOne(username);
    if(investor==null) throw new ResponseStatusException( HttpStatus.NOT_FOUND);
    return new ResponseEntity<>(investor,HttpStatus.OK);

  }
  @PutMapping("/investors/{username}")
  public ResponseEntity<Investor> updateOne(@PathVariable String username, @RequestBody Investor investor){
    //a voir si l'investor peut changer de username ou non.
    if(!Objects.equals(investor.getUsername(),username)||username.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    Investor updatedInvestor = service.updateOne( investor);
    System.out.println(updatedInvestor);
    if(updatedInvestor==null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    return new ResponseEntity<>(updatedInvestor,HttpStatus.OK);
  }

  @DeleteMapping("/investors/{username}")
  public ResponseEntity<Investor> deleteOne(@PathVariable String username){
    if(username.isBlank()){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    //TODO utiliser le verify pour faire l'erreur 404
    Investor investor = service.deleteOne(username);
    if(investor == null) throw new ResponseStatusException(HttpStatus.CONFLICT);
    return new ResponseEntity<>(investor, HttpStatus.OK);

  }
}
