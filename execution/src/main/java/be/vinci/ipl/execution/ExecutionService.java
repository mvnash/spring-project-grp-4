package be.vinci.ipl.execution;

import be.vinci.ipl.execution.models.Position;
import be.vinci.ipl.execution.models.Transaction;

import org.springframework.stereotype.Service;

@Service
public class ExecutionService {

    private final WalletProxy walletProxy;
    private final OrderProxy orderProxy;
    private final PriceProxy priceProxy;

    private final VideosProxy videosProxy;

    public ExecutionService(WalletProxy walletProxy, OrderProxy orderProxy, PriceProxy priceProxy) {
        this.walletProxy = walletProxy;
        this.orderProxy = orderProxy;
        this.priceProxy = priceProxy;
    }
    
    /**
     * Executes a transaction between two users for a specific financial instrument
     * @param transaction Transaction details
     * @return Mono of the transaction result // TODO corriger ceci
     */
    public void executeTransaction(Transaction transaction) { // TODO param + return

        /*
        Le service Execution va contacter le service Wallet pour mettre à jour la quantité de CASH possédée par Alice
        Le service Execution va contacter le service Wallet pour mettre à jour la quantité de CASH possédée par Bob
        Le service Execution va contacter le service Wallet pour mettre à jour la quantité de LNRD possédée par Alice
        Le service Execution va contacter le service Wallet pour mettre à jour la quantité de LNRD possédée par Bob

        Le service Execution va contacter le service Price pour indiquer que le dernier prix de vente pour une action LNRD était de 5 CASH.
        Le service Execution va contacter le service Order pour mettre à jour le statut de l'ordre #CAFECAFE
        Le service Execution va contacter le service Order pour mettre à jour le statut de l'ordre #DEADBEEF
        */

        // TODO update position
        walletProxy.updatePosition(transaction.getSeller(), new Position(transaction.getTicker(), -transaction.getQuantity(), transaction.getPrice()));
        walletProxy.updatePosition(transaction.getBuyer(), new Position(transaction.getTicker(), transaction.getQuantity(), transaction.getPrice()));

        // TODO update cash ici
        walletProxy.updatePosition(transaction.getSeller(), new Position("CASH", transaction.getQuantity(), transaction.getPrice()));
        walletProxy.updatePosition(transaction.getBuyer(), new Position("CASH", -transaction.getQuantity(), transaction.getPrice()));

        // TODO update price service
        priceProxy.updateLastSalePrice(transaction.getTicker(), transaction.getPrice()); // TODO getPrice ou bien autre chose ???

        // TODO mettre à jour le status des ordres achat/vente
        orderProxy.updateOne(transaction.getBuyOrderGuid, 1); // TODO filled =? 1 ??? ou pas objet ???
        orderProxy.updateOne(transaction.getSellOrderGuid, 1);
    }
}