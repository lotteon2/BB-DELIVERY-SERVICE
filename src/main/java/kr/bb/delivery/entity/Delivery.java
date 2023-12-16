package kr.bb.delivery.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kr.bb.delivery.dto.request.DeliveryUpdateRequestDto;
import kr.bb.delivery.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@Getter
@Entity
@Table(name = "delivery")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "delivery_id")
  private Long deliveryId;

  @Column(name = "delivery_tracking_number", unique = true)
  private String deliveryTrackingNumber;

  @Column(name = "delivery_orderer_name", nullable = false)
  private String deliveryOrdererName;

  @Column(name = "delivery_orderer_phone_number", nullable = false)
  private String deliveryOrdererPhoneNumber;

  @Column(name = "delivery_orderer_email", nullable = false)
  @Check(constraints = "delivery_orderer_email LIKE '%@%'")
  private String deliveryOrdererEmail;

  @Column(name = "delivery_recipient_name", nullable = false)
  private String deliveryRecipientName;

  @Column(name = "delivery_road_name", nullable = false)
  private String deliveryRoadName;

  @Column(name = "delivery_address_detail", nullable = false)
  private String deliveryAddressDetail;

  @Column(name = "delivery_zipcode", nullable = false)
  private String deliveryZipcode;

  @Column(name = "delivery_recipient_phone_number", nullable = false)
  private String deliveryRecipientPhoneNumber;

  @Column(name = "delivery_request")
  private String deliveryRequest;

  @Column(name = "delivery_cost", nullable = false)
  private Long deliveryCost;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(name = "delivery_status", nullable = false)
  private DeliveryStatus deliveryStatus = DeliveryStatus.INITIAL;

  public void modifyDeliveryInfo(DeliveryUpdateRequestDto dto){
    this.deliveryRecipientName = dto.getRecipientName();
    this.deliveryRecipientPhoneNumber = dto.getRecipientPhoneNumber();
    this.deliveryZipcode = dto.getZipcode();
    this.deliveryRoadName = dto.getRoadName();
    this.deliveryAddressDetail = dto.getAddressDetail();
  }

  public void modifyStatus(String status){
    this.deliveryStatus = DeliveryStatus.valueOf(status);
  }

  public void generateTrackingNumber(String trackingNumber){this.deliveryTrackingNumber = trackingNumber;}
}
