package be.vinci.ipl.gateway;

import exceptions.BadRequestException;
import exceptions.ConflictException;
import exceptions.NotFoundException;
import exceptions.UnauthorizedException;
import java.util.List;
import java.util.Set;
import models.Credentials;
import feign.FeignException;
import models.Investor;
import models.InvestorWithCredentials;
import models.Order;
import org.springframework.stereotype.Service;
import be.vinci.ipl.wallet.PositionValue;
@Service
public class GatewayService {

  private final AuthenticationProxy authenticationProxy;
  private final InvestorsProxy investorsProxy;
  private final OrdersProxy ordersProxy;
  private final WalletProxy walletProxy;



  public  GatewayService(AuthenticationProxy authenticationProxy,  InvestorsProxy investorsProxy,OrdersProxy ordersProxy, WalletProxy walletProxy) {
    this.authenticationProxy =authenticationProxy;
    this.investorsProxy =investorsProxy;
    this.ordersProxy=ordersProxy;
    this.walletProxy= walletProxy;


  }
  public String connect(Credentials credentials) throws BadRequestException, UnauthorizedException {
    try {
      return authenticationProxy.connect(credentials);
    } catch (FeignException e) {
      if (e.status() == 400) throw new BadRequestException();
      else if (e.status() == 401) throw new UnauthorizedException();
      else throw e;
    }
  }

  public void updateCredentials(String username, Credentials credentials)
      throws BadRequestException, UnauthorizedException {
    try {
       authenticationProxy.updateCredentials(username, credentials);

    } catch (FeignException e) {
      if (e.status() == 400) throw new BadRequestException();
      else if (e.status() == 401) throw new UnauthorizedException();
      else throw e;    }
  }

  public String verify(String token) {
    try {
      return authenticationProxy.verify(token);
    } catch (FeignException e) {
      if (e.status() == 401) return null;
      else throw e;
    }
  }



  public Investor createInvestor(InvestorWithCredentials investor) throws BadRequestException, ConflictException {
    try {
      authenticationProxy.createCredentials(investor.getUsername(),  investor.toCredentials());
    } catch (FeignException e) {
      try {
        investorsProxy.deleteInvestor(investor.getUsername());
      } catch (FeignException ignored) {
      }

      if (e.status() == 400)
        throw new BadRequestException();
      else if (e.status() == 409)
        throw new ConflictException();
      else
        throw e;
    }
    try {
      return investorsProxy.createInvestor(investor.getUsername(), investor.toInvestor());

    } catch (FeignException e) {
      if (e.status() == 400)
        throw new BadRequestException();
      else if (e.status() == 409)
        throw new ConflictException();
      else
        throw e;
    }



  }
  public List<Investor> readAllInvestors() {
    try {
      return investorsProxy.readAllInvestors();
    } catch (FeignException e) {
      if (e.status() == 404) return null;
      else throw e;
    }
  }


  public Investor readInvestor(String username) throws NotFoundException {
    try {
      return investorsProxy.readInvestor(username);
    } catch (FeignException e) {
      if (e.status() == 404) throw new NotFoundException();
      else throw e;
    }
  }

  public BadRequestException updateInvestor(InvestorWithCredentials investor)throws NotFoundException, BadRequestException {
    Investor previousInvestor;
    try {
      previousInvestor = investorsProxy.readInvestor(investor.getUsername());

    } catch (FeignException e) {
      if (e.status() == 404)
        throw new NotFoundException();
      else if (e.status()== 400) return new BadRequestException();
       else  throw e;
    }
    try {
      investorsProxy.updateInvestor(investor.getUsername(), investor.toInvestor());
    } catch (FeignException e) {
      if (e.status() == 400)
        throw new BadRequestException();
      else if (e.status() == 404)
        throw new NotFoundException();
      else
        throw e;
    }
    try {
      authenticationProxy.updateCredentials(investor.getUsername(), investor.toCredentials());
    } catch (FeignException e) {
      try {
        investorsProxy.updateInvestor(investor.getUsername(), previousInvestor);
      } catch (FeignException ignored) {
      }

      if (e.status() == 400)
        throw new BadRequestException();
      else if (e.status() == 404)
        throw new NotFoundException();
      else
        throw e;

    }
    return null;
  }


  public boolean deleteInvestor(String username) throws BadRequestException {
    //reviewsProxy.deleteReviewsFromUser(username);
    //videosProxy.deleteVideosFromAuthor(username);
    //TODO delete order from users (read yaml)

    boolean found = true;
    try {
      authenticationProxy.deleteCredentials(username);
    } catch (FeignException e) {
      if (e.status() == 404) found = false;
      else throw e;
    }
    try {
      investorsProxy.deleteInvestor(username);
    } catch (FeignException e) {
      if (e.status() == 404) found = false;
      if(e.status()==400) throw new BadRequestException();
      else throw e;
    }
    return found;
  }

  //auths

  public Order placeOrder(Order order) throws BadRequestException {
    try {
     return ordersProxy.placeOrder(order);
    } catch (FeignException e) {
      if(e.status()==400) throw new BadRequestException();
    }
    return null;
  }

  public List<Order> getOrdersByUser(String username)
      throws NotFoundException, UnauthorizedException {
    try {
      return ordersProxy.getOrdersByUsername(username);
    } catch (FeignException e) {
      if(e.status()==404) throw new NotFoundException();
      else if (e.status()==401) throw new UnauthorizedException();


    }
    return null;
  }

  //Wallet


  public Set<PositionValue> getOpenPositions(String username)
      throws  NotFoundException {
    try{
      return walletProxy.getOpenPositions(username);
    }catch (FeignException e){
      if(e.status()==404) throw new NotFoundException();
      else throw e;
    }

  }

  public Double getNetWorth(String username)
      throws  NotFoundException {
    try {
      return walletProxy.getNetWorth(username);

    }catch (FeignException e){
    if(e.status()==404) throw new NotFoundException();
    else throw e;
  }
  }







}
