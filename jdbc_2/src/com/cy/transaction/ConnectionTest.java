package com.cy.transaction;

import com.cy.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;

public class ConnectionTest {
    @Test
    public void testGetConnection() throws Exception{
        Connection conn = JDBCUtils.getConnection();
        System.out.println(conn);
    }
}
