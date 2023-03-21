package org.healthadvicegroup.account;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.LongPasswordStrategies;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.healthadvicegroup.Main;
import org.healthadvicegroup.database.Serializable;

import java.util.UUID;

@Getter
public class UserAccount implements Serializable<UserAccount> {

    private final UUID id;
    private final String username;
    private final byte[] passwordHash;
    private final String email;
    private final EnumAccountType accountType;
    private final FitnessTracker fitnessTracker;

    // Used to determine whether an account should be saved or not
    @Setter private boolean dirty;

    // Constructor for deserializing user accounts
    public UserAccount(Document document) {
        this.id = document.get("_id", UUID.class);
        this.username = document.getString("username");
        this.passwordHash = Main.getGSON().fromJson(document.getString("password-hash"), new TypeToken<byte[]>(){}.getType());
        this.email = document.getString("email");
        this.accountType = EnumAccountType.valueOf(document.getString("account-type"));
        this.fitnessTracker = new FitnessTracker(this, document.get("fitness-tracker", Document.class));
    }

    // Constructor for when a new account is created
    public UserAccount(String username, String email, String password) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.passwordHash = BCrypt.with(LongPasswordStrategies.hashSha512(BCrypt.Version.VERSION_2A)).hash(6, password.toCharArray());
        this.accountType = EnumAccountType.USER;
        this.fitnessTracker = new FitnessTracker(this);
        AccountManager.registerAccount(this);
        AccountManager.saveAccount(this);
        System.out.printf("Created account with username %s\n", username);
    }

    @Override
    public Document toDocument(UserAccount object, Gson gson) {
        return new Document("_id", object.getId())
                .append("username", object.username)
                .append("password-hash", new Gson().toJson(object.passwordHash)) // we don't want pretty-printing so we will use a new Gson object
                .append("email", object.email)
                .append("account-type", object.accountType.name())
                .append("fitness-tracker", object.fitnessTracker.toDocument(object.fitnessTracker, gson));
    }

    /**
     * Return whether the provided {@link String} password is correct
     *
     * @param input the provided password
     *
     * @return whether the password matches {@link #passwordHash}
     */
    public boolean verifyPassword(String input) {
        return BCrypt.verifyer().verify(input.toCharArray(), this.passwordHash).verified;
    }
}
