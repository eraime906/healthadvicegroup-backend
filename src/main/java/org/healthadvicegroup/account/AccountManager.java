package org.healthadvicegroup.account;

import org.bson.Document;
import org.healthadvicegroup.Main;
import org.healthadvicegroup.database.MongoCollectionManager;
import org.healthadvicegroup.database.MongoCollectionWrapper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AccountManager {

    // A ConcurrentHashMap will allow us to save accounts asynchronously
    private static final ConcurrentHashMap<UUID, UserAccount> accountCache = new ConcurrentHashMap<>();

    // Fast cache used to quickly determine whether a username exists or not
    private static final HashSet<String>usernameCache = new HashSet<>();
    private static final MongoCollectionWrapper accountCollection = MongoCollectionManager.getCollectionWrapper("accounts");

    public static void init() {
        // Deserialize existing accounts from the database
        long start = System.currentTimeMillis();
        for (Document document : accountCollection.getAllDocuments()) {
            try {
                registerAccount(new UserAccount(document));
            } catch (Exception ex) { // catch all possible exceptions when deserializing
                System.out.printf("Failed to deserialize account with id %s\n", document.get("_id"));
                ex.printStackTrace();
            }
        }
        System.out.printf("Deserialized %s accounts in %sms\n", accountCache.size(), System.currentTimeMillis() - start);

        // Setup loop to save accounts
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // Do large database I/O operations on a separate thread
                new Thread(() -> {
                    double total = 0;
                    for (Map.Entry<UUID, UserAccount> entry : accountCache.entrySet()) {
                        if (!entry.getValue().isDirty()) {
                            continue;
                        }
                        ++total;
                        if (!saveAccount(entry.getValue())) {
                            System.out.printf("Failed to save account with id %s\n", entry.getValue().getId().toString());
                        }
                    }
                    System.out.printf("Saved %s accounts\n", total);
                }).start();
            }
        }, 1000 * 300); // Auto-save accounts every 5 minutes
    }

    /**
     * Register the creation of an account object, whether it's a new account or a deserialized one
     *
     * @param account the account to register
     */
    public static void registerAccount(UserAccount account) {
        accountCache.put(account.getId(), account);
        usernameCache.add(account.getUsername().toLowerCase(Locale.ROOT));
    }

    /**
     * Save the provided {@link UserAccount} to {@link #accountCollection}
     *
     * @param account the account to save
     *
     * @return whether the account was successfully saved or not
     */
    public static boolean saveAccount(UserAccount account) {
        try {
            accountCollection.saveDocument(account.toDocument(account, Main.getGSON()));
            account.setDirty(false);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Fetch the {@link UserAccount} from {@link #accountCache} with the provided id
     * <p>
     * @see #getAccount(UUID)
     *
     * @param id the account id to fetch
     *
     * @return the account, if it exists
     */
    public static UserAccount getAccount(String id) {
        return getAccount(UUID.fromString(id));
    }

    /**
     * Fetch the {@link UserAccount} from {@link #accountCache} with the provided id
     *
     * @param id the account id to fetch
     *
     * @return the account, if it exists
     */
    public static UserAccount getAccount(UUID id) {
        return accountCache.get(id);
    }

    /**
     * Returns whether an {@link UserAccount} exists with the provided {@link String} name
     *
     * @param username the username to check validity of
     *
     * @return whether an account exists with that username or not
     */
    public static boolean doesUsernameExist(String username) {
        return usernameCache.contains(username.toLowerCase(Locale.ROOT));
    }

}
