package app.service.being.user

import app.model.being.user.User
import app.service.AbstractItemService
import app.storage.cassandra.dao.being.user.UserDao
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import jakarta.inject.Singleton
import java.util.UUID

@Singleton
class CassandraUserService(private val userDao: UserDao): UserService, AbstractItemService<User>(userDao) {
    override fun saveAsync(user: User): Completable = Completable.fromPublisher(userDao.saveReactive(user))
    override fun findByIdAsync(id: UUID): Maybe<User> = Maybe.fromPublisher(userDao.findByIdReactive(id))
    override fun findByEmailAsync(email: String): Maybe<User> = Flowable.fromPublisher(userDao.findByEmailReactive(email)).firstElement()
    override fun deleteById(id: UUID): Completable = Completable.fromPublisher(userDao.deleteByIdReactive(id))
    override fun deleteByIdIfExists(id: UUID): Single<Boolean> = Single.fromPublisher(userDao.deleteByIdIfExistsReactive(id))
}
