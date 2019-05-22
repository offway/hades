package cn.offway.hades.provider;

import org.quartz.utils.ConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class MyConnectionProvider implements ConnectionProvider {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Connection getConnection() throws SQLException {
        String profile = System.getProperty("spring.profiles.active", "dev");
        InputStream in;
        if ("dev".equals(profile)) {
            in = this.getClass().getClassLoader().getResourceAsStream("filters/dev/application.yml");
        } else {
            in = this.getClass().getClassLoader().getResourceAsStream("filters/prd/application.yml");
        }
        Yaml yaml = new Yaml();
        Map<String, Object> cfg = yaml.load(in);
        if (cfg.containsKey("spring")) {
            Map a = (Map) cfg.get("spring");
            if (a.containsKey("datasource")) {
                Map b = (Map) a.get("datasource");
                if (b.containsKey("url")) {
                    String c = String.valueOf(b.get("url"));
                    String d = String.valueOf(b.get("username"));
                    String e = String.valueOf(b.get("password"));
                    url = c;
                    username = d;
                    password = e;
                }
            }
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            logger.error(this.getClass().getCanonicalName(), e);
        }
        return null;
    }

    @Override
    public void shutdown() throws SQLException {
        //
    }

    @Override
    public void initialize() throws SQLException {
        Connection connection = getConnection();
        if (connection == null) {
            logger.error(this.getClass().getCanonicalName() + " INIT ERROR");
        }
    }
}
