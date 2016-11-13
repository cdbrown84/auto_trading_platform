/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriyvealgo.utilities;

import com.ib.client.*;
import com.ib.controller.ApiController.IConnectionHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import thriyvealgo.datamanager.IbDataManager;
import thriyvealgo.controllers.RootApplication;
import thriyvealgo.datamanager.IbHistoricalDataThread;
import thriyvealgo.models.Context;
import thriyvealgo.models.HistoricalData;

/**
 *
 * @author Christopher
 */
public class IbConnection implements IConnectionHandler {
	
	public IbConnection(){
		
	}

    //IB Wrapper
    public static IbEWrapperiml wrapper = new IbEWrapperiml();

    //IB Connection
    public static EClientSocket api = new EClientSocket(wrapper);

    //IB Contract
    public Contract contract = new Contract();

    private IbDataManager model = new IbDataManager();

    public static String symbol;
    public static String expiry;
    public static String security;
    public static String currency;
    public static String exchange;
    private int tickidCum = 999;

    public static String startDate = "";
    public static String endDate = "20140903 04:00:00 EST";
    public static String duration = "1 D";
    public static String barSize = "1 min";
    private String whatToShow = "MIDPOINT";
    private int useRTH = 0;
    private int formatDate = 1;

    public static List<Integer> tickid = new ArrayList<>();
    public static List<String> sym = new ArrayList<>();
    public static List<String> exp = new ArrayList<>();
    public static List<String> sec = new ArrayList<>();
    public static List<String> cur = new ArrayList<>();
    public static List<String> exc = new ArrayList<>();

    private Timer timer;
    private String newDate;
    int tickerIdHist = 9999;
    private Date sdfEndDate;
    private Date sdfStartDate;
    private Calendar mainDate;
    
    public IbHistoricalDataThread histThread;
    
    private GuiManager guiM = new GuiManager();

    //Connect to IB
    public void connectTws() {

        Thread thread = new Thread() {
            

            public void run() {

                api.eConnect("127.0.0.1", 7496, 7777);

            }
        };

        thread.start();
        Context.getInstance().setIbConnection(this);

    }

    //Real Time data method which saves records to the database every 5 mins as set in the MainInterface Controller
    public void realTimeDataCapture() {

        //String symbol;
        Thread realTimeCapture = new Thread() {

            @Override
            public void run() {

                if (api.isConnected() == true) {

                    getContracts();
                    IbDataManager.setTickDataCapture();

                    for (int x = 0; x < sym.size(); x++) {

                        contract.m_symbol = sym.get(x).toUpperCase();
                        contract.m_expiry = exp.get(x).toUpperCase();
                        contract.m_secType = sec.get(x).toUpperCase();
                        contract.m_currency = cur.get(x).toUpperCase();
                        contract.m_exchange = exc.get(x).toUpperCase();
                        api.reqMktData(tickid.get(x), contract, "", false, null);

                    }

                } else {
                	guiM.setMainTextView("Not Connected, please reconnect!");
                	
                    
                }

            }
        };
        realTimeCapture.start();

    }
    
    //RealTime data connection with no record or execution
    public void realTimeAction() {

        //String symbol;
        Thread realTime = new Thread() {

            @Override
            public void run() {

                if (api.isConnected() == true) {

                    setContract();

                    api.reqMktData(tickidCum, contract, "", false, null);
                    
                    tickidCum++;
                } else {
                	guiM.setMainTextView("Not Connected, please reconnect!");
                	
                }

            }
        };
        realTime.start();

    }
    
    //RealTime Bar Snapshot data
    public void realTimeBarAction(){
        Thread realTimeBar = new Thread(){
            public void run(){
                 if (api.isConnected() == true) {

                    getContracts();
                    IbDataManager.setTickDataCapture();

                    for (int x = 0; x < sym.size(); x++) {

                        contract.m_symbol = sym.get(x).toUpperCase();
                        contract.m_expiry = exp.get(x).toUpperCase();
                        contract.m_secType = sec.get(x).toUpperCase();
                        contract.m_currency = cur.get(x).toUpperCase();
                        contract.m_exchange = exc.get(x).toUpperCase();
                        api.reqRealTimeBars(tickid.get(x), contract, 5, "TRADES", false, null);
                        

                    }

                } else {

                	guiM.setMainTextView("Not Connected, please reconnect!");
                }
            }
        };
    }

    //Generic Set Active contract method

    public void setContract() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter Symbol");
        try {
            symbol = br.readLine();
            System.out.println("Enter expiry");
            expiry = br.readLine();
            System.out.println("Enter Security Type");
            security = br.readLine();
            System.out.println("Enter Currency");
            currency = br.readLine();
            System.out.println("Enter Exchange");
            exchange = br.readLine();
        } catch (IOException ex) {
            Logger.getLogger(IbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        contract.m_symbol = symbol.toUpperCase();
        contract.m_expiry = expiry.toUpperCase();
        contract.m_secType = security.toUpperCase();
        contract.m_currency = currency.toUpperCase();
        contract.m_exchange = exchange.toUpperCase();

    }

    //Historical data Params set method

    public void setHistoricParams() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.println("Enter start date YYYYMMDD");
            startDate = br.readLine();
            startDate = startDate + " 23:59:00";
            System.out.println("Enter end date YYYYMMDD");
            endDate = br.readLine();
            endDate = endDate + " 23:59:00";
            //System.out.println("Enter duration");
            //duration = br.readLine();

            System.out.println("Enter bar size");
            barSize = br.readLine();
            /*
             System.out.println("Enter what to show");
             whatToShow = br.readLine();
             */

        } catch (IOException ex) {
            Logger.getLogger(IbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Historic data
    public void historicData() {
        timer = new Timer();

        Thread historicData = new Thread() {

            @Override
            public void run() {

                if (api.isConnected() == true) {

                    try {

                        //List<TagValue> chartType = <XYZ>;
                        //setContract();
                        //setHistoricParams();
                    	startDate = startDate + " 23:59:00";
                    	endDate = endDate + " 23:59:00";
                        sdfEndDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss").parse(endDate);
                        sdfStartDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss").parse(startDate);
                        mainDate = Calendar.getInstance();
                        mainDate.setTime(sdfStartDate);
                        
                        contract.m_symbol = symbol.toUpperCase();
                        contract.m_expiry = expiry.toUpperCase();
                        contract.m_secType = security.toUpperCase();
                        contract.m_currency = currency.toUpperCase();
                        contract.m_exchange = exchange.toUpperCase();

                        timer.schedule(new HistoricDataTask(), 2010, 2010);

                    } catch (ParseException ex) {
                        Logger.getLogger(IbConnection.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {

                	guiM.setMainTextView("Not Connected, please reconnect!");
                }
            }
        };

        historicData.start();

    }

    class HistoricDataTask extends TimerTask {

        @Override
        public void run() {
            if (sdfStartDate.before(sdfEndDate)) {
                mainDate.add(Calendar.DAY_OF_MONTH, 1);
                tickerIdHist++;
                sdfStartDate = mainDate.getTime();
                newDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(sdfStartDate);
                //System.out.println(newDate);
                
                Platform.runLater(new Runnable() {
                    public void run() {
                    	Context.getInstance().currentHomeController().setTa("Processing for date: "+newDate.toString());
                       
                   }
                 });
                
                api.reqHistoricalData(tickerIdHist, contract, newDate, duration, barSize, whatToShow, useRTH, formatDate, null);
            } else {
                //histThread = new IbHistoricalDataThread();
                //model.storeToDB(IbConnection.symbol.toLowerCase());
                HistoricalData hd = new HistoricalData();
                hd.setReqNum(2);
                IbEWrapperiml.tmHist.historicalManager(hd);
                timer.cancel();
            }

        }

    }

    // Disconnect from TWS
    public void disconnectTws() {
        api.eDisconnect();
    }

    public RootApplication tester;

    public void getContracts() {

        //Es
        tickid.add(1);
        sym.add("es");
        exp.add("201412");
        sec.add("fut");
        cur.add("usd");
        exc.add("globex");

        //YM
        tickid.add(2);
        sym.add("ym");
        exp.add("201412");
        sec.add("fut");
        cur.add("usd");
        exc.add("ecbot");

        //NQ
        tickid.add(3);
        sym.add("nq");
        exp.add("201412");
        sec.add("fut");
        cur.add("usd");
        exc.add("globex");

        //ZT
        tickid.add(4);
        sym.add("zt");
        exp.add("201412");
        sec.add("fut");
        cur.add("usd");
        exc.add("ecbot");

        //ZF
        tickid.add(5);
        sym.add("zf");
        exp.add("201412");
        sec.add("fut");
        cur.add("usd");
        exc.add("ecbot");

        //MGC
        tickid.add(6);
        sym.add("mgc");
        exp.add("201410");
        sec.add("fut");
        cur.add("usd");
        exc.add("nymex");

        //QG
        tickid.add(7);
        sym.add("qg");
        exp.add("201410");
        sec.add("fut");
        cur.add("usd");
        exc.add("nymex");

        //YC
        tickid.add(8);
        sym.add("yc");
        exp.add("201412");
        sec.add("fut");
        cur.add("usd");
        exc.add("ecbot");

        //YW
        tickid.add(9);
        sym.add("yw");
        exp.add("201412");
        sec.add("fut");
        cur.add("usd");
        exc.add("ecbot");

        //YK
        tickid.add(10);
        sym.add("yk");
        exp.add("201411");
        sec.add("fut");
        cur.add("usd");
        exc.add("ecbot");

        //N225M
        tickid.add(11);
        sym.add("n225m");
        exp.add("201411");
        sec.add("fut");
        cur.add("jpy");
        exc.add("ose.jpn");

        //AUDJPY
        tickid.add(12);
        sym.add("aud");
        exp.add("");
        sec.add("CASH");
        cur.add("jpy");
        exc.add("idealpro");

        //M6E
        tickid.add(13);
        sym.add("m6e");
        exp.add("201412");
        sec.add("fut");
        cur.add("usd");
        exc.add("globex");

        //m6c
        tickid.add(14);
        sym.add("usd");
        exp.add("");
        sec.add("CASH");
        cur.add("cad");
        exc.add("idealpro");

    }

    @Override
    public void connected() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void disconnected() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void accountList(ArrayList<String> list) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void error(Exception e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        final String excep = e.toString();
        
        guiM.setMainTextView(excep);
    }

    @Override
    public void message(int id, int errorCode, String errorMsg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void show(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
