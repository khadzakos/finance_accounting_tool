package hse.accounting.command;

import hse.accounting.facade.AnalyticsFacade;

import java.time.LocalDateTime;

public class GetDifferenceForPeriodCommand implements Command {
    private final AnalyticsFacade facade;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public GetDifferenceForPeriodCommand(AnalyticsFacade facade, LocalDateTime startDate, LocalDateTime endDate) {
        this.facade = facade;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public void execute() {
        System.out.println(facade.getDifferenceForPeriod(startDate, endDate));
    }
}
