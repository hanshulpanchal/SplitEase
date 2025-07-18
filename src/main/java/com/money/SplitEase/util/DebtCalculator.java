package com.money.SplitEase.util;

import com.money.SplitEase.model.Expense;
import com.money.SplitEase.model.Group;
import com.money.SplitEase.model.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


public class DebtCalculator {

    /**
     * Calculates debts and final balances for a list of users based on given expenses.
     */
    public static DebtSummary calculateDebts(List<Expense> expenses, List<User> users) {
        Map<User, BigDecimal> balances = new HashMap<>();
        Map<User, Map<User, BigDecimal>> debts = new HashMap<>();

        // Initialize balances and debts
        users.forEach(user -> {
            balances.put(user, BigDecimal.ZERO);
            debts.put(user, new HashMap<>());
        });

        // Update balances based on each expense
        for (Expense expense : expenses) {
            User payer = expense.getPayer();
            BigDecimal amount = expense.getAmount();
            BigDecimal splitAmount = amount.divide(BigDecimal.valueOf(users.size()), 2, RoundingMode.HALF_UP);

            balances.put(payer, balances.get(payer).add(amount.subtract(splitAmount)));

            for (User user : users) {
                if (!user.equals(payer)) {
                    balances.put(user, balances.get(user).subtract(splitAmount));
                }
            }
        }

        // Settle debts
        List<Map.Entry<User, BigDecimal>> balanceList = new ArrayList<>(balances.entrySet());
        balanceList.sort(Map.Entry.comparingByValue());

        int i = 0, j = balanceList.size() - 1;
        while (i < j) {
            User debtor = balanceList.get(i).getKey();
            User creditor = balanceList.get(j).getKey();

            BigDecimal debtorBalance = balanceList.get(i).getValue();
            BigDecimal creditorBalance = balanceList.get(j).getValue();

            BigDecimal amount = debtorBalance.negate().min(creditorBalance);

            if (amount.compareTo(BigDecimal.ZERO) > 0) {
                debts.get(debtor).put(creditor, amount);

                BigDecimal newDebtorBalance = debtorBalance.add(amount);
                BigDecimal newCreditorBalance = creditorBalance.subtract(amount);

                balanceList.get(i).setValue(newDebtorBalance);
                balanceList.get(j).setValue(newCreditorBalance);
                balances.put(debtor, newDebtorBalance);
                balances.put(creditor, newCreditorBalance);
            }

            if (balanceList.get(i).getValue().compareTo(BigDecimal.ZERO) == 0) i++;
            if (balanceList.get(j).getValue().compareTo(BigDecimal.ZERO) == 0) j--;
        }

        return new DebtSummary(debts, balances);
    }

    /**
     * Overloaded method to calculate debts from a Group object.
     */
    public static Map<String, Map<String, Double>> calculateDebts(Group group) {
        Map<String, Map<String, Double>> result = new HashMap<>();

        if (group == null || group.getExpenses() == null || group.getMembers() == null || group.getExpenses().isEmpty()) {
            return result;
        }

        DebtSummary summary = DebtCalculator.calculateDebts(
                new ArrayList<>(group.getExpenses()),
                new ArrayList<>(group.getMembers())
        );

        for (Map.Entry<User, Map<User, BigDecimal>> debtorEntry : summary.debts().entrySet()) {
            String debtorName = debtorEntry.getKey().getUsername(); // Use getUsername() if available

            Map<String, Double> userDebts = new HashMap<>();
            for (Map.Entry<User, BigDecimal> inner : debtorEntry.getValue().entrySet()) {
                if (inner.getValue().compareTo(BigDecimal.ZERO) > 0) {
                    userDebts.put(inner.getKey().getUsername(), inner.getValue().doubleValue());
                }
            }

            if (!userDebts.isEmpty()) {
                result.put(debtorName, userDebts);
            }
        }

        return result;
    }


    /**
     * Record to hold the results.
     */
    public record DebtSummary(
            Map<User, Map<User, BigDecimal>> debts,
            Map<User, BigDecimal> finalBalances
    ) {}
}
