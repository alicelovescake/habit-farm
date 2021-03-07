package model;

import org.json.JSONObject;
import persistence.Writable;

import java.time.LocalDate;
import java.util.UUID;

// class representing a transaction every time a purchase or request for money is made
public class Transaction implements Writable {
    private Account recipient;
    private Account sender;
    private String id;
    private double amount;
    private LocalDate date;

    //Pending: money has yet to be changed hands, complete: money has changed hands, failed, money hasn't changed hands
    public enum Status {
        PENDING, COMPLETE, FAILED
    }

    public enum Type {
        REQUEST, EXCHANGE
    }

    private Status status;
    private Type type;

    public Transaction(Account recipient, Account sender, double amount, Type type) {
        this.recipient = recipient;
        this.amount = amount;
        this.sender = sender;
        this.id = UUID.randomUUID().toString();
        this.date = LocalDate.now();
        this.status = Status.PENDING;
        this.type = type;

        if (this.type != Type.REQUEST) {
            completeTransaction();
        }

    }

    // getters
    public Status getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public String getSenderUsername() {
        return sender.getUser().getUsername();
    }

    public String getRecipientUsername() {
        return recipient.getUser().getUsername();
    }

    public User getRecipient() {
        return recipient.getUser();
    }

    public Account getSenderAccount() {
        return sender;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public Type getType() {
        return type;
    }

    //MODIFY: this
    // EFFECTS: Process transaction, status changes to complete if sender has sufficient funds, status FAILED otherwise
    public void completeTransaction() {
        if (sender.getBalance() >= amount) {
            sender.decrementBalance(amount);
            recipient.incrementBalance(amount);
            this.status = Status.COMPLETE;
        } else {
            this.status = Status.FAILED;
        }
    }

    @Override
    //EFFECTS: returns transactions in this account as a JSON object
    public JSONObject toJson() {
        JSONObject transactionJson = new JSONObject();
        transactionJson.put("recipient", recipient);
        transactionJson.put("sender", sender);
        transactionJson.put("id", id);
        transactionJson.put("date", date);
        transactionJson.put("amount", amount);
        transactionJson.put("status", status);
        transactionJson.put("type", type);
        return transactionJson;
    }
}
