package mk.ukim.finki.easyfood.model;



import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "delivery_assignment")
@IdClass(DeliveryAssignmentId.class)
public class DeliveryAssignment {

    @Id
    @ManyToOne
    @JoinColumn(name = "deliveryfirm_id")
    private DeliveryFirm deliveryFirm;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private DeliveryMan deliveryMan;


    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;
    // getters and setters
}

