package app.model.exhibit.watch

import app.model.exhibit.ExhibitModel
import app.utils.SecurityUtils
import com.datastax.oss.driver.api.core.uuid.Uuids
import io.micronaut.core.annotation.Introspected
import io.micronaut.security.authentication.Authentication

@Introspected
class WatchModel(
    name: String? = null,
    var description: String? = null,
    var views: Int = 0,
    var rating: Int = 0,
    private: Boolean = false
): ExhibitModel<Watch>(name, private) {
    override fun convert(authentication: Authentication): Watch? {
        val clientId = SecurityUtils.getClientId(authentication) ?: return null
        return Watch(
            Uuids.timeBased(),
            name,
            clientId,
            description,
            views,
            rating,
            private
        )
    }
}
