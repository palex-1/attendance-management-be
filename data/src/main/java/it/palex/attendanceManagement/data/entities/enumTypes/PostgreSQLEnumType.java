package it.palex.attendanceManagement.data.entities.enumTypes;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

@SuppressWarnings("rawtypes")
public class PostgreSQLEnumType extends org.hibernate.type.EnumType {

	private static final long serialVersionUID = 1L;

	public void nullSafeSet(
            PreparedStatement st,
            Object value,
            int index,
            SharedSessionContractImplementor session)
        throws HibernateException, SQLException {
        if(value == null) {
            st.setNull( index, Types.OTHER );
        }
        else {
            st.setObject(
                index,
                value.toString(),
                Types.OTHER
            );
        }
    }
}
