package liquibase.integration.commandline

import spock.lang.Specification

class GenerateChangeLogTest extends Specification {
	def 'generateChangeLog'() {
		when:
			Main.main([
				'--driver=com.ibm.db2.jcc.DB2Driver',
				'--classpath=C:/IBM/SQLLIB/java/db2jcc4.jar;./bin',
				'--changeLogFile=db.changelog.xml',
				'--url=jdbc:db2://DB2B:5031/DB2B',
				'--username=c20100a',
				'--password=Samps0n1',
//				'--defaultSchemaName=ORDORDER',
				'generateChangeLog'
			] as String[]);

		then:
			notThrown(Exception)
	}
}

//--driver=com.ibm.db2.jcc.DB2Driver
//--classpath=C:\IBM\SQLLIB\java\db2jcc4.jar
//--changeLogFile=db.changelog.xml
//--url=jdbc:db2://DB2B:5031/DB2B
//--username=c20100a
//--password=Samps0n1
//--defaultSchemaName=ORDORDER
