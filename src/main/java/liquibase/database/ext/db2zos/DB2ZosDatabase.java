package liquibase.database.ext.db2zos;

import liquibase.database.DatabaseConnection;
import liquibase.database.core.DB2Database;
import liquibase.exception.DatabaseException;

public class DB2ZosDatabase extends DB2Database {
	@Override
	public int getPriority() {
		return super.getPriority() + 5;
	}

	@Override
	public boolean isCorrectDatabaseImplementation(DatabaseConnection conn) throws DatabaseException {
		return conn.getDatabaseProductName().startsWith("DB2") && conn.getDatabaseProductVersion().startsWith("DSN");
	}

	@Override
	public Integer getDefaultPort() {
		return 5031;
	}

	@Override
	protected String getDefaultDatabaseProductName() {
		return "DB2 for z/OS";
	}

	@Override
	public String getShortName() {
		return "db2zos";
	}

	@Override
	public boolean supportsSchemas() {
		return true;
	}
}
