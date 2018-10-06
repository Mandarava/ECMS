package com.finance.util;

import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * LOAD DATA [LOW_PRIORITY | CONCURRENT] [LOCAL] INFILE 'file_name'
 * [REPLACE | IGNORE]
 * INTO TABLE tbl_name
 * [PARTITION (partition_name [, partition_name] ...)]
 * [CHARACTER SET charset_name]
 * [{FIELDS | COLUMNS}
 * [TERMINATED BY 'string']
 * [[OPTIONALLY] ENCLOSED BY 'char']
 * [ESCAPED BY 'char']
 * ]
 * [LINES
 * [STARTING BY 'string']
 * [TERMINATED BY 'string']
 * ]
 * [IGNORE number {LINES | ROWS}]
 * [(col_name_or_user_var
 * [, col_name_or_user_var] ...)]
 * [SET col_name={expr | DEFAULT},
 * [, col_name={expr | DEFAULT}] ...]
 * <p>
 * SELECT * FROM c_rule_result INTO OUTFILE 'C:\\ProgramData\\MySQL\MySQL Server 5.7\\Uploads\\1.sql' FIELDS TERMINATED BY '|' ENCLOSED BY '"' LINES TERMINATED BY '\r\n';
 *
 * @see "https://dev.mysql.com/doc/refman/5.7/en/load-data.html"
 */
public class BulkLoadData2MySQL {

    private static final Logger logger = Logger.getLogger(BulkLoadData2MySQL.class);

    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8", "root", "root");
        } catch (ClassNotFoundException | SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String testSql = "LOAD DATA LOCAL INFILE '" + System.currentTimeMillis() + ".csv' " +
                "IGNORE " +
                "INTO TABLE test (b,c,d,e,f,g) ";
        InputStream dataStream = getTestDataInputStream();
        BulkLoadData2MySQL dao = new BulkLoadData2MySQL();
        try {
            long beginTime = System.currentTimeMillis();
            int rows = dao.bulkLoadFromInputStream(testSql, dataStream);
            long endTime = System.currentTimeMillis();
            System.out.println("importing " + rows + " rows data into mysql and cost " + (endTime - beginTime) + " ms!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static InputStream getTestDataInputStream() throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= 10; i++) {
            for (int j = 0; j <= 10000; j++) {
                builder.append(4);
                builder.append("\t");
                builder.append(4 + 1);
                builder.append("\t");
                builder.append(4 + 2);
                builder.append("\t");
                builder.append(4 + 3);
                builder.append("\t");
                builder.append(4 + 4);
                builder.append("\t");
                builder.append(4 + 5);
                builder.append("\n");
            }
        }
        byte[] bytes = builder.toString().getBytes("UTF-8");
        return new ByteArrayInputStream(bytes);
    }

    /**
     * load bulk data from InputStream to MySQL
     */
    private int bulkLoadFromInputStream(String loadDataSql,
                                       InputStream dataStream) throws SQLException {
        if (dataStream == null) {
            logger.info("InputStream is null ,No data is imported");
            return 0;
        }
        PreparedStatement statement = connection.prepareStatement(loadDataSql);
        int result = 0;
        if (statement.isWrapperFor(com.mysql.jdbc.Statement.class)) {
            com.mysql.jdbc.PreparedStatement mysqlStatement = statement.unwrap(com.mysql.jdbc.PreparedStatement.class);
            mysqlStatement.setLocalInfileInputStream(dataStream);
            result = mysqlStatement.executeUpdate();
        }
        statement.close();
        connection.close();
        return result;
    }

}
