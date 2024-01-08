import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public final class TokenPair {
    private String token;
    private long expire;
    TokenPair() {
        try {
            long now = System.currentTimeMillis();
            byte[] bytes = ByteBuffer.allocate(Long.BYTES).putLong(now).array();
            byte[] digest = MessageDigest.getInstance("MD5").digest(bytes);
            BigInteger num = new BigInteger(1, digest);
            this.token = num.toString(16);
            while (this.token.length() < 32) {
                this.token = "0" + this.token;
            }
            long month = 2592000000L;
            this.expire = now + month;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public String getToken() {
        return token;
    }

    public long getExpire() {
        return expire;
    }

    public TokenPair renew() {
        long now = System.currentTimeMillis();
        long month = 2592000000L;
        this.expire = now + month;
        return this;
    }
}
