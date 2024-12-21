package org.max.home;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CourierInfoEntityTest extends AbstractTest {
    //CRUD
    @Order(1)
    @Test
    void getCountTableSize() throws SQLException {
        String sql = "SELECT * FROM courier_info";
        Statement stmt = getConnection().createStatement();
        int countTableSize = 0;
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            countTableSize++;
        }
        final Query query = getSession()
                .createSQLQuery("SELECT * FROM courier_info").addEntity(CourierInfoEntity.class);
        Assertions.assertEquals(4, countTableSize);
        Assertions.assertEquals(4, query.list().size());
    }

    @Order(2)
    @Test
    void createNewCourierTest() {
        CourierInfoEntity courierInfoEntityNew = new CourierInfoEntity();
        courierInfoEntityNew.setFirstName("Ivan");
        courierInfoEntityNew.setLastName("Ivanov");
        courierInfoEntityNew.setDeliveryType("bike");
        courierInfoEntityNew.setPhoneNumber("+ 7 999 999 9999");

        Session session = getSession();
        session.beginTransaction();
        Integer id = (Integer) session.save(courierInfoEntityNew);
        session.getTransaction().commit();

        CourierInfoEntity courierInfoEntity = getSession().find(CourierInfoEntity.class, id);

        Assertions.assertEquals("Ivan", courierInfoEntity.getFirstName());
        Assertions.assertEquals("Ivanov", courierInfoEntity.getLastName());
        Assertions.assertEquals("+ 7 999 999 9999", courierInfoEntity.getPhoneNumber());
        Assertions.assertEquals("bike", courierInfoEntity.getDeliveryType());
    }

    @Order(3)
    @Test
    void updateCourierInfoTest() {
        CourierInfoEntity courierInfoEntityOld = getSession().find(CourierInfoEntity.class, 5);
        Assertions.assertEquals("Ivan", courierInfoEntityOld.getFirstName());
        courierInfoEntityOld.setFirstName("Vladimir");

        Session session = getSession();
        session.beginTransaction();
        session.update(courierInfoEntityOld);
        session.getTransaction().commit();

        CourierInfoEntity courierInfoEntityNew = getSession().find(CourierInfoEntity.class, 5);
        Assertions.assertEquals("Vladimir", courierInfoEntityNew.getFirstName());
    }

    @Order(4)
    @Test
    void deleteCourierInfoTest() throws SQLException {
        String sql = "DELETE FROM courier_info WHERE courier_id=5;";
        Statement stmt = getConnection().createStatement();
        int resOperationDelete = stmt.executeUpdate(sql);
        final Query query = getSession()
                .createSQLQuery("SELECT * FROM courier_info").addEntity(CourierInfoEntity.class);
        Assertions.assertEquals(1, resOperationDelete);
        Assertions.assertEquals(4, query.list().size());
    }
}