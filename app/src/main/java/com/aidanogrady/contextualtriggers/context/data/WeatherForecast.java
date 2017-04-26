package com.aidanogrady.contextualtriggers.context.data;

/**
 * The WeatherForecast is an abstraction of results obtained from any external weather data source.
 * Any implementation of a weather data source should convert the external source's classification
 * into these values.
 *
 * @author Aidan O'Grady
 */
public enum WeatherForecast {
    CLEAR("clear"),
    CLOUDY("cloudy"),
    DRIZZLE("drizzle"),
    RAIN("rain"),
    SNOW("snow"),
    THUNDERSTORM("thunderstorm");

    /**
     * The representation of the enumerator to be used in string representation.
     */
    private final String mDescription;

    /**
     * Creates a new WeatherForecast instance with the given description.
     *
     * @param description  the description of the enumerator value.
     */
    WeatherForecast(String description) {
        this.mDescription = description;
    }

    @Override
    public String toString() {
        return mDescription;
    }
}
