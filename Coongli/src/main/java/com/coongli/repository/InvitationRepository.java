package com.coongli.repository;

import com.coongli.domain.Invitation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Invitation entity.
 */
public interface InvitationRepository extends JpaRepository<Invitation,Long> {

    @Query("select invitation from Invitation invitation where invitation.sender.login = ?#{principal.username}")
    List<Invitation> findBySenderIsCurrentUser();

    @Query("select invitation from Invitation invitation where invitation.recipient.login = ?#{principal.username}")
    List<Invitation> findByRecipientIsCurrentUser();

}
