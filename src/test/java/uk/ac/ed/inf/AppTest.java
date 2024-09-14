//package uk.ac.ed.inf;
//
//import static org.junit.Assert.assertEquals;
//
//import org.junit.Test;
//
//import java.io.ByteArrayOutputStream;
//import java.io.PrintStream;
//
//public class AppTest {
//
//    //TODO: Need to do test for invalid status code
//
//    @Test
//    public void testMainWithZeroArguments() {
//        String[] args = new String[]{};
//        App.main(args);
//
//    }
//
//    @Test
//    public void testMainWithOneArguments() {
//        String[] args = new String[]{"2023-01-01"};
//
//
//        App.main(args);
//
//    }
//
//    @Test
//    public void testMainWithTwoValidArguments() {
//        String[] args = new String[]{"2023-01-01", "https://ilp-rest.azurewebsites.net"};
//
//        App.main(args);
//
//
//    }
//
//
//
//    @Test
//    public void testMainWithInvalidDateFormat() {
//        String date = "20-05-2002";
//        String[] args = new String[]{date, "https://ilp-rest.azurewebsites.net"};
//
//        App.main(args);
//
//    }
//
//    @Test
//    public void testMainWithDateAsNull() {
//        String date = null;
//        String[] args = new String[]{date, "https://ilp-rest.azurewebsites.net"};
//
//        App.main(args);
//
//    }
//
//    @Test
//    public void testMainWithDateAsEmptyString() {
//        String date = "";
//        String[] args = new String[]{date, "https://example.com", "generatorCode"};
//
//        App.main(args);
//
//
//    }
//
//    @Test
//    public void testMainWithNullURL() {
//        String[] args = new String[]{"2023-01-01", null};
//
//
//        App.main(args);
//
//
//    }
//
//    @Test
//    public void testMainWithEmptyStringURL() {
//        String[] args = new String[]{"2023-01-01", ""};
//        App.main(args);
//    }
//
//    @Test
//    public void testMainWithInvalidURL() {
//        String[] args = new String[]{"2023-01-01", "invalidURL"};
//        App.main(args);
//    }
//
//    @Test
//    public void testApp(){
//        String[] args = new String[]{"2023-11-01", null};
//        App.main(args);
//    }
//
//    @Test
//    public void testWrongApp(){
//        String[] args = new String[]{"2024-12-12", "https://ilp-rest.azurewebsites.net"};
//        App.main(args);
//    }
//
//
//}
