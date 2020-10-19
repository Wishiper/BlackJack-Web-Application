package agprojects.blackjack.services;

import agprojects.blackjack.models.Player;
import agprojects.blackjack.repositories.PlayerRepository;
import agprojects.blackjack.services.base.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {


    @Autowired
    PlayerRepository playerRepository;

    @Override
    public Player createNewPlayer(Player player) {

        playerRepository.save(player);

        return player;
    }

    @Override
    public void deletePlayerByName(String playerName) {

    }

    @Override
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public void hit() {

    }

    @Override
    public void doubleDown() {

    }

    @Override
    public void stand() {

    }

    @Override
    public void split() {

    }

    public Optional<Player> getPlayerById(int playerId){
        return playerRepository.findById(playerId);
    }
}
