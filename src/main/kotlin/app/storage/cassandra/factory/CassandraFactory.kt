package app.storage.cassandra.factory

import app.storage.cassandra.dao.DaoMapper
import app.storage.cassandra.dao.being.user.UserDao
import app.storage.cassandra.dao.exhibit.watch.WatchDao
import com.datastax.oss.driver.api.core.CqlIdentifier
import com.datastax.oss.driver.api.core.CqlSession
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class CassandraFactory(cqlSession: CqlSession) {
    private val keyspace: CqlIdentifier = cqlSession.keyspace.get()
    private val daoMapper: DaoMapper = DaoMapper.builder(cqlSession).withDefaultKeyspace(cqlSession.keyspace.get()).build()

    @Singleton
    fun daoMapper(): DaoMapper = daoMapper
    @Singleton
    fun userDao(): UserDao = daoMapper.userDao(keyspace)
    @Singleton
    fun watchDao(): WatchDao = daoMapper.watchDao(keyspace)
}
