package com.notime.mall.config;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileWriter;
import java.io.IOException;

@Configuration
public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2018020102122556";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCdQeknhM2rhiGAH6V0ljxn3rAWIdzduTEQuteTfwjnZtvMhQPuuN1b/88D5yMuaZhZNFeUdWb+SmtP9DAzAWWgnT13T0YhJcxP6txm7JBRrjadCRt+LOFxPiPQk5t9fH7yXjw9i4uMDsNJeTncrVZ/AZYrk0ESC9anJR8XeuBc3HE8T4fqlKKl35jlumIWrPbPNQhKGXaGcOnpiaXO9qYYUSP/tnrjNYXHOso0yBs4YTl+LLX2TJ12p3n/oX6HnL4zQgtN5k4QasHP7CIig1ngcVQGfWsMm4djI9KXNXvGLQPfMQEmyb71mM5OCdl1MtAc6OaIAymhSv2hOLNIuyodAgMBAAECggEAe05/P5mGm4QlKI2n8u8KlneqovASe1kG/BNFjkYB+VBR8OAr4TfbepPvAyRuFap+5xN/yMz14VcBJkRWtufVhEdHNxJV7w/wUIncIGhGEYYFFMVbZWhTrbQH6TiUp6TC9dCmc6vD1CKPRkFj+YGBXT0lPy3LzBa0TYNyCbszyhthrgkpuFYbB0R93IPvvBh5NJFXQytwNb2oVopC9AQWviqnZUZcT0eJ087dQ1WLPa6blBD8DP1PUq0Ldr6pgKfObFxIj8+87DlJznRfdEsbqZlS7jagdw5tLr71WJpctIGPqKpgvajfePP/lj3eY82BKQB+aTw0zmAiB05Yes4LgQKBgQDq3EiQR8J1MEN2rpiLt1WvDYYvKVUgOY7Od//fRPgaMBstbe4TzGBpR8E+z267bHAWLaWtHkfX6muFHn1x68ozEUWk/nZq0smWnuPdcy4E7Itbk36W2FF/rOZB7j5ddlC9byrxDSNgcf9/FA/CU+i5KVQpLYfsk2dvwomvu0aFVQKBgQCraXpxzMmsBx4127LsZDO5bxfxb6nqzyK4NPe0VaGiRg8oaCWczcLz1J5iRqC9QeEwsSt4XU1sYBMTcsFpA0apZpm3prH2HJRx/isNENesaHcihF0mMd0WxU3xyRvWSDeZV5A1Zy1ZEJ+p17DGwb2j+yo2uBrDNXBgBWEzXwiRqQKBgBdXFvsHtqKQzlOQHGbeLGy+KlSrheMy9Sc9s7cLkqB/oWPNZfifugEceW71jGqh5y29EZb3yGoDyPWsxwi4Rxr2H3a7Nyd8lT4bwkdyt+MTYvIR4WW6T7chhqyMsbP2GyYIUzsrdBWUnrCRXNOSJTGpksyY0sZHC+OGcMp/EQ4VAoGBAIISSVL/pm1+/UK7U1ukcced8JpKNLM0uVD1CJ50eHHOHgR4e0owrWYfioxisejLjBlJ6AWvL2g0w2T3qKKKVN2JOM4ulU5/w3l4+KwygqaWowizTogEQJPd5ta52ADTzjTzSD/t6nByd+YHAWLhc4lyt0bMj6pf68VBb8/upm75AoGAGAYz79IVHp9eppykufjNcWu6okkG8tZnzuyaWKW/CuKKBWMaTk0vcyQlfJfxIBccoQrBuYyXBdcpPuZ/ys2C25pNrkACuhIKNgnMc0floJoYEfJzetw/3cIimWu4NJzVQOaojaGA58oo2+fub43Xn25Jq4rvSVe3oLdb5xWkw5Q=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhkZi6W0wn/prX+NIIF9ATb5Z8ReKK4hFYtBrweDfGHD1mNW7YIZY4G5hE7S2Sry8eFXlFgSlBWlJ4fVnDaK9MkVThpwE2H65ooVlK/wLuyPqovIVpMt/utva5Ayuzv7eQOWK45FdLDNDlK8QLoBko6SS+YbnWnf7a+mrf4NAS4UFClpfe8Byqe8XIraO2Cg4Ko5Y5schX39rOAH8GlLdgqQRYVQ2dCnkIQ+L+I4Cy9Mvw3rIkTwt3MBU+AqREXY4r5Bn6cmmX/9MAJbFqrofGiUAqG+qbjTcZAzgNPfuiD0zXgt/YYjMQMzck75BOmwnYOam2ajODUSQn8Xybsa7wQIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://yeko.mall.com:8087/alipay/callback/return";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://yeko.mall.com:8087/alipay/callback/return";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipay.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Bean
    public AlipayClient alipayClient(){
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,app_id,merchant_private_key,"json",charset,alipay_public_key,sign_type);
        return  alipayClient;
    }

}

