package com.gluck.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gluck.dto.GameStateDto;
import com.gluck.service.AppService;

@Controller
public class AppController {

    public @Autowired AppService appService;

    @ResponseBody
    @RequestMapping(value = "/home", method = GET)
    @ResponseStatus(OK)
    public Map<String, String> home() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("msg", "Welcome to Gluck Connect Four app");
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/", method = GET)
    @ResponseStatus(OK)
    public Map<String, String> emptyUrl() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("msg", "Welcome to Gluck Connect Four app");
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/start", method = POST)
    @ResponseStatus(OK)
    public GameStateDto startGame(@RequestParam("p1") String player1, @RequestParam("p2") String player2) {
        return appService.startGame(player1, player2);
    }

    @ResponseBody
    @ResponseStatus(OK)
    @RequestMapping(value = "/player", method = POST)
    public Map<String, Object> createPlayer(@RequestParam("name") String name) {
        Long pId = appService.createPlayer(name);
        return getSuccessMap("Player Id", pId);
    }

    @ResponseBody
    @ResponseStatus(OK)
    @RequestMapping(value = "/game/{gameId}/next-turn", method = PUT)
    public GameStateDto nextTurn(@PathVariable("gameId") Long gameId, @RequestParam("player") Long pl,
            @RequestParam("column") int column) {
        return appService.nextTurn(gameId, pl, column);
    }

    @ResponseBody
    @ResponseStatus(OK)
    @RequestMapping(value = "/test", method = GET)
    public void test() {
        appService.test();
    }

    private Map<String, Object> getSuccessMap(String name, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, value);
        map.put("status", "success");
        return map;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("ib-error-code", "cms.invalid.argument.400");

        Map<String, String> result = new HashMap<>();
        result.put("status", "Error, Illegal Argument");
        result.put("Message", e.getMessage());
        ResponseEntity<Map<String, String>> response = new ResponseEntity<>(result, headers,
                BAD_REQUEST);

        return response;

    }

}
