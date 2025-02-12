import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class CryptographyHelper {
    public static String generateHash(String data) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            //we want to end up with hexidecimal values not bytes
            StringBuilder hexadecimalString = new StringBuilder();
            // Mamy -5 , ale dzięki temu że jest 0xff & hash[i] to możemy skończyć z dodatnią wartością bo 0xff&-5=256-5=251
            for (int i = 0; i < hash.length; i++) {
                String hexadecimal = Integer.toHexString(0xff & hash[i]);
                if(hexadecimal.length() == 1) hexadecimalString.append('0');//zapewnia, że każdy hash będzie mieć 64 characters
                hexadecimalString.append(hexadecimal);
            }
            return hexadecimalString.toString();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    //generate public key and private key
    //private key: 256 bits long random integer
    //public key: point on the elliptic curve
    //(x,y)- both of these values are 256 bits long
    public static KeyPair ellipticCurveCrypto(){
        try{
         KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
            ECGenParameterSpec params = new ECGenParameterSpec("prime256v1");
            keyPairGenerator.initialize(params);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //ECC to sign the given transaction (message)
    //ECDSA - elliptic curve digital signature algorithm (ECDSA)
    public static byte[] sign(PrivateKey privateKey, String input) {
        Signature signature;
        byte[] output = new byte[0];
        try{
            signature = Signature.getInstance("ECDSA", "BC");
            signature.initSign(privateKey);
            signature.update(input.getBytes());
            output = signature.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return output;
    }
    //check whether the given transaction belongs to the sender based on signature
    public static boolean verify(PublicKey publicKey, String data, byte[] signature) {
        try{
            Signature ecdsaSignature = Signature.getInstance("ECDSA", "BC");
            ecdsaSignature.initVerify(publicKey);
            ecdsaSignature.update(data.getBytes());
            return ecdsaSignature.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
