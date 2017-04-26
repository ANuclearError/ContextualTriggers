package com.aidanogrady.contextualtriggers.context.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The weather result is an encapsulation of data obtained from any weather source. It provides
 * details on weather conditions such as temperature, wind or any other information a trigger may
 * wish to use for determining if the weather is sufficient for this trigger.
 *
 * @author Aidan O'Grady
 */
public class WeatherResult implements Parcelable {
    public static final String TAG = "com.aidanogrady.contextualtriggers.WeatherForceast";

    /**
     * The general forecast.
     */
    private WeatherForecast mForecast;

    /**
     * The temperature of the forecast.
     */
    private double mTemperature;

    /**
     * The wind speed of the forecast.
     */
    private double mWindSpeed;

    /**
     * The forecase humidity.
     */
    private double mHumidity;

    public static final Creator<WeatherResult> CREATOR = new Creator<WeatherResult>() {
        @Override
        public WeatherResult createFromParcel(Parcel in) {
            return new WeatherResult(in);
        }

        @Override
        public WeatherResult[] newArray(int size) {
            return new WeatherResult[size];
        }
    };

    private WeatherResult(Parcel in) {
        mForecast = WeatherForecast.valueOf(in.readString());
        mTemperature = in.readDouble();
        mWindSpeed = in.readDouble();
        mHumidity = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mForecast.toString());
        dest.writeDouble(mTemperature);
        dest.writeDouble(mWindSpeed);
        dest.writeDouble(mHumidity);
    }

    /**
     * Returns the generalised forecast of this weather report.
     *
     * @return  weather forecast
     */
    public WeatherForecast getForecast() {
        return mForecast;
    }

    /**
     * Returns the temperature reported by this forecast.
     *
     * @return  temperature
     */
    public double getTemperature() {
        return mTemperature;
    }

    /**
     * Returns the wind speed reported by this forecast.
     *
     * @return  wind speed
     */
    public double getWindSpeed() {
        return mWindSpeed;
    }

    /**
     * Returns the humidity reported by this forecast.
     *
     * @return  humidity
     */
    public double getHumidity() {
        return mHumidity;
    }

    @Override
    public String toString() {
        return "WeatherResult{" +
                "mForecast=" + mForecast +
                ", mTemperature=" + mTemperature +
                ", mWindSpeed=" + mWindSpeed +
                ", mHumidity=" + mHumidity +
                '}';
    }
}
