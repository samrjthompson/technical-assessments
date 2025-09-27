package org.example.problems.jpmorgan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoneyLending {

    /*
    Alice, Bob and Chris are a group of friends. Alice lends Bob £10, Bob lends Chris £5 and Chris lends Alice £5.
    With the minimal amount of transactions, return the combination of friends and amount to settle all balances.

    While the number of transactions is for you to determine, a single transaction would look like this: "PersonA,PersonB,Amount".
    For both inputs and outputs, PersonA always lends/sends money to PersonB.

    While this example has three transactions, the solution must be compatible with any number of
    transactions, not just these three.

    Example input = "alice,bob,10", "bob,chris,5", "chris,alice,5".
     */

    public List<String> resolveTransactions(List<String> input) {
        List<Transaction> transactions = new ArrayList<>();
        Map<String, Integer> balanceMap = new HashMap<>();

        // Extract transactions and build initial balanceMap with default values
        for (String t : input) {
            String[] array = t.split(",");
            Transaction transaction = new Transaction()
                    .sender(array[0])
                    .receiver(array[1])
                    .amount(array[2]);

            transactions.add(transaction);
            balanceMap.put(transaction.getSender(), 0);
        }

        // Apply input transactions to balanceMap
        for (Transaction transaction : transactions) {
            final String sender = transaction.getSender();
            final String receiver = transaction.getReceiver();
            final int senderBalance = balanceMap.get(sender);
            final int receiverBalance = balanceMap.get(receiver);
            final int delta = transaction.getAmount();

            // minus balance of sender by amount from sender
            balanceMap.put(sender, senderBalance - delta);

            // add balance to receiver by amount from sender
            balanceMap.put(receiver, receiverBalance + delta);
        }

        // Build lists of remaining creditors and debtors
        List<Participant> allParticipants = buildParticipants(balanceMap);
        List<Participant> creditors = allParticipants
                .stream()
                .filter(Participant::isCreditor)
                .toList();

        List<Participant> debtors = allParticipants
                .stream()
                .filter(p -> !p.isCreditor())
                .toList();

        List<String> output = new ArrayList<>();
        for (Participant creditor : creditors) {
            for (Participant debtor : debtors) {
                final int debtorBalance = debtor.getBalance();
                final int creditorBalance = creditor.getBalance();

                // If creditor/debtor has already settled their balance, continue
                if (creditorBalance >= 0 || debtorBalance <= 0) {
                    continue;
                }

                final int paymentAmount;
                if (creditorBalance + debtorBalance > 0) {
                    // only take what's needed from debtor to settle creditor's balance
                    paymentAmount = -creditorBalance;
                    debtor.balance(debtorBalance - paymentAmount);

                } else {
                    // take all from debtor
                    paymentAmount = debtor.getBalance();
                    debtor.balance(0);
                }
                creditor.balance(creditorBalance + paymentAmount);
                output.add(buildOutputString(new Transaction()
                        .sender(debtor.getName())
                        .receiver(creditor.getName())
                        .amount(paymentAmount)));
            }
        }

        return output.stream()
                .sorted()
                .toList();
    }

    private static List<Participant> buildParticipants(Map<String, Integer> map) {
        List<Participant> output = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() != 0) {
                Participant participant = new Participant()
                        .name(entry.getKey())
                        .balance(entry.getValue());

                output.add(participant);
            }
        }

        return output;
    }

    private static String buildOutputString(Transaction transaction) {
        return "%s,%s,%d".formatted(transaction.getSender(), transaction.getReceiver(), transaction.getAmount());
    }

    private static class Transaction {

        private String sender;
        private String receiver;
        private int amount;

        public String getSender() {
            return sender;
        }

        public Transaction sender(String sender) {
            this.sender = sender;
            return this;
        }

        public String getReceiver() {
            return receiver;
        }


        public Transaction receiver(String receiver) {
            this.receiver = receiver;
            return this;
        }

        public int getAmount() {
            return amount;
        }

        public Transaction amount(String amount) {
            this.amount = Integer.parseInt(amount);
            return this;
        }

        public Transaction amount(int amount) {
            this.amount = amount;
            return this;
        }
    }

    private static class Participant {

        private String name;
        private int balance;
        private boolean isCreditor;

        public String getName() {
            return name;
        }

        public Participant name(String name) {
            this.name = name;
            return this;
        }

        public int getBalance() {
            return balance;
        }

        public Participant balance(int balance) {
            this.balance = balance;

            isCreditor = balance < 0;

            return this;
        }

        public boolean isCreditor() {
            return isCreditor;
        }
    }
}
