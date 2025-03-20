package hse.accounting.command;

import hse.accounting.facade.AnalyticsFacade;

import java.time.LocalDateTime;

public class GetAccountDifferenceForPeriodCommand implements Command {
    private final AnalyticsFacade facade;
    private final Long bankAccountId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public GetAccountDifferenceForPeriodCommand(AnalyticsFacade facade, Long bankAccountId, LocalDateTime startDate, LocalDateTime endDate) {
        this.facade = facade;
        this.bankAccountId = bankAccountId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public void execute() {
        System.out.println(facade.getAccountDifferenceForPeriod(bankAccountId, startDate, endDate));
    }
}
