package dbfit.fixture;

import dbfit.api.DbObject;
import dbfit.util.DbParameterAccessors;
import dbfit.util.HeaderRow;
import dbfit.util.Row;
import dbfit.util.actions.RowAction;

import java.sql.SQLException;
import java.util.List;

public abstract class ExecutionTable {
    public void run() throws Throwable {
        DbObject dbObject = getTargetDbObject();
        if (!areDataRowsPresent()) {//single execution, no args
            StatementExecution preparedStatement = dbObject.buildPreparedStatement(DbParameterAccessors.EMPTY);
            preparedStatement.run();
        } else {
            DbParameterAccessors accessors = new HeaderRow(getColumnNames(), dbObject).getAccessors();
            StatementExecution execution = dbObject.buildPreparedStatement(accessors.toArray());
            RowAction action = newRowAction(execution);

            for (Row row = nextRow(accessors); row != null; row = nextRow(accessors)) {
                action.runRow(row);
            }
        }
    }

    protected RowAction newRowAction(StatementExecution execution) {
        return new RowAction(execution);
    }

    protected abstract DbObject getTargetDbObject() throws SQLException;
    protected abstract List<String> getColumnNames();
    protected abstract boolean areDataRowsPresent();
    protected abstract Row nextRow(DbParameterAccessors accessors) throws Throwable;
}
