package uk.ac.ed.inf;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.model.LngLatHandler;

import static org.junit.Assert.*;
public class LngLatHandlerTest {
    public static LngLat[] sampleIntCoordinates;
    public static LngLat[] centralAreaCoordinates;

    @BeforeClass
    public static void setUp(){

        LngLat p1 = new LngLat(0,0);
        LngLat p2 = new LngLat(3,2);
        LngLat p3 = new LngLat(4,-1);
        LngLat p4 = new LngLat(0,-4);
        LngLat p5 = new LngLat(-5,-2);
        LngLat p6 = new LngLat(-2,-1);
        LngLat p7 = new LngLat(-2,2);
        sampleIntCoordinates = new LngLat[]{p1,p2,p3,p4,p5,p6,p7};


        LngLat c1 = new LngLat(-3.192473,  55.946233);
        LngLat c2 = new LngLat(-3.192473,  55.942617);
        LngLat c3 = new LngLat(-3.184319,  55.942617);
        LngLat c4 = new LngLat(-3.184319,  55.946233);
        centralAreaCoordinates = new LngLat[]{c1,c2,c3,c4};
    }


    @Test
    public void centralAreaPointAsVertex1(){
        LngLat position = new LngLat(-3.192473,  55.946233);
        NamedRegion region = new NamedRegion("test", centralAreaCoordinates);
        LngLatHandler lngLatHandler = new LngLatHandler();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    @Test
    public void centralAreaPointAsVertex2(){
        LngLat position = new LngLat(-3.192473,  55.942617);
        NamedRegion region = new NamedRegion("test", centralAreaCoordinates);
        LngLatHandler lngLatHandler = new LngLatHandler();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    @Test
    public void centralAreaPointAsVertex3(){
        LngLat position = new LngLat(-3.184319,  55.942617);
        NamedRegion region = new NamedRegion("test", centralAreaCoordinates);
        LngLatHandler lngLatHandler = new LngLatHandler();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

//    @Test
//    public void centralAreaPointOnEdge1(){
//        LngLat position = new LngLat(-3.184319,  55.943222);
//        NamedRegion region = new NamedRegion("test", centralAreaCoordinates);
//        LngLatHandler lngLatHandler = new LngLatHandler();
//        boolean result = lngLatHandler.isInRegion(position, region);
//        assertTrue(result);
//    }

    @Test
    public void centralAreaPointOnEdge2(){
        LngLat position = new LngLat(-3.186319,  55.942617);
        NamedRegion region = new NamedRegion("test", centralAreaCoordinates);
        LngLatHandler lngLatHandler = new LngLatHandler();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);

    }

    @Test
    public void centralAreaPointInside1(){
        LngLat position = new LngLat(-3.187310,  55.944230);
        NamedRegion region = new NamedRegion("test", centralAreaCoordinates);
        LngLatHandler lngLatHandler = new LngLatHandler();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    @Test
    public void centralAreaPointInside2(){
        LngLat position = new LngLat(-3.187310,  55.944000);
        NamedRegion region = new NamedRegion("test", centralAreaCoordinates);
        LngLatHandler lngLatHandler = new LngLatHandler();
        LngLat nextPosition = lngLatHandler.nextPosition(position, 90);
        boolean result = lngLatHandler.isInRegion(nextPosition, region);
        assertTrue(result);
    }


    // Tests with diagram
    @Test
    public void isInRegionWithPointAsVertex1(){
        LngLat position = new LngLat(0,-4);
        NamedRegion region = new NamedRegion("test", sampleIntCoordinates);
        LngLatHandler lngLatHandler = new LngLatHandler();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    @Test
    public void isInRegionWithPointAsVertex2(){
        LngLat position = new LngLat(-2,-1);
        NamedRegion region = new NamedRegion("test", sampleIntCoordinates);
        LngLatHandler lngLatHandler = new LngLatHandler();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    @Test
    public void isInRegionWithPointAsVertex3(){
        LngLat position = new LngLat(3,2);
        NamedRegion region = new NamedRegion("test", sampleIntCoordinates);
        LngLatHandler lngLatHandler = new LngLatHandler();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    @Test
    public void isInRegionWithPointAsVertex4(){
        LngLat position = new LngLat(-5,-2);
        NamedRegion region = new NamedRegion("test", sampleIntCoordinates);
        LngLatHandler lngLatHandler = new LngLatHandler();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    @Test
    public void isInRegionWithPointOnEdge1(){
        LngLat position = new LngLat(-2,0);
        NamedRegion region = new NamedRegion("test", sampleIntCoordinates);
        LngLatHandler lngLatHandler = new LngLatHandler();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    @Test
    public void isInRegionWithPointOnEdge2(){
        LngLat position = new LngLat(-1,1);
        NamedRegion region = new NamedRegion("test", sampleIntCoordinates);
        LngLatHandler lngLatHandler = new LngLatHandler();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    @Test
    public void isInRegionWithPointInside1(){
        LngLat position = new LngLat(0,-2);
        NamedRegion region = new NamedRegion("test", sampleIntCoordinates);
        LngLatHandler lngLatHandler = new LngLatHandler();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    @Test
    public void isInRegionWithPointInside2(){
        LngLat position = new LngLat(3,-1);
        NamedRegion region = new NamedRegion("test", sampleIntCoordinates);
        LngLatHandler lngLatHandler = new LngLatHandler();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    @Test
    public void isInRegionWithPointOutside1(){
        LngLat position = new LngLat(2,4);
        NamedRegion region = new NamedRegion("test", sampleIntCoordinates);
        LngLatHandler lngLatHandler = new LngLatHandler();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertFalse(result);
    }

    @Test
    public void isInRegionWithPointOutside2(){
        LngLat position = new LngLat(-5,-4);
        NamedRegion region = new NamedRegion("test", sampleIntCoordinates);
        LngLatHandler lngLatHandler = new LngLatHandler();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertFalse(result);
    }



}
