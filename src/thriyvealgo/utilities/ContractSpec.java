/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriyvealgo.utilities;

/**
 *
 * @author Christopher
 */
public class ContractSpec {

    private int size = 0;

    public int contractSize(String sym) {

        String symbol = sym.toLowerCase();

        switch (symbol) {
            case "ymusd":
                size = 5;
                break;
            case "esusd":
                //ES Futures *50 contract size
                size = 50;
                break;
            case "none":
                //Futures emini
                //leverage = 10;
                break;
            case "nonnes":
                //Futures bonds
                //leverage = 10;
                break;

            default:
                throw new IllegalArgumentException("Contract not registered: " + symbol);
        }

        return size;
    }

}
