// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.params;

import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnConnectionPNames;
import org.apache.http.cookie.params.CookieSpecPNames;
import org.apache.http.auth.params.AuthPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.CoreConnectionPNames;

@Deprecated
public interface AllClientPNames extends CoreConnectionPNames, CoreProtocolPNames, ClientPNames, AuthPNames, CookieSpecPNames, ConnConnectionPNames, ConnManagerPNames, ConnRoutePNames
{
}
