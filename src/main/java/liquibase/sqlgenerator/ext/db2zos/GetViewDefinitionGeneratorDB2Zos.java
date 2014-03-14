package liquibase.sqlgenerator.ext.db2zos;

import liquibase.CatalogAndSchema;
import liquibase.database.Database;
import liquibase.database.ext.db2zos.DB2ZosDatabase;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.GetViewDefinitionGeneratorDB2;
import liquibase.statement.core.GetViewDefinitionStatement;

public class GetViewDefinitionGeneratorDB2Zos extends GetViewDefinitionGeneratorDB2 {
	@Override
	public int getPriority() {
		return super.getPriority() + 5;
	}

	@Override
	public boolean supports(GetViewDefinitionStatement statement, Database database) {
		return database instanceof DB2ZosDatabase;
	}

	@Override
	public Sql[] generateSql(GetViewDefinitionStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
		CatalogAndSchema schema = database.correctSchema(new CatalogAndSchema(statement.getCatalogName(), statement.getSchemaName()));
		return new Sql[] {new UnparsedSql(
			"SELECT STATEMENT FROM SYSIBM.SYSVIEWS WHERE NAME = '" + statement.getViewName() + "' AND CREATOR = '" + schema.getSchemaName() + "'"
		)};
	}
}
