package org.max.home;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DeliveryEntityTest extends AbstractTest {
    //CRUD
    @Order(1)
    @Test
    void getCountTableSize() throws SQLException {
        String sql = "SELECT * FROM delivery";
        Statement stmt = getConnection().createStatement();
        int countTableSize = 0;
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            countTableSize++;
        }
        final Query query = getSession()
                .createSQLQuery("SELECT * FROM delivery").addEntity(DeliveryEntity.class);
        Assertions.assertEquals(15, countTableSize);
        Assertions.assertEquals(15, query.list().size());
    }

    @Order(2)
    @Test
    void createNewCourierTest() {
        DeliveryEntity delivery = new DeliveryEntity();
        delivery.setOrder_id(100);
        delivery.setCourier_id(100);
        delivery.setTaken("Yes");
        String localDateTime = String.valueOf(LocalDate.now());
        delivery.setDateArrived(localDateTime);
        delivery.setPaymentMethod("Cash");

        Session session = getSession();
        session.beginTransaction();
        Integer id = (Integer) session.save(delivery);
        session.getTransaction().commit();

        DeliveryEntity deliveryEntity = getSession().find(DeliveryEntity.class, id);

        Assertions.assertEquals("Yes", deliveryEntity.getTaken());
        Assertions.assertEquals(localDateTime, deliveryEntity.getDateArrived());
        Assertions.assertEquals("Cash", deliveryEntity.getPaymentMethod());
        Assertions.assertEquals(100, deliveryEntity.getCourier_id());
        Assertions.assertEquals(100, deliveryEntity.getOrder_id());
    }

    @Order(3)
    @Test
    void updateCourierInfoTest() {
        DeliveryEntity delivery = getSession().find(DeliveryEntity.class, 16);
        Assertions.assertEquals(delivery.getPaymentMethod(), "Cash");

        delivery.setPaymentMethod("Card");
        Session session = getSession();
        session.beginTransaction();
        session.update(delivery);
        session.getTransaction().commit();

        DeliveryEntity deliveryAfterUpdate = getSession().find(DeliveryEntity.class, 16);
        Assertions.assertEquals(deliveryAfterUpdate.getPaymentMethod(), "Card");
    }

    @Order(4)
    @Test
    void deleteCourierInfoTest() throws SQLException {
        String sql = "DELETE FROM delivery WHERE delivery_id=16;";
        Statement stmt = getConnection().createStatement();
        int resOperationDelete = stmt.executeUpdate(sql);
        final Query query = getSession()
                .createSQLQuery("SELECT * FROM delivery").addEntity(DeliveryEntity.class);
        Assertions.assertEquals(1, resOperationDelete);
        Assertions.assertEquals(15, query.list().size());
    }
}