package be.vinci.ipl.call;

import be.vinci.ipl.call.models.Wallet;

import java.util.ArrayList;

public class CallService {

    public boolean verifyWallet(ArrayList<Wallet> wallets) {
        boolean removed = false;
        for (Wallet wallet : wallets) {
            if (wallet.getQuantity() < 0) {
                removed = true;
                int cashToRemove = (wallet.getUnitvalue() * Math.abs(wallet.getQuantity())) / 100;
                // RETIRER L ARGENT A L UTILISATEUR
            }
        }
        return removed;
    }

}
