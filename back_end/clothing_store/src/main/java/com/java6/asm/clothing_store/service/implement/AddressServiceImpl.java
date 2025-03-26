package com.java6.asm.clothing_store.service.implement;

import com.java6.asm.clothing_store.constance.StatusEnum;
import com.java6.asm.clothing_store.dto.request.AddressRequest;
import com.java6.asm.clothing_store.dto.response.AddressResponse;
import com.java6.asm.clothing_store.entity.Address;
import com.java6.asm.clothing_store.entity.User;
import com.java6.asm.clothing_store.exception.AppException;
import com.java6.asm.clothing_store.exception.ErrorCode;
import com.java6.asm.clothing_store.repository.AddressRepository;
import com.java6.asm.clothing_store.repository.UserRepository;
import com.java6.asm.clothing_store.service.AddressService;
import lombok.AllArgsConstructor;
import org.apache.hc.core5.reactor.IOSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public AddressResponse createAddress(AddressRequest request) {

        User user = userRepository.findByEmailAndStatus(request.getEmail(), StatusEnum.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Address address = new Address();
        address.setAddressLine(request.getAddressLine());
        address.setWard(request.getWard());
        address.setDistrict(request.getDistrict());
        address.setCity(request.getCity());
        address.setIsDefault(request.getIsDefault() != null ? request.getIsDefault() : false);
        address.setUser(user);

        Address savedAddress = addressRepository.save(address);

        AddressResponse response = getAddressResponse(savedAddress);

        return response;
    }

    private static AddressResponse getAddressResponse(Address savedAddress) {
        AddressResponse response = new AddressResponse();
        response.setId(savedAddress.getId());
        response.setAddressLine(savedAddress.getAddressLine());
        response.setWard(savedAddress.getWard());
        response.setDistrict(savedAddress.getDistrict());
        response.setCity(savedAddress.getCity());
        response.setStatus(savedAddress.getStatus());
        response.setIsDefault(savedAddress.getIsDefault());
        response.setEmail(savedAddress.getUser().getEmail()); // Trả về email
        return response;
    }
}