package agprojects.blackjack.controllers;

import agprojects.blackjack.utilities.CustomModelMapper;
import agprojects.blackjack.models.dto.PlayerDTO;
import agprojects.blackjack.services.PlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    PlayerServiceImpl playerService;

    @Autowired
    CustomModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<PlayerDTO>> getAllPlayers(){
        List<PlayerDTO> playerDTOList = playerService.getAllPlayers()
                .stream().map(player -> modelMapper.getModelMapper()
                        .map(player, PlayerDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(playerDTOList,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PlayerDTO> createNewPlayer(@RequestBody PlayerDTO player){
        PlayerDTO playerDTO = modelMapper.convertFromPlayer(playerService.createNewPlayer(player));
        return new ResponseEntity<>(playerDTO, HttpStatus.OK);
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable int playerId){
        PlayerDTO playerDTO = modelMapper.convertFromPlayer(playerService.getPlayerById(playerId));
        return new ResponseEntity<>(playerDTO,HttpStatus.OK);
    }


    @PutMapping("/bet/{playerId}/{playerBet}")
    public ResponseEntity<PlayerDTO> placeBetByPlayerId(@PathVariable int playerId, @PathVariable double playerBet) {
        PlayerDTO playerDTO = modelMapper.convertFromPlayer(playerService.placeBet(playerId,playerBet));
        return new ResponseEntity<>(playerDTO, HttpStatus.OK);
    }

    @PutMapping("/{playerId}/balance/{playerBalance}")
    public ResponseEntity<PlayerDTO> addBalanceToPlayer(@PathVariable int playerId, @PathVariable int playerBalance) {
        PlayerDTO playerDTO = modelMapper.convertFromPlayer(playerService.addBalanceToPlayer(playerId,playerBalance));
        return new ResponseEntity<>(playerDTO, HttpStatus.OK);
    }

    @PutMapping("/seat/{playerId}/{playerSeat}")
    public ResponseEntity<PlayerDTO> sitPlayer(@PathVariable int playerId, @PathVariable int playerSeat) {
        PlayerDTO playerDTO = modelMapper.convertFromPlayer(playerService.seatPlayer(playerId,playerSeat));
        return new ResponseEntity<>(playerDTO, HttpStatus.OK);
    }

    @PutMapping("/{action}/{playerId}/{handId}")
    public ResponseEntity<PlayerDTO> executePlayerAction(@PathVariable String action, @PathVariable int playerId, @PathVariable int handId) {
        PlayerDTO playerDTO = modelMapper.convertFromPlayer(playerService.executeAction(action,playerId,handId));
        return new ResponseEntity<>(playerDTO, HttpStatus.OK);
    }

}
