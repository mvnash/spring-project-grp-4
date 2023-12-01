package be.vinci.ipl.wallet;

import feign.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, String> {
    @Query("SELECT w FROM wallets w WHERE w.id.investorUsername = :investorUsername")
    Set<Wallet> getAllByInvestorUsername(@Param("investorUsername") String investorUsername);

}

