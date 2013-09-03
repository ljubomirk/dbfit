package dbfit.fixture;

import dbfit.api.DbObject;
import dbfit.util.DbParameterAccessors;
import dbfit.util.Row;
import dbfit.util.fit.FitHelpers;
import fit.Fixture;
import fit.Parse;

import java.sql.SQLException;
import java.util.List;

public class FitExecutionTable extends ExecutionTable {
    private Fixture fixture;
    private Parse rows;
    private Parse currentRow;

    public FitExecutionTable(Fixture fixture, Parse rows) {
        this.fixture = fixture;
        this.rows = rows;
        if (rows != null)
            this.currentRow = rows.more;
    }

    protected DbObject getTargetDbObject() throws SQLException {
        throw new RuntimeException("should be implemented by subclasses");
    }

    @Override
    protected boolean areDataRowsPresent() {
        return rows != null;
    }

    @Override
    protected List<String> getColumnNames() {
        return FitHelpers.getCellTextFrom(rows.parts);
    }

    @Override
    protected Row nextRow(DbParameterAccessors accessors) throws Throwable {
        if (currentRow == null) return null;
        Row row = new Row(accessors, currentRow, fixture);
        currentRow = currentRow.more;
        return row;
    }

}
