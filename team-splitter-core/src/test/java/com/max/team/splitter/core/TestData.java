package com.max.team.splitter.core;

import com.max.team.splitter.core.model.Player;

import java.util.ArrayList;
import java.util.List;

public class TestData {
    public static Player createPlayer(Long id) {
        Player player = new Player(id);
        return player;
    }

    public static Player createPlayer(Long id, Integer score) {
        Player player = createPlayer(id);
        player.setScore(score);

        return player;
    }

    public static Player createPlayer(long id, String firstName, String lastName, Integer score) {
        Player player = createPlayer(id);
        player.setScore(score);
        player.setFirstName(firstName);
        player.setLastName(lastName);

        return player;
    }

    public static Player createPlayer(int id, String firstName, String lastName, Integer score) {
        return createPlayer((long) id, firstName, lastName, score);
    }

    public static List<Player> playersList() {
        List<Player> players = new ArrayList<>();
        
        players.add(createPlayer(949248180,"Sergii","Rodik", 85));
        players.add(createPlayer(355281005,"Maksym","Mukhanov", 82));
        players.add(createPlayer(5231027737L,"Yaroslav","Voznenko", 77));
        players.add(createPlayer(1651967319,"Pavel","Labusov", 75));
        players.add(createPlayer(1644965644,"Max","Soloviov", 74));
        players.add(createPlayer(1678108918,"Kostya","Medovyi", 72));
        players.add(createPlayer(602917870,"Макс","Родионов",70));
        players.add(createPlayer(559180399,"Nikita","Golowin", 70));
        players.add(createPlayer(5403262522L,"Oleksandr","Tatura", 69));
        players.add(createPlayer(308729382,"Vlad","", 69));
        players.add(createPlayer(637649763,"Nazar","", 69));
        players.add(createPlayer(1912027184,"Max","Kovalev", 67));
        players.add(createPlayer(1554399761,"Alex","Butinov", 67));
        players.add(createPlayer(589341508,"David","Eliyahu", 66));
        players.add(createPlayer(2042510450,"Michael","Iskander", 66));
        players.add(createPlayer(5155590390L,"Andrew","NULL", 65));
        players.add(createPlayer(1187354326,"Vi","Ly", 65));
        players.add(createPlayer(5223527345L,"Max","Stepanskiy", 65));
        players.add(createPlayer(1800014655,"Egor","", 62));
        players.add(createPlayer(681468857,"Igor","Petrenko", 60));
        players.add(createPlayer(1604379312,"Alexei","Anisimov", 60));
        players.add(createPlayer(1811855761,"Roman","Bukreev", 60));
        players.add(createPlayer(5184467820L,"Alexander","Nazar’s dad",58));
        players.add(createPlayer(5077590840L,"Beny","", 57));
        players.add(createPlayer(6021110114L,"Oleksandr","", 57));
        players.add(createPlayer(725885281,"Max","Fralov", 56));
        players.add(createPlayer(1362681125,"Anton","", 55));
        players.add(createPlayer(5376693539L,"Igor","Zelenyy", 45));

        return players;
    }
}
