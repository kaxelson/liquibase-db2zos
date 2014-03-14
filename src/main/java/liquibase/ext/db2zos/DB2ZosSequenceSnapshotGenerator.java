package liquibase.ext.db2zos;

import liquibase.database.Database;
import liquibase.database.ext.db2zos.DB2ZosDatabase;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.snapshot.SnapshotGenerator;
import liquibase.snapshot.jvm.SequenceSnapshotGenerator;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;

public class DB2ZosSequenceSnapshotGenerator extends SequenceSnapshotGenerator {
	@Override
	public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
		if (database instanceof DB2ZosDatabase) {
			return super.getPriority(objectType, database) + 5;
		} else {
			return PRIORITY_NONE;
		}
	}

	@Override
	protected String getSelectSequenceSql(Schema schema, Database database) {
		if (database instanceof DB2ZosDatabase) {
			return "SELECT NAME AS SEQNAME FROM SYSIBM.SYSSEQUENCES WHERE SEQTYPE = 'S' AND SCHEMA = '" + schema.getCatalogName() + "'";
		} else {
			throw new UnexpectedLiquibaseException("Don't know how to query for sequences on " + database);
		}
	}

	@Override
	public Class<? extends SnapshotGenerator>[] replaces() {
		return new Class[] {SequenceSnapshotGenerator.class};
	}
}
