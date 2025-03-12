package com.siukatech.poc.react.backend.module.core.data.dialect;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.dialect.pagination.LimitOffsetLimitHandler;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.query.sqm.FetchClauseType;
import org.hibernate.sql.ast.SqlAstTranslator;
import org.hibernate.sql.ast.SqlAstTranslatorFactory;
import org.hibernate.sql.ast.spi.StandardSqlAstTranslatorFactory;
import org.hibernate.sql.ast.tree.Statement;
import org.hibernate.sql.exec.spi.JdbcOperation;

@Slf4j
public class MyPostgreSQLDialect extends PostgreSQLDialect {

    /**
     * Reference:
     * https://stackoverflow.com/a/75359151
     *
     * Above reference saying overriding the getLimitHandler method can fix the offset fetch syntax issue.
     * This is not working for latest hibernate, but older hibernate should be ok.
     *
     * @return
     */
    @Override
    public LimitHandler getLimitHandler() {
        LimitHandler limitHandler = null;
//        limitHandler = OffsetFetchLimitHandler.INSTANCE;
        limitHandler = LimitOffsetLimitHandler.INSTANCE;
        log.debug("getLimitHandler - limitHandler: [{}]", limitHandler);
        return limitHandler;
    }

    /**
     * Return false when FetchClauseType.ROWS_ONLY.
     * This may be better than current implementation.
     *
     * @param type
     * @return
     */
    @Override
    public boolean supportsFetchClause(FetchClauseType type) {
        boolean result = super.supportsFetchClause(type);
        boolean resultForDebug = result;
        if (type.equals(FetchClauseType.ROWS_ONLY)) {
            result = false;
        }
        log.debug("supportsFetchClause - type: [{}], result: [{}], resultForDebug: [{}]", type, result, resultForDebug);
        return result;
    }

    /**
     * Reference:
     * https://stackoverflow.com/a/78953954
     *
     * The sql translation is done by the SqlAstTranslatorFactory.
     * That is the reason why even the LimitHandler got replaced, but nothing happened
     *
     * @return
     */
    public SqlAstTranslatorFactory getSqlAstTranslatorFactory() {
        log.debug("getSqlAstTranslatorFactory - start");
        SqlAstTranslatorFactory sqlAstTranslatorFactory = new StandardSqlAstTranslatorFactory() {
            protected <T extends JdbcOperation> SqlAstTranslator<T> buildTranslator(SessionFactoryImplementor sessionFactory, Statement statement) {
//                return new PostgreSQLSqlAstTranslator(sessionFactory, statement);
                return new MyPostgreSQLSqlAstTranslator(sessionFactory, statement);
            }
        };
        log.debug("getSqlAstTranslatorFactory - sqlAstTranslatorFactory: [{}], end", sqlAstTranslatorFactory);
        return sqlAstTranslatorFactory;
    }

}
