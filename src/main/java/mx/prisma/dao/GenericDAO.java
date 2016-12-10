package mx.prisma.dao;

import java.sql.Connection;
import java.sql.SQLException;

import mx.prisma.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;

public class GenericDAO {
	protected Session session = null;

	public GenericDAO() {
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}

	public Connection getConnection() throws SQLException {
		SessionFactoryImplementor sfi = (SessionFactoryImplementor) session.getSessionFactory();
		ConnectionProvider cp = sfi.getConnectionProvider();
		Connection connection = null;
		connection = cp.getConnection();
		return connection;
	}
}

