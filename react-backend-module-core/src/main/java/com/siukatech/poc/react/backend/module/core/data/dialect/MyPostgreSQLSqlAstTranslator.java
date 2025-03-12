package com.siukatech.poc.react.backend.module.core.data.dialect;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.PostgreSQLSqlAstTranslator;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.query.sqm.FetchClauseType;
import org.hibernate.sql.ast.tree.Statement;
import org.hibernate.sql.ast.tree.select.QueryPart;

@Slf4j
public class MyPostgreSQLSqlAstTranslator extends PostgreSQLSqlAstTranslator {

    public MyPostgreSQLSqlAstTranslator(SessionFactoryImplementor sessionFactory, Statement statement) {
        super(sessionFactory, statement);
    }

    /**
     * Change renderOffsetFetchClause to renderLimitOffsetClause if there is the offset fetch issue.
     * Currently there is no issue, this class is created as reference.
     * @param queryPart
     */
    @Override
    public void visitOffsetFetchClause(QueryPart queryPart) {
        log.debug("visitOffsetFetchClause - start");
        boolean isRowNumberingCurrentQueryPart = this.isRowNumberingCurrentQueryPart();
        log.debug("visitOffsetFetchClause - isRowNumberingCurrentQueryPart: [{}]", isRowNumberingCurrentQueryPart);
//        if (!this.isRowNumberingCurrentQueryPart()) {
        if (!isRowNumberingCurrentQueryPart) {
            if (this.getDialect().supportsFetchClause(FetchClauseType.ROWS_ONLY)) {
//                this.renderOffsetFetchClause(queryPart, true);
                this.renderOffsetFetchClause(queryPart, true);
            } else {
                this.renderLimitOffsetClause(queryPart);
            }
        }
        log.debug("visitOffsetFetchClause - end");
    }

}
