package ru.stepchenkov.db.dao._base;

import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.ColumnMappers;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import ru.stepchenkov.db.dao.DataSourceProvider;
import ru.stepchenkov.env.Env;

@Slf4j
public class _BaseMainDao {

    protected Jdbi jdbi;

    public _BaseMainDao() {
        log.info("Инициализация Base DAO");

        jdbi = Jdbi.create(DataSourceProvider.getH2DataSource(Env.DB.DB_CONFIG));
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.getConfig(ColumnMappers.class).setCoalesceNullPrimitivesToDefaults(false);

        log.info("Base DAO готов");
    }
}
