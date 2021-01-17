// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.helpers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.IOException;
import java.io.Closeable;

public class Closer
{
    public static void closeSilent(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        }
        catch (Exception ex) {}
    }
    
    public static void close(final Closeable closeable) throws IOException {
        if (closeable != null) {
            closeable.close();
        }
    }
    
    public static void closeSilent(final Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        }
        catch (Exception ex) {}
    }
    
    public static void close(final Statement statement) throws SQLException {
        if (statement != null) {
            statement.close();
        }
    }
    
    public static void closeSilent(final Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        }
        catch (Exception ex) {}
    }
    
    public static void close(final Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
