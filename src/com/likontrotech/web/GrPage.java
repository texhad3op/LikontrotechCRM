package com.likontrotech.web;

import org.apache.wicket.markup.html.WebPage;

public class GrPage extends WebPage {

	public GrPage() {
		
//        JFreeChart ff = createChart(createDataset());
//        BufferedImage bi = ff.createBufferedImage(700, 500);
//		RequestCycle.get().getResponse().setContentType("image/png");
//		OutputStream os = RequestCycle.get().getResponse().getOutputStream();
//		
//
//		try {
//			ImageIO.write(bi, "png", os);
//			os.close();			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

//    private static JFreeChart createChart(IntervalXYDataset intervalxydataset)   
//    {   
//        JFreeChart jfreechart = ChartFactory.createXYBarChart("йцукенгąčęėįšųžExecutions - USA", "Year", true, "Number of People", intervalxydataset, PlotOrientation.VERTICAL, true, false, false);   
//        jfreechart.addSubtitle(new TextTitle("Source: http://www.amnestyusa.org/abolish/listbyyear.do"));   
//        jfreechart.setBackgroundPaint(Color.white);   
//        XYPlot xyplot = jfreechart.getXYPlot();   
//        //XYItemRenderer xyitemrenderer = xyplot.getRenderer();   
////        StandardXYToolTipGenerator standardxytooltipgenerator = new StandardXYToolTipGenerator("{1} = {2}", new SimpleDateFormat("yyyy"), new DecimalFormat("0"));   
////        xyitemrenderer.setToolTipGenerator(standardxytooltipgenerator);   
//        xyplot.setBackgroundPaint(Color.lightGray);   
//        xyplot.setRangeGridlinePaint(Color.white);   
//        DateAxis dateaxis = (DateAxis)xyplot.getDomainAxis();   
//        dateaxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);   
//        dateaxis.setLowerMargin(0.01D);   
//        dateaxis.setUpperMargin(0.01D);   
//        return jfreechart;   
//    }   
//   
//    private static IntervalXYDataset createDataset()   
//    {   
//        //TimeSeries timeseries = new TimeSeries("Executions", "Year", "Count", class$org$jfree$data$time$Year != null ? class$org$jfree$data$time$Year : (class$org$jfree$data$time$Year = class$("org.jfree.data.time.Year")));   
//        TimeSeries timeseries = new TimeSeries("Executions", "Year", "Count", org.jfree.data.time.Month.class);
//        
//        try   
//        {   
//          timeseries.add(new Month(1,2010), new Integer(15));   
//          timeseries.add(new Month(2,2010), new Integer(12));            
//          timeseries.add(new Month(3,2010), new Integer(17));   
//          timeseries.add(new Month(4,2010), new Integer(32)); 
//          timeseries.add(new Month(5,2010), new Integer(21));   
//          timeseries.add(new Month(6,2010), new Integer(22));         	
//        	
//        	
////            timeseries.add(new Year(1976), new Integer(0));   
////            timeseries.add(new Year(1977), new Integer(1));   
////            timeseries.add(new Year(1978), new Integer(0));   
////            timeseries.add(new Year(1979), new Integer(2));   
////            timeseries.add(new Year(1980), new Integer(0));   
////            timeseries.add(new Year(1981), new Integer(1));   
////            timeseries.add(new Year(1982), new Integer(2));   
////            timeseries.add(new Year(1983), new Integer(5));   
////            timeseries.add(new Year(1984), new Integer(21));   
////            timeseries.add(new Year(1985), new Integer(18));   
////            timeseries.add(new Year(1986), new Integer(18));   
////            timeseries.add(new Year(1987), new Integer(25));   
////            timeseries.add(new Year(1988), new Integer(11));   
////            timeseries.add(new Year(1989), new Integer(16));   
////            timeseries.add(new Year(1990), new Integer(23));   
////            timeseries.add(new Year(1991), new Integer(14));   
////            timeseries.add(new Year(1992), new Integer(31));   
////            timeseries.add(new Year(1993), new Integer(38));   
////            timeseries.add(new Year(1994), new Integer(31));   
////            timeseries.add(new Year(1995), new Integer(56));   
////            timeseries.add(new Year(1996), new Integer(45));   
////            timeseries.add(new Year(1997), new Integer(74));   
////            timeseries.add(new Year(1998), new Integer(68));   
////            timeseries.add(new Year(1999), new Integer(98));   
////            timeseries.add(new Year(2000), new Integer(85));   
////            timeseries.add(new Year(2001), new Integer(66));   
////            timeseries.add(new Year(2002), new Integer(71));   
////            timeseries.add(new Year(2003), new Integer(65));   
//        }   
//        catch(Exception exception)   
//        {   
//            System.err.println(exception.getMessage());   
//        }   
//        TimeSeriesCollection timeseriescollection = new TimeSeriesCollection(timeseries);   
//        timeseriescollection.setDomainIsPointsInTime(false);   
//        return timeseriescollection;   
//    }   


}
