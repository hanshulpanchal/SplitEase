import com.money.SplitEase.dto.ExpenseDTO;
import com.money.SplitEase.model.Expense;
import com.money.SplitEase.model.Group;
import com.money.SplitEase.model.User;

public Expense toEntity(ExpenseDTO dto) {
    Expense expense = new Expense();
    expense.setId(dto.getId());
    expense.setDescription(dto.getDescription());
    expense.setAmount(dto.getAmount());
    expense.setDate(dto.getDate());

    // Set minimal User and Group with only ID
    User payer = new User();
    payer.setId(dto.getPayerId());
    expense.setPayer(payer);

    Group group = new Group();
    group.setId(dto.getGroupId());
    expense.setGroup(group);

    return expense;
}

void main() {
}
