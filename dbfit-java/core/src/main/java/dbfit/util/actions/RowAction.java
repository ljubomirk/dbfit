package dbfit.util.actions;

import dbfit.fixture.StatementExecution;
import dbfit.util.Cell;
import dbfit.util.Row;

import java.util.Arrays;
import java.util.List;

public class RowAction {
    protected StatementExecution execution;
    private List<Action> possibleActions = Arrays.asList(
            new SaveActualValueToSymbolAction(),
            new StoredValueEqualsSpecifiedValueAssertion(),
            new ActualValueDoesNotEqualSpecifiedValueAssertion(),
            new DisplayActualValueAction(),
            new ActualValueEqualsSpecifiedValueAssertion(),
            new AssignSpecifiedValueToAccessor(),
            new AssignStoredValueToAccessor());

    public RowAction(StatementExecution execution) {
        this.execution = execution;
    }

    /**
     * execute a single row
     */
    public void runRow(Row row) throws Throwable {
        setInputs(row);
        run();
        evaluateOutputs(row);
    }

    private void run() {
        execution.run();
    }

    protected void setInputs(Row row) throws Throwable {
        for (Cell cell : row.getInputCells()) {
            doCell(cell);
        }
    }

    protected void evaluateOutputs(Row row) throws Throwable {
        for (Cell cell : row.getOutputCells()) {
            doCell(cell);
        }
    }

    private void doCell(Cell cell) throws Throwable {
        try {
            new MostAppropriateAction(possibleActions).run(cell);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
