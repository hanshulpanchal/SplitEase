package com.money.SplitEase.util;

import com.money.SplitEase.model.Expense;
import com.money.SplitEase.model.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class DebtCalculator {

    public static DebtSummary calculateDebts(List<Expense> expenses, List<User> users) {
        var balances = new HashMap<User, BigDecimal>();
        var debts = new HashMap<User, Map<User, BigDecimal>>();

        // Initialize balances & debts
        users.forEach(user -> {
            balances.put(user, BigDecimal.ZERO);
            debts.put(user, users.stream()
                    .filter(other -> !other.equals(user))
                    .collect(Collectors.toMap(other -> other, other -> BigDecimal.ZERO)));
        });

        // Calculate balances after expenses
        expenses.forEach(expense -> {
            var payer = expense.getPayer();
            var amount = expense.getAmount();
            var splitAmount = amount.divide(BigDecimal.valueOf(users.size()), 2, RoundingMode.HALF_UP);



            balances.compute((User) payer, (u, old) -> (old != null ? old : BigDecimal.ZERO).add(amount.subtract(splitAmount)));
            users.stream()
                    .filter(user -> !user.equals(payer))
                    .forEach(user -> balances.compute(user, (u, old) -> old.subtract(splitAmount)));
        });

        // Calculate debts
        var balanceList = new ArrayList<>(balances.entrySet());
        balanceList.sort(Map.Entry.comparingByValue());

        int i = 0, j = balanceList.size() - 1;
        while (i < j) {
            var debtorEntry = balanceList.get(i);
            var creditorEntry = balanceList.get(j);

            var debtor = debtorEntry.getKey();
            var creditor = creditorEntry.getKey();

            var debtAmount = debtorEntry.getValue().negate().min(creditorEntry.getValue());
            if (debtAmount.compareTo(BigDecimal.ZERO) > 0) {
                debts.get(debtor).put(creditor, debtAmount);

                balances.put(debtor, balances.get(debtor).add(debtAmount));
                balances.put(creditor, balances.get(creditor).subtract(debtAmount));

                debtorEntry.setValue(balances.get(debtor));
                creditorEntry.setValue(balances.get(creditor));
            }

            if (debtorEntry.getValue().compareTo(BigDecimal.ZERO) == 0) i++;
            if (creditorEntry.getValue().compareTo(BigDecimal.ZERO) == 0) j--;
        }

        return new DebtSummary(debts, balances);
    }

    public record DebtSummary(
            Map<User, Map<User, BigDecimal>> debts,
            Map<User, BigDecimal> finalBalances
    ) {}
}
