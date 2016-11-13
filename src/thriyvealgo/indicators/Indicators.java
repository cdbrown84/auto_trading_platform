/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriyvealgo.indicators;

import java.util.List;

/**
 *
 * @author Christopher
 */
public class Indicators {

    //Simple Moving Average Indicator
    public double sma(int setting, int ePeriod, List<Double> priceArray) {

        double results = 0;
        double smaSum = 0;

        if (setting < ePeriod) {
            for (int i = 0; i < priceArray.size(); i++) {
                smaSum = smaSum + priceArray.get(i);
            }

            results = smaSum / priceArray.size();

            return results;
        } else {

            results = 0;
            return results;
        }

    }

}
