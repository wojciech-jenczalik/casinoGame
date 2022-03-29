package pl.jenczalik.casinogame.adapters.api;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
class GameEndpoint {
    private final GameFacade gameFacade;

    @GetMapping("/{gameId}")
    GameHistoryResponse getGameHistory(@PathVariable UUID gameId) {
        return gameFacade.getGameHistory(gameId);
    }

    @PostMapping
    GameStateResponse startGame(@RequestBody GameStartRequest request) {
        return gameFacade.startGame(request);
    }

    @PostMapping("/{gameId}")
    GameStateResponse playRound(@PathVariable UUID gameId, @RequestBody PlayRoundRequest request) {
        return gameFacade.playRound(gameId, request);
    }
}
