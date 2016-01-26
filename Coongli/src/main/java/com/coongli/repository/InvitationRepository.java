package com.coongli.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coongli.domain.Invitation;


@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Integer > {

	@Query("select i from Invitation i where i.sender.id=?1 order by i.creationMoment desc")
	Collection<Invitation> findSentByUser(long userId);
	
	@Query("select i from Invitation i where i.accepted = false and i.rejected = false and i.recipient.id=?1 order by i.session.startMoment desc")
	Collection<Invitation> findPendingByUser(long userId);
	
	@Query("select i from Invitation i where (i.accepted = true or i.rejected = true) and i.recipient.id=?1 order by i.session.startMoment desc")
	Collection<Invitation> findAnsweredByUser(long userId);

	@Query("select i from Invitation i where i.recipient.id=?1 and i.session.id=?2")
	Collection<Invitation> findInvitationsToSession(long userId,long sessionId);
}