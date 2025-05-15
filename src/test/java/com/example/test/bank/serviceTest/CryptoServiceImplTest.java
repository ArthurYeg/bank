import com.example.test.bank.exception.UnsupportedCurrencyException;
import com.example.test.bank.service.impl.CryptoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CryptoServiceImplTest {

    private CryptoServiceImpl cryptoService;

    @BeforeEach
    void setUp() {
        cryptoService = new CryptoServiceImpl("YourSecureSecretKey123");
    }

    @Test
    void testConvertCurrency_Converts_USD_To_EUR_Correctly() {
        BigDecimal amount = new BigDecimal("100");
        BigDecimal expected = new BigDecimal("85.00");
        BigDecimal result = cryptoService.convertCurrency(amount, "USD", "EUR");
        assertEquals(expected, result, "Currency conversion from USD to EUR did not match expected value.");
    }

    @Test
    void testConvertCurrency_ValidConversion() {
        BigDecimal result = cryptoService.convertCurrency(new BigDecimal("100"), "USD", "EUR");
        assertEquals(new BigDecimal("85.00"), result);
    }

    @Test
    void testConvertCurrency_UnsupportedCurrency() {
        assertThrows(UnsupportedCurrencyException.class, () -> {
            cryptoService.convertCurrency(new BigDecimal("100"), "USD", "JPY");
        });
    }

    @Test
    void testEncryptDecrypt_EmptyString() {
        String encrypted = cryptoService.encrypt("");
        String decrypted = cryptoService.decrypt(encrypted);
        assertEquals("", decrypted);
    }

    @Test
    void testDecrypt_InvalidData() {
        assertThrows(CryptoServiceImpl.DecryptionException.class, () -> {
            cryptoService.decrypt("invalidData");
        });
    }

    @Test
    void encryptDecryptRoundTrip() {
        String original = "test data";
        String encrypted = cryptoService.encrypt(original);
        String decrypted = cryptoService.decrypt(encrypted);
        assertEquals(original, decrypted);
    }

    @Test
    void testGetCurrentPrice_ValidCurrency() {
        BigDecimal btcPrice = cryptoService.getCurrentPrice("BTC");
        assertEquals(new BigDecimal("40000"), btcPrice);

        BigDecimal ethPrice = cryptoService.getCurrentPrice("ETH");
        assertEquals(new BigDecimal("3000"), ethPrice);
    }

    @Test
    void testGetCurrentPrice_UnsupportedCurrency() {
        assertThrows(UnsupportedCurrencyException.class, () -> {
            cryptoService.getCurrentPrice("JPY");
        });
    }

    @Test
    void testGetHistoricalPrices_ValidCurrency() {
        List<BigDecimal> prices = cryptoService.getHistoricalPrices("BTC", 5);
        assertEquals(5, prices.size());

    }
    @Test
    void testIsValidCardNumber() {
        String validCardNumber = "4532015112830366";
        String invalidCardNumber = "4532015112830365";
        assertTrue(cryptoService.isValidCardNumber(validCardNumber), "Valid card number should return true.");
        assertFalse(cryptoService.isValidCardNumber(invalidCardNumber), "Invalid card number should return false.");
    }

//    @Test
//    void testCalculateLuhnCheckDigit() {
//        String number = "123456789012345"; // Example input
//        int expectedCheckDigit = 9; // Expected check digit
//        int actualCheckDigit = cryptoService.calculateLuhnCheckDigit(number);
//        assertEquals(expectedCheckDigit, actualCheckDigit, "Luhn check digit calculation is incorrect.");
//    }


    @Test
    void testConvertCurrency_Converts_USD_To_GBP_Correctly() {
        BigDecimal amount = new BigDecimal("100");
        BigDecimal expected = new BigDecimal("75.00");
        BigDecimal result = cryptoService.convertCurrency(amount, "USD", "GBP");
        assertEquals(expected, result, "Currency conversion from USD to GBP did not match expected value.");
    }

    @Test
    void testGenerateCardNumber_ValidLengthAndStartingDigit() {
        String cardNumber = cryptoService.generateCardNumber();
        assertEquals(16, cardNumber.length(), "Card number should be 16 digits long.");
        assertTrue(cardNumber.startsWith("4"), "Card number should start with '4'.");
        assertTrue(isValidLuhn(cardNumber), "Card number failed Luhn check");
    }
    public boolean isValidLuhn(String cardNumber) {
        int sum = 0;
        boolean alternate = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        return (sum % 10 == 0);
    }

}
