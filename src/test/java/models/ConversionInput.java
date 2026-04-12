package models;

/**
 * Model class representing a currency conversion input.
 *
 * Fields:
 * - from currency
 * - to currency
 * - amount
 */

public class ConversionInput {
    private String from;
    private String to;
    private double amount;

    public ConversionInput() {
    }

    public ConversionInput(String from, String to, double amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ConversionInput{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", amount=" + amount +
                '}';
    }
}