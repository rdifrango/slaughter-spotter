import slick.lifted.MappedTypeMapper
import java.sql.Date
import org.joda.time.Instant
import slick.lifted.TypeMapper.DateTypeMapper

object TimestampMapper {
  implicit def date2dateTime = MappedTypeMapper.base[Instant, Date](
    dateTime => new Date(dateTime.getMillis),
    date => new Instant(date))
}