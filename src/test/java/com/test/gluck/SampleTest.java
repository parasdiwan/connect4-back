package com.test.gluck;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.gluck.dto.GameStateDto;
import com.gluck.service.AppService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { UnitTestConfig.class }, loader = AnnotationConfigContextLoader.class)
public class SampleTest {

    private @Autowired AppService appService;
    private GameStateDto gsDto = new GameStateDto();
    private Long p1Id, p2Id, gameId;

    @Before
    public void prepareTestData() {
        p1Id = appService.createPlayer("Some guy");
        p2Id = appService.createPlayer("Other guy");
        gsDto = appService.startGame(p1Id.toString(), p2Id.toString());
        gameId = gsDto.getGameId();
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidColumnValue() {
        appService.nextTurn(gameId, gsDto.getNextPlayer(), 22);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidPlayerTurn() {
        Long player = gsDto.getNextPlayer();
        Long invalidPlayer = player.equals(p1Id) ? p2Id : p1Id;
        gsDto = appService.nextTurn(gameId, invalidPlayer, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void columnFilledTest() {
        while (true) {
            Long player = gsDto.getNextPlayer();
            appService.nextTurn(gameId, player, 1);
        }
    }
}
