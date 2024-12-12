package com.blazemhan.gptoffice.controller;

import com.blazemhan.gptoffice.entity.SupplyRequest;
import com.blazemhan.gptoffice.service.SupplyRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/supplies")
    public class SupplyRequestController {

        private final SupplyRequestService requestService;

        public SupplyRequestController(SupplyRequestService requestService) {
            this.requestService = requestService;
        }

        @PostMapping("/request")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<?> createRequest(
                @RequestParam("requesterId") Long requesterId,
                @RequestParam("itemId") Long itemId,
                @RequestParam("quantity") int quantity) {

            SupplyRequest request = requestService.createRequest(requesterId, itemId, quantity);
            return ResponseEntity.ok("Request created successfully");
        }

        @PostMapping("/approve")
        @PreAuthorize("hasRole('MANAGER')")
        public ResponseEntity<?> approveRequest(
                @RequestParam("requestId") Long requestId,
                @RequestParam("approverid") Long approverId) {

            SupplyRequest request = requestService.approveRequest(requestId, approverId);
            return ResponseEntity.ok("Request approved by " + request.getApprovedBy().getUsername());
        }

    @PostMapping("/reject")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> rejectRequest(
            @RequestParam Long requestId,
            @RequestParam Long approverId,
            @RequestParam String reason) {

        SupplyRequest request = requestService.rejectRequest(requestId, approverId, reason);
        return ResponseEntity.ok("Request rejected by " + request.getApprovedBy().getUsername());
    }

    @PostMapping("/release")
    @PreAuthorize("hasRole('STOREKEEPER')")
    public ResponseEntity<?> releaseRequest(@RequestParam("requestId") Long requestId,
                                            @RequestParam ("storekeeperid") Long storekeeperId) {
            SupplyRequest request = requestService.releaseRequest(requestId, storekeeperId);

        return ResponseEntity.ok("Request released by " + request.getReleasedBy().getUsername());

    }

    }


