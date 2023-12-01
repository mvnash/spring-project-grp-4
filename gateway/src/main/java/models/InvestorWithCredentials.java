package models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InvestorWithCredentials {
  private String username;
  private String firstname;
  private String lastname;
  private String password;

  public Investor toInvestor(){
    return new Investor(username,firstname,lastname);
  }
  public Credentials toCredentials(){
    return new Credentials(username,password);
  }

}
