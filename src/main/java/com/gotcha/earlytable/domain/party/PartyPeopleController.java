package com.gotcha.earlytable.domain.party;

import com.gotcha.earlytable.domain.party.dto.PartyPeopleResponseDto;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import com.gotcha.earlytable.global.enums.Auth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PartyPeopleController {

    private final PartyPeopleService partyPeopleService;

    public PartyPeopleController(PartyPeopleService partyPeopleService) {
        this.partyPeopleService = partyPeopleService;
    }

//    /**
//     *  파티탈퇴 API
//     * @param invitationId
//     * @param userDetails
//     * @return ResponseEntity<String>
//     */
//    @CheckUserAuth(requiredAuthorities = {Auth.USER})
//    @DeleteMapping("receive/invitations/{invitationId}")
//    public ResponseEntity<String> leaveInvitation(@PathVariable Long invitationId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//
//        partyPeopleService.leaveInvitation(invitationId, userDetails.getUser());
//
//        return ResponseEntity.status(HttpStatus.OK).body("떠나기가 완료되었습니다.");
//    }

    /**
     *  파티탈퇴 API
     * @param partyId
     * @param userDetails
     * @return ResponseEntity<String>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @DeleteMapping("receive/invitations/{partyId}")
    public ResponseEntity<String> leaveInvitation2(@PathVariable Long partyId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        partyPeopleService.leaveInvitation2(partyId, userDetails.getUser());

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

        partyPeopleService.exilePartyPeople(partyId, userId, userDetails.getUser());

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

        partyPeopleService.exileAllPartyPeople(partyId,userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body("모두 내보내기가 완료되었습니다.");
    }

    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @GetMapping("party")
    public ResponseEntity<List<PartyPeopleResponseDto>> getPartyPeople(@RequestParam Long partyId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<PartyPeopleResponseDto> dto = partyPeopleService.getPartyPeople(partyId, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(dto);

    }

}
