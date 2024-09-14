package uk.ac.ed.inf.model;

import org.junit.Test;
import static org.junit.Assert.*;

import uk.ac.ed.inf.ilp.data.CreditCardInformation;

import java.time.LocalDate;

public class CreditCardTest {
    // Credit Card Number Tests
    @Test
    public void nullCreditCardNumber(){
        CreditCardInformation creditCardInformation = new CreditCardInformation(null, "12/22", "123");
        OrderValidator orderValidator = new OrderValidator();
        boolean result = orderValidator.validateCreditCardNumber(creditCardInformation.getCreditCardNumber());
        assertFalse(result);
    }

    @Test
    public void onlyNumericInCreditCardNumber(){
        CreditCardInformation creditCardInformation = new CreditCardInformation("123456789123456T", "12/22", "123");
        OrderValidator orderValidator = new OrderValidator();
        boolean result = orderValidator.validateCreditCardNumber(creditCardInformation.getCreditCardNumber());
        assertFalse(result);
    }

    @Test
    public void emptyCreditCardNumber(){
        CreditCardInformation creditCardInformation = new CreditCardInformation("", "12/22", "123");
        OrderValidator orderValidator = new OrderValidator();
        boolean result = orderValidator.validateCreditCardNumber(creditCardInformation.getCreditCardNumber());
        assertFalse(result);
    }

    @Test
    public void validCreditCardNumber(){
        CreditCardInformation creditCardInformation = new CreditCardInformation("1234567891234567", "12/22", "123");
        OrderValidator orderValidator = new OrderValidator();
        boolean result = orderValidator.validateCreditCardNumber(creditCardInformation.getCreditCardNumber());
        assertTrue(result);
    }

    @Test
    public void moreThan16DigitCreditCardNumber(){
        CreditCardInformation creditCardInformation = new CreditCardInformation("12345678912345678", " 12/22", "123");
        OrderValidator orderValidator = new OrderValidator();
        boolean result = orderValidator.validateCreditCardNumber(creditCardInformation.getCreditCardNumber());
        assertFalse(result);
    }

    @Test
    public void lessThan16DigitCreditCardNumber(){
        CreditCardInformation creditCardInformation = new CreditCardInformation("123456789123456", "12/22", "123");
        OrderValidator orderValidator = new OrderValidator();
        boolean result = orderValidator.validateCreditCardNumber(creditCardInformation.getCreditCardNumber());
        assertFalse(result);
    }

    // CVV Tests
    @Test
    public void nullCVV(){
        CreditCardInformation creditCardInformation = new CreditCardInformation("1234567891234567", "12/22", null);
        OrderValidator orderValidator = new OrderValidator();
        boolean result = orderValidator.validateCVV(creditCardInformation.getCvv());
        assertFalse(result);
    }

    @Test
    public void onlyNumericInCVV(){
        CreditCardInformation creditCardInformation = new CreditCardInformation("1234567891234567", "12/22", "12D");
        OrderValidator orderValidator = new OrderValidator();
        boolean result = orderValidator.validateCVV(creditCardInformation.getCvv());
        assertFalse(result);
    }

    @Test
    public void emptyCVV(){
        CreditCardInformation creditCardInformation = new CreditCardInformation("1234567891234567", "12/22", "");
        OrderValidator orderValidator = new OrderValidator();
        boolean result = orderValidator.validateCVV(creditCardInformation.getCvv());
        assertFalse(result);
    }

    @Test
    public void validCVV(){
        CreditCardInformation creditCardInformation = new CreditCardInformation("1234567891234567", "12/22", "456");
        OrderValidator orderValidator = new OrderValidator();
        boolean result = orderValidator.validateCVV(creditCardInformation.getCvv());
        assertTrue(result);
    }

    @Test
    public void moreThan3Digit(){
        CreditCardInformation creditCardInformation = new CreditCardInformation("1234567891234567", "12/22", "1234");
        OrderValidator orderValidator = new OrderValidator();
        boolean result = orderValidator.validateCVV(creditCardInformation.getCvv());
        assertFalse(result);
    }

    @Test
    public void lessThan3Digit(){
        CreditCardInformation creditCardInformation = new CreditCardInformation("1234567891234567", "12/22", "12");
        OrderValidator orderValidator = new OrderValidator();
        boolean result = orderValidator.validateCVV(creditCardInformation.getCvv());
        assertFalse(result);
    }

    // Expiration Date Tests
    @Test
    public void nullExpirationDate(){
        CreditCardInformation creditCardInformation = new CreditCardInformation("1234567891234567", null, "123");
        LocalDate orderDate = LocalDate.parse("2023-11-26");
        OrderValidator orderValidator = new OrderValidator();
        boolean result = orderValidator.validateExpirationDate(creditCardInformation.getCreditCardExpiry(), orderDate);
        assertFalse(result);
    }

    @Test
    public void onlyNumericInExpirationDate(){
        CreditCardInformation creditCardInformation = new CreditCardInformation("1234567891234567", "12/2D", "123");
        LocalDate orderDate = LocalDate.parse("2023-11-26");
        OrderValidator orderValidator = new OrderValidator();
        boolean result = orderValidator.validateExpirationDate(creditCardInformation.getCreditCardExpiry(), orderDate);
        assertFalse(result);
    }

    @Test
    public void emptyExpirationDate(){
        CreditCardInformation creditCardInformation = new CreditCardInformation("1234567891234567", "", "123");
        LocalDate orderDate = LocalDate.parse("2023-11-26");
        OrderValidator orderValidator = new OrderValidator();
        boolean result = orderValidator.validateExpirationDate(creditCardInformation.getCreditCardExpiry(), orderDate);
        assertFalse(result);
    }

    @Test
    public void validExpirationDate(){
        CreditCardInformation creditCardInformation = new CreditCardInformation("1234567891234567", "12/24", "123");
        LocalDate orderDate = LocalDate.parse("2023-11-26");
        OrderValidator orderValidator = new OrderValidator();
        boolean result = orderValidator.validateExpirationDate(creditCardInformation.getCreditCardExpiry(), orderDate);
        assertTrue(result);
    }

    @Test
    public void pastExpirationDate(){
        CreditCardInformation creditCardInformation = new CreditCardInformation("1234567891234567", "12/20", "123");
        LocalDate orderDate = LocalDate.parse("2023-11-26");
        OrderValidator orderValidator = new OrderValidator();
        boolean result = orderValidator.validateExpirationDate(creditCardInformation.getCreditCardExpiry(), orderDate);
        assertFalse(result);
    }

    @Test
    public void incorrectMonthInExpirationDate(){
        CreditCardInformation creditCardInformation = new CreditCardInformation("1234567891234567", "13/24", "123");
        LocalDate orderDate = LocalDate.parse("2023-11-26");
        OrderValidator orderValidator = new OrderValidator();
        boolean result = orderValidator.validateExpirationDate(creditCardInformation.getCreditCardExpiry(), orderDate);
        assertFalse(result);
    }







}