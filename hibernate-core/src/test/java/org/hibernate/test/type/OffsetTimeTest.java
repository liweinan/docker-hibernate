/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.test.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.dialect.AbstractHANADialect;
import org.hibernate.dialect.MariaDBDialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.type.descriptor.sql.BigIntTypeDescriptor;
import org.hibernate.type.descriptor.sql.TimestampTypeDescriptor;

import org.hibernate.testing.SkipForDialect;
import org.hibernate.testing.SkipLog;
import org.junit.Test;
import org.junit.runners.Parameterized;

/**
 * Tests for storage of LocalTime properties.
 */
public class OffsetTimeTest extends AbstractJavaTimeTypeTest<OffsetTime, OffsetTimeTest.EntityWithOffsetTime> {

	private static class ParametersBuilder extends AbstractParametersBuilder<ParametersBuilder> {
		public ParametersBuilder add(int hour, int minute, int second, int nanosecond, String offset, ZoneId defaultTimeZone) {
			if ( !isNanosecondPrecisionSupported() ) {
				nanosecond = 0;
			}
			return add( defaultTimeZone, hour, minute, second, nanosecond, offset, 1970, 1, 1 );
		}

		public ParametersBuilder addPersistedWithoutHibernate(int yearWhenPersistedWithoutHibernate,
				int monthWhenPersistedWithoutHibernate, int dayWhenPersistedWithoutHibernate,
				int hour, int minute, int second, int nanosecond, String offset, ZoneId defaultTimeZone) {
			if ( !isNanosecondPrecisionSupported() ) {
				nanosecond = 0;
			}
			return add(
					defaultTimeZone, hour, minute, second, nanosecond, offset,
					yearWhenPersistedWithoutHibernate,
					monthWhenPersistedWithoutHibernate, dayWhenPersistedWithoutHibernate
			);
		}
	}

	@Parameterized.Parameters(name = "{1}:{2}:{3}.{4}[{5}] (JDBC write date: {6}-{7}-{8}) {0}")
	public static List<Object[]> data() {
		return new ParametersBuilder()
				.alsoTestRemappingsWithH2( TimeAsTimestampRemappingH2Dialect.class )
				// None of these values was affected by HHH-13266 (JDK-8061577)
				.add( 19, 19, 1, 0, "+10:00", ZONE_UTC_MINUS_8 )
				.add( 19, 19, 1, 0, "+01:30", ZONE_UTC_MINUS_8 )
				.add( 19, 19, 1, 0, "-06:00", ZONE_UTC_MINUS_8 )
				.add( 19, 19, 1, 0, "+10:00", ZONE_PARIS )
				.add( 19, 19, 1, 0, "+01:30", ZONE_PARIS )
				.add( 19, 19, 1, 500, "+01:00", ZONE_PARIS )
				.add( 19, 19, 1, 0, "-08:00", ZONE_PARIS )
				.add( 0, 9, 20, 0, "+00:09:21", ZONE_PARIS )
				.add( 0, 19, 31, 0, "+00:19:32", ZONE_PARIS )
				.add( 0, 19, 31, 0, "+00:19:32", ZONE_AMSTERDAM )
				.skippedForDialects(
						// MySQL/Mariadb cannot store values equal to epoch exactly, or less, in a timestamp.
						Arrays.asList( MySQLDialect.class, MariaDBDialect.class ),
						b -> b
								.add( 0, 0, 0, 0, "+01:00", ZONE_GMT )
								.add( 0, 0, 0, 0, "+00:00", ZONE_GMT )
								.add( 0, 0, 0, 0, "-01:00", ZONE_GMT )
								.add( 0, 0, 0, 0, "+00:00", ZONE_OSLO )
								.add( 0, 0, 0, 0, "+00:19:32", ZONE_AMSTERDAM )
								.addPersistedWithoutHibernate( 1892, 1, 1, 0, 0, 0, 0, "+00:00", ZONE_OSLO )
								.addPersistedWithoutHibernate( 1900, 1, 1, 0, 9, 20, 0, "+00:09:21", ZONE_PARIS )
								.addPersistedWithoutHibernate( 1900, 1, 1, 0, 19, 31, 0, "+00:19:32", ZONE_PARIS )
								.addPersistedWithoutHibernate( 1900, 1, 1, 0, 19, 31, 0, "+00:19:32", ZONE_AMSTERDAM )
								.addPersistedWithoutHibernate( 1600, 1, 1, 0, 0, 0, 0, "+00:19:32", ZONE_AMSTERDAM )
				)
				.build();
	}

	private final int hour;
	private final int minute;
	private final int second;
	private final int nanosecond;
	private final String offset;

	private final int yearWhenPersistedWithoutHibernate;
	private final int monthWhenPersistedWithoutHibernate;
	private final int dayWhenPersistedWithoutHibernate;

	public OffsetTimeTest(EnvironmentParameters env, int hour, int minute, int second, int nanosecond, String offset,
			int yearWhenPersistedWithoutHibernate, int monthWhenPersistedWithoutHibernate, int dayWhenPersistedWithoutHibernate) {
		super( env );
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.nanosecond = nanosecond;
		this.offset = offset;
		this.yearWhenPersistedWithoutHibernate = yearWhenPersistedWithoutHibernate;
		this.monthWhenPersistedWithoutHibernate = monthWhenPersistedWithoutHibernate;
		this.dayWhenPersistedWithoutHibernate = dayWhenPersistedWithoutHibernate;
	}

	@Override
	protected Class<EntityWithOffsetTime> getEntityType() {
		return EntityWithOffsetTime.class;
	}

	@Override
	protected EntityWithOffsetTime createEntityForHibernateWrite(int id) {
		return new EntityWithOffsetTime( id, getOriginalPropertyValue() );
	}

	protected OffsetTime getOriginalPropertyValue() {
		return OffsetTime.of( hour, minute, second, nanosecond, ZoneOffset.of( offset ) );
	}

	@Override
	protected OffsetTime getExpectedPropertyValueAfterHibernateRead() {
		// For some reason, the offset is not stored, so the restored values use the offset from the default JVM timezone.
		if ( TimeAsTimestampRemappingH2Dialect.class.equals( getRemappingDialectClass() ) ) {
			return getOriginalPropertyValue().withOffsetSameLocal( OffsetDateTime.now().getOffset() );
		}
		else {
			// When storing time as java.sql.Time, we only get second precision (not nanosecond)
			return getOriginalPropertyValue().withNano( 0 ).withOffsetSameLocal( OffsetDateTime.now().getOffset() );
		}
	}

	@Override
	protected OffsetTime getActualPropertyValue(EntityWithOffsetTime entity) {
		return entity.value;
	}

	@Override
	protected void setJdbcValueForNonHibernateWrite(PreparedStatement statement, int parameterIndex)
			throws SQLException {
		if ( TimeAsTimestampRemappingH2Dialect.class.equals( getRemappingDialectClass() ) ) {
			statement.setTimestamp(
					parameterIndex,
					new Timestamp(
							yearWhenPersistedWithoutHibernate - 1900,
							monthWhenPersistedWithoutHibernate - 1,
							dayWhenPersistedWithoutHibernate,
							hour, minute, second, nanosecond
					)
			);
		}
		else {
			statement.setTime( parameterIndex, new Time( hour, minute, second ) );
		}
	}

	@Override
	protected Object getExpectedJdbcValueAfterHibernateWrite() {
		if ( TimeAsTimestampRemappingH2Dialect.class.equals( getRemappingDialectClass() ) ) {
			return new Timestamp( 1970 - 1900, 0, 1, hour, minute, second, nanosecond );
		}
		else {
			return new Time( hour, minute, second );
		}
	}

	@Override
	protected Object getActualJdbcValue(ResultSet resultSet, int columnIndex) throws SQLException {
		return resultSet.getTimestamp( columnIndex );
	}

	@Override
	@Test
	public void nativeWriteThenRead() {
		if ( TimeAsTimestampRemappingH2Dialect.class.equals( getRemappingDialectClass() ) &&
				!ZONE_GMT.equals( getDefaultJvmTimeZone() ) ) {
			SkipLog.reportSkip( "OffsetTimeType remapped as timestamp only works reliably with GMT default JVM for nativeWriteThenRead; see HHH-13357" );
			return;
		}
		super.nativeWriteThenRead();
	}

	@Override
	@Test
	@SkipForDialect(value = AbstractHANADialect.class, comment = "HANA seems to return a java.sql.Timestamp instead of a java.sql.Time")
	public void writeThenNativeRead() {
		super.writeThenNativeRead();
	}

	@Override
	@Test
	public void writeThenRead() {
		if ( TimeAsTimestampRemappingH2Dialect.class.equals( getRemappingDialectClass() ) &&
				!ZONE_GMT.equals( getDefaultJvmTimeZone() ) ) {
			SkipLog.reportSkip( "OffsetTimeType remapped as timestamp only works reliably with GMT default JVM for writeThenRead; see HHH-13357" );
			return;
		}
		super.writeThenRead();
	}

	@Entity(name = ENTITY_NAME)
	static final class EntityWithOffsetTime {
		@Id
		@Column(name = ID_COLUMN_NAME)
		private Integer id;

		@Basic
		@Column(name = PROPERTY_COLUMN_NAME)
		private OffsetTime value;

		protected EntityWithOffsetTime() {
		}

		private EntityWithOffsetTime(int id, OffsetTime value) {
			this.id = id;
			this.value = value;
		}
	}

	public static class TimeAsTimestampRemappingH2Dialect extends AbstractRemappingH2Dialect {
		public TimeAsTimestampRemappingH2Dialect() {
			super( Types.TIME, TimestampTypeDescriptor.INSTANCE );
		}
	}

	public static class TimeAsBigIntRemappingH2Dialect extends AbstractRemappingH2Dialect {
		public TimeAsBigIntRemappingH2Dialect() {
			super( Types.TIME, BigIntTypeDescriptor.INSTANCE );
		}
	}
}
