package com.blazemhan.gptoffice.service;

import com.blazemhan.gptoffice.entity.*;
import com.blazemhan.gptoffice.repository.SupplyItemRepository;
import com.blazemhan.gptoffice.repository.SupplyRequestRepository;
import com.blazemhan.gptoffice.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SupplyRequestService {
        private final SupplyRequestRepository requestRepository;
        private final SupplyItemRepository itemRepository;
        private final UserRepository userRepository;

        public SupplyRequestService(SupplyRequestRepository requestRepository,
                                    SupplyItemRepository itemRepository,
                                    UserRepository userRepository) {
            this.requestRepository = requestRepository;
            this.itemRepository = itemRepository;
            this.userRepository = userRepository;
        }

        public SupplyRequest createRequest(Long requesterId, Long itemId, int quantity) {
            User requester = userRepository.findById(requesterId)
                    .orElseThrow(() -> new RuntimeException("Requester not found"));

            SupplyItem item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Item not found"));

            if (quantity > item.getQuantity()) {
                throw new RuntimeException("Requested quantity exceeds stock");
            }

            SupplyRequest request = new SupplyRequest();
            request.setRequester(requester);
            request.setItem(item);
            request.setRequestedQuantity(quantity);
            request.setStatus(RequestStatus.PENDING);
            request.setRequestedAt(LocalDateTime.now());
            return requestRepository.save(request);
        }

        public SupplyRequest approveRequest(Long requestId, Long approverId) {
            SupplyRequest request = requestRepository.findById(requestId)
                    .orElseThrow(() -> new RuntimeException("Request not found"));

            User approver = userRepository.findById(approverId)
                    .orElseThrow(() -> new RuntimeException("Approver not found"));

            if (approver.getRole() != Role.MANAGER) {
                throw new RuntimeException("Only managers can approve requests");
            }

            request.setApprovedBy(approver);
            request.setStatus(RequestStatus.APPROVED);

            return requestRepository.save(request);
        }

    @Transactional
    public SupplyRequest rejectRequest(Long requestId, Long approverId, String reason) {
        SupplyRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Only pending requests can be rejected");
        }

        User approver = userRepository.findById(approverId)
                .orElseThrow(() -> new RuntimeException("Approver not found"));

        if (approver.getRole() != Role.MANAGER) {
            throw new RuntimeException("Only managers can reject requests");
        }

        request.setApprovedBy(approver);
        request.setStatus(RequestStatus.REJECTED);

        // Optionally, store the reason in a log or notification system
        System.out.println("Request rejected: " + reason);

        return requestRepository.save(request);
    }

    @Transactional
    public SupplyRequest  releaseRequest(Long requestId, Long storekeeperId){
            SupplyRequest supplyRequest = requestRepository.findById(requestId).orElseThrow(()
                    -> new RuntimeException("Request not found"));

             if(supplyRequest.getStatus() != RequestStatus.APPROVED){
                 throw new RuntimeException("Only APPROVED requests can be released");
             }

             User storekeeper = userRepository.findById(storekeeperId).orElseThrow(()->
                     new RuntimeException("Storekeeper not found"));

        if (storekeeper.getRole() != Role.STOREKEEPER) {
            throw new RuntimeException("Only storekeepers can release requests");
        }

        supplyRequest.setReleasedBy(storekeeper);
        supplyRequest.setStatus(RequestStatus.RELEASED);
        supplyRequest.setReleasedAt(LocalDateTime.now());

        return requestRepository.save(supplyRequest);
    }
    }






