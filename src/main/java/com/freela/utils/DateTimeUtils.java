package com.freela.utils;

import com.freela.exception.ApiException;
import jakarta.inject.Singleton;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Singleton
public class DateTimeUtils {
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxxx");

	public String convertToDate(LocalDate date) {
		if(date == null) {
			return null;
		}
		return DATE_FORMATTER.format(date);
	}

	public LocalDate convertToDate(String date) {
		return convertToDate(date, null);
	}

	public LocalDate convertToDate(String date, ApiException.Source source) {
		if(date == null) {
			return null;
		}

		try{
			return LocalDate.parse(date, DATE_FORMATTER);
		} catch (Exception e) {
			if(source != null) {
				source.setValue(date);
				source.setExpected("Format: " + DATE_FORMATTER);
			}
			throw new ApiException("Invalid date", source);
		}

	}

	public String convertToTime(LocalTime offsetTime) {
		if(offsetTime == null) {
			return null;
		}
		return TIME_FORMATTER.format(offsetTime);
	}

	public LocalTime convertToTime(String time) {
		return convertToTime(time, null);
	}

	public LocalTime convertToTime(String time, ApiException.Source source) {
		if(time == null) {
			return null;
		}

		try{
			return LocalTime.parse(time, TIME_FORMATTER);
		} catch (Exception e) {
			if(source != null) {
				source.setValue(time);
				source.setExpected("Format: " + TIME_FORMATTER);
			}
			throw new ApiException("Invalid time", source);
		}
	}

	public String convertToTimeZone(ZoneId zoneId) {
		if(zoneId == null) {
			return null;
		}

		return zoneId.getId();
	}

	public ZoneId convertToTimeZone(String zoneId) {
		return convertToTimeZone(zoneId, null);
	}

	public ZoneId convertToTimeZone(String zoneId, ApiException.Source source) {
		if(zoneId == null) {
			return null;
		}

		try{
			return ZoneId.of(zoneId);
		} catch (Exception e) {
			if(source != null) {
				source.setValue(zoneId);
				source.setExpected("Valid time zone id");
			}
			throw new ApiException("Invalid time zone", source);
		}
	}

	public ZoneOffset getTimeZoneOffset(ZoneId zoneId, LocalDate date, LocalTime time) {
		if (date == null) {
			date = LocalDate.now();
		}

		if (time == null) {
			time = LocalTime.now();
		}

		return zoneId.getRules().getOffset(LocalDateTime.of(date, time));
	}

	public OffsetDateTime getOffsetDateTime(ZoneId zoneId, LocalDate date, LocalTime time) {
		return OffsetDateTime.of(
				date,
				time,
				getTimeZoneOffset(zoneId, date, time)
		);
	}

	public String getTimeZoneOffsetAsString(ZoneId zoneId, LocalDate date, LocalTime time) {
		return getTimeZoneOffset(zoneId, date, time).toString().replace("Z", "+00:00");
	}

	public String convertDateTime(OffsetDateTime dateTime) {
		if(dateTime == null) {
			return null;
		}

		return DATE_TIME_FORMATTER.format(dateTime);
	}
}
