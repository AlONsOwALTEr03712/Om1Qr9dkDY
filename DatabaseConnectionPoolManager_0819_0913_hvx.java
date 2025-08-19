// 代码生成时间: 2025-08-19 09:13:59
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;

public class DatabaseConnectionPoolManager {
    // 使用ConcurrentHashMap存储数据库连接池
    private ConcurrentHashMap<String, DataSource> connectionPool;
    
    // JDBC驱动名称及数据库URL
    private String jdbcDriver;
    private String dbUrl;
    
    // 数据库的用户名与密码
    private String username;
    private String password;
    
    public DatabaseConnectionPoolManager(String jdbcDriver, String dbUrl, String username, String password) {
        this.jdbcDriver = jdbcDriver;
        this.dbUrl = dbUrl;
        this.username = username;
        this.password = password;
        this.connectionPool = new ConcurrentHashMap<>();
        try {
            // 加载JDBC驱动
            Class.forName(jdbcDriver);
        } catch (ClassNotFoundException e) {
            System.err.println("There is no such driver class: " + e.getMessage());
        }
    }
    
    // 获取数据库连接
    public Connection getConnection(String poolName) {
        DataSource dataSource = connectionPool.get(poolName);
        if (dataSource == null) {
            synchronized (this) {
                dataSource = connectionPool.get(poolName);
                if (dataSource == null) {
                    // 创建连接池并添加到连接池管理
                    dataSource = createDataSource(poolName);
                    connectionPool.put(poolName, dataSource);
                }
            }
        }
        try {
            // 从连接池获取连接
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("There is an error getting connection from pool: " + e.getMessage(), e);
        }
    }
    
    // 创建数据源并返回
    private DataSource createDataSource(String poolName) {
        // 根据实际情况选择不同的数据源实现，这里以HikariCP为例
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource();
        dataSource.setJdbcUrl(dbUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(jdbcDriver);
        // 可以根据需要设置其他HikariCP参数
        return dataSource;
    }
    
    // 关闭指定的数据库连接池
    public void shutdownConnectionPool(String poolName) {
        DataSource dataSource = connectionPool.remove(poolName);
        if (dataSource != null) {
            try {
                ((com.zaxxer.hikari.HikariDataSource) dataSource).close();
            } catch (Exception e) {
                System.err.println("Error shutting down the connection pool: " + e.getMessage());
            }
        }
    }
    
    // 关闭所有数据库连接池
    public void shutdownAllConnectionPools() {
        for (DataSource dataSource : connectionPool.values()) {
            try {
                ((com.zaxxer.hikari.HikariDataSource) dataSource).close();
            } catch (Exception e) {
                System.err.println("Error shutting down a connection pool: " + e.getMessage());
            }
        }
        connectionPool.clear();
    }
    
    public static void main(String[] args) {
        // 示例：创建数据库连接池管理器，并获取连接
        DatabaseConnectionPoolManager manager = new DatabaseConnectionPoolManager(
                "org.postgresql.Driver",
                "jdbc:postgresql://localhost:5432/mydb",
                "username",
                "password"
        );
        
        try (Connection connection = manager.getConnection("myPool")) {
            // 使用连接进行操作
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 在适当的时候关闭连接池
            manager.shutdownAllConnectionPools();
        }
    }
}