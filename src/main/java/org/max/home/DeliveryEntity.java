package org.max.home;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery")
public class DeliveryEntity {
    @Id
    @Column(name = "delivery_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer deliveryId;
    @Column(name = "order_id")
    private Integer order_id;
    @Column(name = "courier_id")
    private Integer courier_id;
    @Column(name = "date_arrived")
    private String dateArrived;
    @Column(name = "taken")
    private String taken;
    @Column(name = "payment_method")
    private String paymentMethod;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryEntity that = (DeliveryEntity) o;
        return deliveryId == that.deliveryId && Objects.equals(dateArrived, that.dateArrived) && Objects.equals(taken, that.taken) && Objects.equals(paymentMethod, that.paymentMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deliveryId, dateArrived, taken, paymentMethod);
    }
}
