package com.example.springprojet;

import org.springframework.stereotype.Service;

@Service
public class InvestorService {
  private final InvestorRepository repository;

  public InvestorService(InvestorRepository repository){
    this.repository= repository;

  }
  public Iterable<Investor> readAllInvestors(){
    return repository.findAll();
  }

  public Investor createOne(Investor investor){
    if(repository.existsById(investor.getUsername()))return null;
    repository.save(investor);
    return investor;
  }

  public Investor readOne(String username ){
      return repository.findById(username).orElse(null);
  }

  public Investor updateOne(Investor investor){
    if(!repository.existsById(investor.getUsername()))return null;
    System.out.println("exists");
    repository.save(investor);
    return investor;

  }

  public Investor deleteOne(String username){
    if(!repository.existsById(username))return null;
    Investor investor = repository.findById(username).orElse(null);
    repository.deleteById(username);
    return  investor;
  }


}
