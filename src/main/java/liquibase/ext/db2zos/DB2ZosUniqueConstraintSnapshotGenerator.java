package liquibase.ext.db2zos;

import java.util.List;
import java.util.Map;

import liquibase.database.Database;
import liquibase.database.ext.db2zos.DB2ZosDatabase;
import liquibase.exception.DatabaseException;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.executor.ExecutorService;
import liquibase.snapshot.SnapshotGenerator;
import liquibase.snapshot.jvm.UniqueConstraintSnapshotGenerator;
import liquibase.statement.core.RawSqlStatement;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;
import liquibase.structure.core.Table;
import liquibase.structure.core.UniqueConstraint;

public class DB2ZosUniqueConstraintSnapshotGenerator extends UniqueConstraintSnapshotGenerator {
	@Override
	public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
		if (database instanceof DB2ZosDatabase) {
			return super.getPriority(objectType, database) + 5;
		} else {
			return PRIORITY_NONE;
		}
	}

	@Override
	protected List<Map<String, ?>> listColumns(UniqueConstraint example, Database database) throws DatabaseException {
		Table table = example.getTable();
		Schema schema = table.getSchema();
		String name = example.getName();

		String sql = null;
		if (database instanceof DB2ZosDatabase) {
			sql = "SELECT K.COLNAME, K.TBCREATOR, K.TBNAME AS column_name FROM SYSIBM.SYSKEYCOLUSE K " +
					"JOIN SYSIBM.SYSTABCONST T " +
					"ON K.CONSTNAME = T.CONSTNAME " +
					"AND K.TBCREATOR = T.TBCREATOR " +
					"AND K.TBNAME = T.TBNAME " +
					"WHERE T.TYPE = 'U' " +
					"AND T.TBNAME = '" + database.correctObjectName(table.getName(), Table.class) + "' " +
					"AND T.TBCREATOR = '" + database.correctObjectName(schema.getCatalogName(), Schema.class) + "' " +
					"AND K.CONSTNAME = '" + database.correctObjectName(name, UniqueConstraint.class) + "' " +
					"ORDER BY K.COLSEQ";
		} else {
			throw new UnexpectedLiquibaseException("Don't know how to query for unique constraints on " + database);
		}

		return ExecutorService.getInstance().getExecutor(database).queryForList(new RawSqlStatement(sql));
	}

	@Override
	public Class<? extends SnapshotGenerator>[] replaces() {
		return new Class[] {UniqueConstraintSnapshotGenerator.class};
	}
}
