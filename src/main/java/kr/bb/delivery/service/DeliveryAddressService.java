package kr.bb.delivery.service;

import bloomingblooms.domain.delivery.DeliveryAddressInsertDto;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import kr.bb.delivery.dto.response.DeliveryAddressReadResponseDto;
import kr.bb.delivery.entity.DeliveryAddress;
import kr.bb.delivery.exception.errors.DeliveryAddressEntityNotFoundException;
import kr.bb.delivery.mapper.DeliveryAddressMapper;
import kr.bb.delivery.repository.DeliveryAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryAddressService {
  private final DeliveryAddressRepository deliveryAddressRepository;

  // 배송지 목록을 최근 날짜순으로 정렬한다.
  public List<DeliveryAddressReadResponseDto> getDeliveryAddress(Long userId) {
    List<DeliveryAddress> deliveryAddressList =
        deliveryAddressRepository.findAllByUserId(
            userId);
    return deliveryAddressList.stream()
        .map(DeliveryAddressReadResponseDto::fromEntity)
        .collect(Collectors.toList());
  }

  /** 배송 주소지 생성 API) 배송 주소지 생성 10개 미만이면 주소지를 추가하고, 10개 이상이면 updatedAt이 가장 오래된 row를 새로운 정보로 덮어씌운다. */
  @Transactional
  public void createDeliveryAddress(DeliveryAddressInsertDto dto) {
    long addressCount = deliveryAddressRepository.countByUserId(dto.getUserId());
    if (addressCount == 10) {
      DeliveryAddress oldestAddress =
          deliveryAddressRepository.findOldestByUserId(dto.getUserId(), Pageable.ofSize(1)).stream()
              .findFirst()
              .orElseThrow(DeliveryAddressEntityNotFoundException::new);
      oldestAddress.replaceOldDeliveryAddressInfo(dto);
    } else {
      // 배송지 목록에서 배송지 선택시
      if (dto.getDeliveryAddressId() != null) {
        DeliveryAddress deliveryAddress =
            deliveryAddressRepository
                .findById(dto.getDeliveryAddressId())
                .orElseThrow(EntityNotFoundException::new);
        // 마지막 사용 날짜 갱신
        deliveryAddress.updateTime();
      // 신규 배송지 추가시
      } else {
        deliveryAddressRepository.save(DeliveryAddressMapper.toEntity(dto));
      }
    }
  }
}
