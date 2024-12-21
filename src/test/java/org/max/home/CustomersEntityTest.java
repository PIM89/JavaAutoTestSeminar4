package org.max.home;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomersEntityTest extends AbstractTest{
    //CRUD
    @Order(1)
    @Test
    void getCountTableSize() throws SQLException {
        String sql = "SELECT * FROM customers";
        Statement stmt = getConnection().createStatement();
        int countTableSize = 0;
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            countTableSize++;
        }
        final Query query = getSession()
                .createSQLQuery("SELECT * FROM customers").addEntity(CustomersEntity.class);
        Assertions.assertEquals(15, countTableSize);
        Assertions.assertEquals(15, query.list().size());
    }

    @Order(2)
    @Test
    void createNewCourierTest() {
        CustomersEntity customersEntity = new CustomersEntity();
        //first_name, last_name, phone_number, district, street, house, apartment
        customersEntity.setFirstName("Ivan");
        customersEntity.setLastName("Ivanov");
        customersEntity.setPhoneNumber("+ 7 999 999 9999");
        customersEntity.setDistrict("Central");
        customersEntity.setStreet("Moskovskaya");
        customersEntity.setHouse("5");
        customersEntity.setApartment("25");

        Session session = getSession();
        session.beginTransaction();
        Integer id = (Integer) session.save(customersEntity);
        session.getTransaction().commit();

        CustomersEntity customer = getSession().find(CustomersEntity.class, id);

        Assertions.assertEquals("Ivan", customer.getFirstName());
        Assertions.assertEquals("Ivanov", customer.getLastName());
        Assertions.assertEquals("+ 7 999 999 9999", customer.getPhoneNumber());
        Assertions.assertEquals("Central", customer.getDistrict());
        Assertions.assertEquals("Moskovskaya", customer.getStreet());
        Assertions.assertEquals("5", customer.getHouse());
        Assertions.assertEquals("25", customer.getApartment());
    }

    @Order(3)
    @Test
    void updateCourierInfoTest() {
        CustomersEntity customersEntity = getSession().find(CustomersEntity.class, 16);

        Assertions.assertEquals("Ivan", customersEntity.getFirstName());
        customersEntity.setFirstName("Vladimir");

        Session session = getSession();
        session.beginTransaction();
        session.update(customersEntity);
        session.getTransaction().commit();

        CustomersEntity customersEntityAfterUpdate = getSession().find(CustomersEntity.class, 16);
        Assertions.assertEquals("Vladimir", customersEntityAfterUpdate.getFirstName());
    }

    @Order(4)
    @Test
    void deleteCourierInfoTest() throws SQLException {
        String sql = "DELETE FROM customers WHERE customer_id=16;";
        Statement stmt = getConnection().createStatement();
        int resOperationDelete = stmt.executeUpdate(sql);
        final Query query = getSession()
                .createSQLQuery("SELECT * FROM customers").addEntity(CustomersEntity.class);
        Assertions.assertEquals(1, resOperationDelete);
        Assertions.assertEquals(15, query.list().size());
        Assertions.assertNull(getSession().find(CustomersEntity.class, 16));
    }
}