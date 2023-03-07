package org.healthadvicegroup.account;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.healthadvicegroup.database.Serializable;

import java.util.UUID;

@Getter
public class UserAccount implements Serializable<UserAccount> {

    private final UUID id;
    private final String username;
    private final String email;
    private final EnumAccountType accountType;
    private final FitnessTracker fitnessTracker;

    // Used to determine whether an account should be saved or not
    @Setter private boolean dirty;

    // Constructor for deserializing user accounts
    public UserAccount(Document document) {
        this.id = UUID.fromString(document.getString("_id"));
        this.username = document.getString("username");
        this.email = document.getString("email");
        this.accountType = EnumAccountType.valueOf(document.getString("account-type"));
        this.fitnessTracker = new FitnessTracker(this, document.get("fitness-tracker", Document.class));
    }

    // Constructor for when a new account is created
    public UserAccount(String username, String email) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.accountType = EnumAccountType.USER;
        this.fitnessTracker = new FitnessTracker(this);
        AccountManager.registerAccount(this);
        AccountManager.saveAccount(this);
    }

    @Override
    public Document toDocument(UserAccount object) {
        return new Document("_id", object.getId())
                .append("username", object.username)
                .append("email", object.email)
                .append("account-type", object.accountType.name())
                .append("fitness-tracker", object.fitnessTracker.toDocument(object.fitnessTracker));
    }
}
