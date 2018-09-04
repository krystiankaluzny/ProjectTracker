package org.projecttracker.model.db

import com.raizlabs.android.dbflow.converter.TypeConverter
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

@com.raizlabs.android.dbflow.annotation.TypeConverter
class OffsetDateTimeConverter : TypeConverter<String, OffsetDateTime>() {

    companion object {
        val formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    }

    override fun getDBValue(model: OffsetDateTime?): String? = model?.let { formatter.format(model) }

    override fun getModelValue(data: String?): OffsetDateTime? = data?.let { OffsetDateTime.parse(data, formatter) }
}