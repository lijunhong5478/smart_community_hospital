import com.tyut.Application;
import com.tyut.utils.CryptoUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class TestCrypto {
    @Autowired
    private CryptoUtil cryptoUtil;
    @Test
    public void test() {
        String originalText = "123456";
        //ed55f05984b0298da080fcefd3e3bd3c
        String encryptedText = cryptoUtil.encodePassword(originalText);
        System.out.println("加密后的密码：" + encryptedText);
        System.out.println("解密后的密码：" + cryptoUtil.matches(originalText, encryptedText));
    }
}
