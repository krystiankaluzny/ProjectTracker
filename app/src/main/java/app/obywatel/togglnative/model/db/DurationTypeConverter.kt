package app.obywatel.togglnative.model.db

import com.raizlabs.android.dbflow.converter.TypeConverter
import org.threeten.bp.Duration

@com.raizlabs.android.dbflow.annotation.TypeConverter
class DurationTypeConverter : TypeConverter<Long, Duration>() {

    override fun getDBValue(model: Duration?): Long? = model?.seconds

    override fun getModelValue(data: Long?): Duration? = data?.let { Duration.ofSeconds(data) }
}