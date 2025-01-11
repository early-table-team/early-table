package com.gotcha.earlytable.domain.party;

import com.gotcha.earlytable.domain.party.dto.InvitationResponseDto;
import com.gotcha.earlytable.domain.party.dto.InvitationStatusDto;
import com.gotcha.earlytable.domain.party.dto.ReceivedInvitationResponseDto;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import com.gotcha.earlytable.global.enums.Auth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InvitationController {

    private final InvitationService invitationService;

    public InvitationController(final InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    /**
     *  초대발생 API
     * @param userId
     * @param reservationId
     * @param userDetails
     * @return ResponseEntity<String>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @PostMapping("/invitations/users/{userId}")
    public ResponseEntity<String> inviteUser(@PathVariable Long userId, @RequestParam Long reservationId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        invitationService.inviteUser(userId,reservationId, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body("초대가 발송 되었습니다.");
    }


    /**
     *  나에게 온 초대 전체조회 API
     * @param userDetails
     * @return ResponseEntity<List<ReceivedInvitationResponseDto>>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @GetMapping("receive/invitations")
    public ResponseEntity<List<ReceivedInvitationResponseDto>> getAllReceiveInvitations(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<ReceivedInvitationResponseDto> responseDtoList = invitationService.getAllReceiveInvitations(userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);

    }

    /**
     *  나에게 온 초대 단건조회 API
     * @param invitationId
     * @param userDetails
     * @return ResponseEntity<ReceivedInvitationResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @GetMapping("receive/invitations/{invitationId}")
    public ResponseEntity<ReceivedInvitationResponseDto> getReceiveInvitation(@PathVariable Long invitationId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ReceivedInvitationResponseDto responseDto = invitationService.getReceiveInvitation(invitationId, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     *  초대 수락/거절 API
     * @param invitationId
     * @param requestDto
     * @param userDetails
     * @return ResponseEntity<ReceivedInvitationResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @PatchMapping("receive/invitations/{invitationId}")
    public ResponseEntity<ReceivedInvitationResponseDto> changeInvitationStatus(@PathVariable Long invitationId, @RequestBody InvitationStatusDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ReceivedInvitationResponseDto responseDto = invitationService.changeInvitationStatus(invitationId, requestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     *  파티탈퇴 API
     * @param invitationId
     * @param userDetails
     * @return ResponseEntity<String>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @DeleteMapping("receive/invitations/{invitationId}")
    public ResponseEntity<String> leaveInvitation(@PathVariable Long invitationId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        invitationService.leaveInvitation(invitationId, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body("떠나기가 완료되었습니다.");
    }


    /**
     *  파티원 1명 추방 API
     * @param partyId
     * @param userId
     * @param userDetails
     * @return ResponseEntity<String>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @DeleteMapping("/party/partyPeople/users/{userId}")
    public ResponseEntity<String> exilePartyPeople(@RequestParam Long partyId, @PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        invitationService.exilePartyPeople(partyId, userId, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body("추방이 완료 되었습니다.");

    }

    /**
     *  파티원 전체추방 API
     * @param partyId
     * @param userDetails
     * @return ResponseEntity<String>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @DeleteMapping("/party/partyPeople/users")
    public ResponseEntity<String> exileAllPartyPeople(@RequestParam Long partyId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        invitationService.exileAllPartyPeople(partyId,userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body("모두 내보내기가 완료되었습니다.");
    }

}
