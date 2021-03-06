package agprojects.blackjack.services;

import agprojects.blackjack.exceptions.ApiRequestException;
import agprojects.blackjack.models.Player;
import agprojects.blackjack.models.Table;
import agprojects.blackjack.models.dto.PlayerDTO;
import agprojects.blackjack.repositories.PlayerRepository;
import agprojects.blackjack.services.base.HandService;
import agprojects.blackjack.utilities.CustomModelMapper;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    CustomModelMapper modelMapper;

    @Mock
    HandService handService;

    @Mock
    Table table;

    @InjectMocks
    private static final PlayerServiceImpl playerService = new PlayerServiceImpl();

    @Before
    public void createMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createNewPlayer_ShouldCallPlayerRepositorySaveOnce_WithTheCreatedPlayerFromModelMapper() {

        PlayerDTO playerDTO = new PlayerDTO();

        Player player = new Player();
        player.setPlayerId(1);
        player.setName("name");

        when(modelMapper.convertFromPlayerDTO(playerDTO)).thenReturn(player);

        playerService.createNewPlayer(playerDTO);

        verify(playerRepository,times(1)).save(player);
    }

    @Test
    void getPlayerById_ShouldRetrieveTheRightPlayerFromPlayerRepository() {
        int playerId = 1;

        Player player = new Player();
        player.setPlayerId(playerId);
        player.setName("name");

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        Player playerResult = playerService.getPlayerById(playerId);

        Assert.assertEquals(player,playerResult);
    }

    @Test
    void getPlayerById_ShouldThrowApiRequestException_WhenPlayerIsNotFound() throws ApiRequestException {
        int playerId = 1;

        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ApiRequestException.class, () -> playerService.getPlayerById(playerId));

        String expectedMessage = String.format(PlayerServiceImpl.PLAYER_NOT_FOUND,playerId);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getAllPlayers_ShouldCallPlayerRepositoryFindAllOnce() {
        when(playerRepository.findAll()).thenReturn(Lists.emptyList());

        playerService.getAllPlayers();

        verify(playerRepository,times(1)).findAll();
    }

    @Test
    void placeBet_ShouldSetPlayerBetCorrect_WhenPlayerHasEnoughBalance() {
        double playerBet = 225.0;
        int playerId = 1;
        Player player = new Player();
        player.setPlayerId(1);
        player.setName("name");
        player.setBalance(225);

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        Player resultPlayer = playerService.placeBet(playerId,playerBet);

        verify(playerRepository,times(1)).save(player);
        assertEquals(playerBet,resultPlayer.getBet());
    }

    @Test
    void placeBet_ShouldThrowNOT_ENOUGH_BALANCE_WhenPlayerHasNotEnoughBalance() {
        double playerBet = 225.0;
        int playerId = 1;
        Player player = new Player();
        player.setPlayerId(1);
        player.setName("name");
        player.setBalance(100);

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        Exception exception = assertThrows(ApiRequestException.class, () -> playerService.placeBet(playerId,playerBet));

        String expectedMessage = String.format(PlayerServiceImpl.NOT_ENOUGH_BALANCE,playerId);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void addBalanceToPlayer_ShouldAddTheCorrectValueToPlayerBalance() {

        int playerBalance = 100;
        int playerCurrentBalance = 50;
        int playerId = 1;
        Player player = new Player();
        player.setPlayerId(1);
        player.setName("name");
        player.setBalance(playerCurrentBalance);

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        Player resultPlayer = playerService.addBalanceToPlayer(playerId,playerBalance);

        verify(playerRepository,times(1)).save(player);
        assertEquals(playerCurrentBalance+playerBalance,resultPlayer.getBalance());
    }

    @Test
    void addBalanceToPlayer_ShouldThrowCANNOT_ADD_NEGATIVE_BALANCE_WhenBalanceToAddIsNegativeNumber() {

        int playerBalanceToAdd = -100;
        int playerCurrentBalance = 50;
        int playerId = 1;
        Player player = new Player();
        player.setPlayerId(1);
        player.setName("name");
        player.setBalance(playerCurrentBalance);

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        Exception exception = assertThrows(ApiRequestException.class, () -> playerService.addBalanceToPlayer(playerId,playerBalanceToAdd));

        String expectedMessage = String.format(PlayerServiceImpl.CANNOT_ADD_NEGATIVE_BALANCE,playerBalanceToAdd);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void seatPlayer() {
        int playerId = 1;
        int playerSeat = 1;
        Player player = new Player();
        player.setPlayerId(1);
        player.setName("name");

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(table.sitPlayer(playerSeat,player)).thenReturn("Player " + player.getName() + " has been seated successfully");

        playerService.seatPlayer(playerId,playerSeat);

        verify(table,times(1)).sitPlayer(playerSeat,player);
        verify(playerRepository,times(1)).save(player);
    }

    @Test
    void hit_ShouldCallHandServiceHitAndPlayerRepositorySaveOnce_WithTheCorrectPlayer() {
        int playerId = 1;
        int handId = 1;
        Player player = new Player();
        player.setPlayerId(1);
        player.setName("name");

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        playerService.hit(playerId,handId);

        verify(handService,times(1)).hit(player,handId);
        verify(playerRepository,times(1)).save(player);

    }

    @Test
    void doubleDownShouldCallHandServiceDoubleAndPlayerRepositorySaveOnce_WithTheCorrectPlayer() {
        int playerId = 1;
        int handId = 1;
        Player player = new Player();
        player.setPlayerId(1);
        player.setName("name");

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        playerService.doubleDown(playerId,handId);

        verify(handService,times(1)).doubleDown(player,handId);
        verify(playerRepository,times(1)).save(player);
    }

    @Test
    void stand_ShouldCallHandServiceStandAndPlayerRepositorySaveOnce_WithTheCorrectPlayer() {
        int playerId = 1;
        int handId = 1;
        Player player = new Player();
        player.setPlayerId(1);
        player.setName("name");

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        playerService.stand(playerId,handId);

        verify(handService,times(1)).stand(player,handId);
        verify(playerRepository,times(1)).save(player);
    }

    @Test
    void split_ShouldCallHandServiceSplitAndPlayerRepositorySaveOnce_WithTheCorrectPlayer() {
        int playerId = 1;
        int handId = 1;
        Player player = new Player();
        player.setPlayerId(1);
        player.setName("name");

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        playerService.split(playerId,handId);

        verify(handService,times(1)).split(player,handId);
        verify(playerRepository,times(1)).save(player);
    }

    @Test
    void surrender_ShouldCallHandServiceSurrenderAndPlayerRepositorySaveOnce_WithTheCorrectPlayer() {
        int playerId = 1;
        int handId = 1;
        Player player = new Player();
        player.setPlayerId(1);
        player.setName("name");

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        playerService.surrender(playerId,handId);

        verify(handService,times(1)).surrender(player,handId);
        verify(playerRepository,times(1)).save(player);
    }

    @Test
    void executeAction_ShouldCallPlayerServiceHit_WithActionHit() {
        String action = "hit";
        int playerId = 1;
        int handId = 1;
        Player player = new Player();
        player.setPlayerId(1);
        player.setName("name");

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        playerService.executeAction(action,playerId,handId);

        verify(handService,times(1)).hit(player,handId);
    }

    @Test
    void executeAction_ShouldCallPlayerServiceDouble_WithActionDouble() {
        String action = "double";
        int playerId = 1;
        int handId = 1;
        Player player = new Player();
        player.setPlayerId(1);
        player.setName("name");

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        playerService.executeAction(action,playerId,handId);

        verify(handService,times(1)).doubleDown(player,handId);
    }

    @Test
    void executeAction_ShouldCallPlayerServiceSplit_WithActionSplit() {
        String action = "split";
        int playerId = 1;
        int handId = 1;
        Player player = new Player();
        player.setPlayerId(1);
        player.setName("name");

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        playerService.executeAction(action,playerId,handId);

        verify(handService,times(1)).split(player,handId);
    }

    @Test
    void executeAction_ShouldCallPlayerServiceStand_WithActionStand() {
        String action = "stand";
        int playerId = 1;
        int handId = 1;
        Player player = new Player();
        player.setPlayerId(1);
        player.setName("name");

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        playerService.executeAction(action,playerId,handId);

        verify(handService,times(1)).stand(player,handId);
    }

    @Test
    void executeAction_ShouldCallPlayerServiceSurrender_WithActionSurrender() {
        String action = "surrender";
        int playerId = 1;
        int handId = 1;
        Player player = new Player();
        player.setPlayerId(1);
        player.setName("name");

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        playerService.executeAction(action,playerId,handId);

        verify(handService,times(1)).surrender(player,handId);
    }

    @Test
    void executeAction_ShouldThrowACTION_NOT_ALLOWED_WithActionTest() {
        String action = "test";
        int playerId = 1;
        int handId = 1;
        Player player = new Player();
        player.setPlayerId(1);
        player.setName("name");

        Exception exception = assertThrows(ApiRequestException.class, () -> playerService.executeAction(action,playerId,handId));

        String expectedMessage = String.format(PlayerServiceImpl.ACTION_NOT_ALLOWED,action);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}