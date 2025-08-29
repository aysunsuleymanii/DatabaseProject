package mk.ukim.finki.easyfood.model;


import java.io.Serializable;
import java.util.Objects;

public class DeliveryAssignmentId implements Serializable {
    private Long deliveryFirm;
    private Long deliveryMan;

    public DeliveryAssignmentId() {}

    public DeliveryAssignmentId(Long deliveryFirm, Long deliveryMan) {
        this.deliveryFirm = deliveryFirm;
        this.deliveryMan = deliveryMan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeliveryAssignmentId)) return false;
        DeliveryAssignmentId that = (DeliveryAssignmentId) o;
        return Objects.equals(deliveryFirm, that.deliveryFirm) && Objects.equals(deliveryMan, that.deliveryMan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deliveryFirm, deliveryMan);
    }
}

