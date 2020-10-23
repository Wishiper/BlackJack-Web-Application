package agprojects.blackjack.models.dto;

import agprojects.blackjack.models.Dealer;
import agprojects.blackjack.models.Hand;
import agprojects.blackjack.models.Player;
import agprojects.blackjack.models.card.Card;
import agprojects.blackjack.models.card.CardType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomModelMapperTest {

    CustomModelMapper modelMapper = new CustomModelMapper();

    Dealer dealer = new Dealer();



    @Test
    void convertFromPlayerDtoToPlayer_ShouldBeCorrect() {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setPlayerId(1);
        playerDTO.setName("name");
        playerDTO.setBalance(100);
        playerDTO.setBet(50);
        Hand hand = new Hand();
        hand.addCard(dealer.draw());
        hand.addCard(dealer.draw());
        List<Hand> hands = new ArrayList<>();
        hands.add(hand);
        playerDTO.setHands(hands);


        Player player = modelMapper.convertFromPlayerDTO(playerDTO);
        assertEquals(playerDTO.getPlayerId(),player.getPlayerId());
        assertEquals(playerDTO.getName(),player.getName());
        assertEquals(playerDTO.getBalance(),player.getBalance());
        assertEquals(playerDTO.getBet(),player.getBet());
        assertEquals(playerDTO.getHands().get(0).getCardsInHand().get(0).getType(), player.getHands().get(0).getCardsInHand().get(0).getType());
        assertEquals(playerDTO.getHands().get(0).getCardsInHand().get(1).getType(), player.getHands().get(0).getCardsInHand().get(1).getType());

    }

    @Test
    void convertFromPlayerToPlayerDTO_ShouldBeCorrect() {
        Player player = new Player();
        player.setPlayerId(1);
        player.setName("name");
        player.setBalance(100);
        player.setBet(50);
        Hand hand = new Hand();
        hand.addCard(dealer.draw());
        hand.addCard(dealer.draw());
        List<Hand> hands = new ArrayList<>();
        hands.add(hand);
        player.setHands(hands);

        PlayerDTO playerDTO = modelMapper.convertFromPlayer(player);
        assertEquals(player.getPlayerId(),playerDTO.getPlayerId());
        assertEquals(player.getName(),playerDTO.getName());
        assertEquals(player.getBalance(),playerDTO.getBalance());
        assertEquals(player.getBet(),playerDTO.getBet());
        assertEquals(player.getHands().get(0).getCardsInHand().get(0).getType(), playerDTO.getHands().get(0).getCardsInHand().get(0).getType());
        assertEquals(player.getHands().get(0).getCardsInHand().get(1).getType(), playerDTO.getHands().get(0).getCardsInHand().get(1).getType());
    }
}