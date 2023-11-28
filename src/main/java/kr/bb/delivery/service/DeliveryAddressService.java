package kr.bb.delivery.service;

import java.util.List;
import java.util.stream.Collectors;
import kr.bb.delivery.dto.response.DeliveryAddressReadResponseDto;
import kr.bb.delivery.entity.DeliveryAddress;
import kr.bb.delivery.repository.DeliveryAddressRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryAddressService {
    private final DeliveryAddressRepository deliveryAddressRepository;

    @Transactional
    public List<DeliveryAddressReadResponseDto> getDeliveryAddress(Long userId){
        List<DeliveryAddress> deliveryAddressList = deliveryAddressRepository.findAllByUserId(userId);
        return deliveryAddressList.stream().map(DeliveryAddressReadResponseDto::fromEntity).collect(
                Collectors.toList());
    }
}
