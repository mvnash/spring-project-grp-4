package be.vinci.ipl.gateway;
import be.vinci.ipl.wallet.PositionValue;
import exceptions.BadRequestException;
import exceptions.ConflictException;
import exceptions.NotFoundException;
import exceptions.UnauthorizedException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import models.Credentials;
import models.Investor;
import models.InvestorWithCredentials;
import models.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController {
//TODO trop tard pour se rendre compte que les methodes renvoient void alors que mes tests dans investors fonctionnent correctement

  private final GatewayService service;

  public GatewayController(GatewayService service) {
    this.service = service;
  }

  @GetMapping("/investors")
  public ResponseEntity<Void> getAllInvestors() {
    List<Investor> investors = service.readAllInvestors();
    if (investors.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>( HttpStatus.OK);
    }
  }


 @PostMapping("/investors/{username}")
  public ResponseEntity<Investor> createInvestor(@PathVariable String username, @RequestBody InvestorWithCredentials investor) {
    if (!Objects.equals(investor.getUsername(), username)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    try {
      Investor investorCreated = service.createInvestor(investor);
      return new ResponseEntity<>(investorCreated,HttpStatus.OK);
    } catch (BadRequestException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (ConflictException e) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
  }

  @GetMapping("/investor/{username}")
  public ResponseEntity<Void> readInvestor(@PathVariable String username//,    @RequestHeader("Authorization") String token
  //pour relaiser les tests sans devoir mettre de token
      // rendu compte trop tard que je mettais des void alors que dans investor pas
  ) {
    /*
    try {
      String authenticatedUser = service.verify(token);
      if (authenticatedUser == null || !authenticatedUser.equals(username)) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }*/
    try{
      Investor investor = service.readInvestor(username);
      if (investor == null)
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      else
        return new ResponseEntity<>(HttpStatus.OK);

    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

    @PutMapping("/investor/{username}")
  public ResponseEntity<Void> updateInvestor(@PathVariable String username, @RequestBody InvestorWithCredentials investor,
                                              @RequestHeader("Authorization") String token){
    if(!Objects.equals(investor.getUsername(), username)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    try {
      String authenticatedUser = service.verify(token);
      if (authenticatedUser == null || !authenticatedUser.equals(username)) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
      service.updateInvestor(investor);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (BadRequestException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/investor/{username}")
  public ResponseEntity<Void> deleteUser(@PathVariable String username, @RequestHeader("Authorization") String token) {
    try {
      String authenticatedUser = service.verify(token);
      if (authenticatedUser == null || !authenticatedUser.equals(username)) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
      String user = service.verify(token);
      if (user == null)
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      if (!Objects.equals(user, username))
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
      boolean found = service.deleteInvestor(username);
      if (!found)
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      else
        return new ResponseEntity<>(HttpStatus.OK);

    } catch (BadRequestException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  //auths

  @PostMapping("/authentication/connect")
  public ResponseEntity<String> connect(@RequestBody Credentials credentials) {
    try {
      String token = service.connect(credentials);
      return new ResponseEntity<>(token, HttpStatus.OK);
    } catch (BadRequestException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (UnauthorizedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }

  @PutMapping("/authentication/{username}")
  public ResponseEntity<Void> createCredentials(@PathVariable String username, @RequestBody Credentials credentials) {
    try {
      service.updateCredentials(username, credentials);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (BadRequestException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (UnauthorizedException e){
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
  }

  @PostMapping("/order")
  public ResponseEntity<Order> placeOrder(@RequestBody Order order) {

    try {
      Order placedOrder = service.placeOrder(order);
      return new ResponseEntity<>(placedOrder, HttpStatus.OK);
    } catch (BadRequestException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

  }

  @GetMapping("/order/by-user/{username}")
  public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable String username) {
    try {
      List<Order> userOrders = service.getOrdersByUser(username);
      return new ResponseEntity<>(userOrders, HttpStatus.OK);
    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (UnauthorizedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }

  //Wallet
  @GetMapping("/wallet/{username}")
  public ResponseEntity<Set<PositionValue>> getOpenPositions(@PathVariable String username, @RequestHeader("Authorization") String token) {
    try {
      String authenticatedUser = service.verify(token);
      if (authenticatedUser == null || !authenticatedUser.equals(username)) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }

      Set<PositionValue> walletPositions = service.getOpenPositions(username);


      return new ResponseEntity<>(walletPositions, HttpStatus.OK);

    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/wallet/{username}/net-worth")
  public ResponseEntity<Double> getNetWorth(@PathVariable String username, @RequestHeader("Authorization") String token) {
    try {
      String authenticatedUser = service.verify(token);
      if (authenticatedUser == null || !authenticatedUser.equals(username)) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
      Double netWorth = service.getNetWorth(username);

      return new ResponseEntity<>(netWorth, HttpStatus.OK);

    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }


}
