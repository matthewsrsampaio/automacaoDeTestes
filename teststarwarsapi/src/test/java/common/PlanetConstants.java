package common;

import java.util.ArrayList;
import java.util.List;

import com.example.teststarwarsapi.domain.Planet;

public class PlanetConstants {
    public static final Planet PLANET = new Planet("name", "climate", "terrain");
    public static final Planet INVALID_PLANET = new Planet("", "", "");

    public static final Planet TATOOINE = new Planet(1L, "Tatooine", "arid", "desert");
    public static final Planet ALDERAAN = new Planet(2L, "Alderaan", "temperate", "grasslands, mountains");
    public static final Planet YAVIN_IV = new Planet(3L, "Yavin IV", "temperate, tropical", "jungle, rainforests");
    public static final Planet HOTH = new Planet(4L, "Hoth", "frozen", "tundra, ice caves, mountain ranges");
    public static final Planet DAGOBAR = new Planet(5L, "Dagobah", "murky", "swamp, jungles");
    public static final Planet BESPIN = new Planet(6L, "Bespin", "temperate", "gas giant");
    public static final Planet ENDOR = new Planet(7L, "Endor", "temperate", "forests, mountains, lakes");
    public static final Planet NABOO = new Planet(8L, "Naboo", "temperate", "grassy hills, swamps, forests, mountains");
    public static final Planet CORUSCANT = new Planet(9L, "Coruscant", "temperate", "cityscape, mountains");
    public static final Planet KAMINO = new Planet(10L, "Kamino", "temperate", "ocean");
    public static final Planet GEONOSIS = new Planet(11L, "Geonosis", "temperate, arid", "rock, desert, mountain, barren");
    public static final List<Planet> PLANETS = new ArrayList<>() {
        {
            add(TATOOINE);
            add(ALDERAAN);
            add(YAVIN_IV);
            add(HOTH);
            add(DAGOBAR);
            add(BESPIN);
            add(ENDOR);
            add(NABOO);
            add(CORUSCANT);
            add(KAMINO);
            add(GEONOSIS);
        }
    };
}
