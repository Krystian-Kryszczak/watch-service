package app.factory.cassandra

import app.storage.cassandra.dao.DaoMapper
import app.storage.cassandra.dao.being.user.UserDao
import app.storage.cassandra.dao.exhibit.watch.WatchDao
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.micronaut.test.extensions.kotest.annotation.MicronautTest

@MicronautTest
class CassandraFactoryTest(
    private val daoMapper: DaoMapper,
    private val userDao: UserDao,
    private val watchDao: WatchDao
): StringSpec({

    "dao mapper inject test" {
        daoMapper shouldNotBe null
    }

     "user dao inject test" {
         userDao shouldNotBe null
    }

     "watch dao inject test" {
         watchDao shouldNotBe null
    }
})
