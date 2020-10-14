package agprojects.blackjack.repositories;

import agprojects.blackjack.models.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

/**
 * Repository class for all Cards.
 */
@Repository
public interface CardRepository extends JpaRepository<Card,Integer> {
}
