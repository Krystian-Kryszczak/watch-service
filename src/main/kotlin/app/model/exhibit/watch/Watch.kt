package app.model.exhibit.watch

import app.model.exhibit.Exhibit
import com.datastax.oss.driver.api.mapper.annotations.*
import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.UUID

@Entity
@SchemaHint(targetElement = SchemaHint.TargetElement.TABLE)
data class Watch(
    @PartitionKey
    override var id: UUID? = null,
    override var name: String? = null,
    override var creatorId: UUID? = null,
    var description: String? = null,
    override var views: Int = 0,
    override var rating: Int = 0,
    override var private: Boolean = false
): Exhibit(id, name, creatorId, views, rating, private) {
    @Transient
    @JsonIgnore
    override fun getUrl(): String? = if (id != null) "/watch/$id" else null
}
