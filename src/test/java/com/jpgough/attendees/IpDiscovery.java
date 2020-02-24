package com.jpgough.attendees;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class IpDiscovery {

    public static String myIPAddress() {
        String candidateIPAddress = null;
        try {
            candidateIPAddress = InetAddress.getLocalHost().getHostAddress();
            if (!candidateIPAddress.equals("127.0.0.1")) {
                return candidateIPAddress;
            }
            final Enumeration<NetworkInterface> interfaces =
                    NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                final NetworkInterface cur = interfaces.nextElement();
                if (cur.isLoopback()) {
                    continue;
                }
                for (final InterfaceAddress addr : cur.getInterfaceAddresses()) {
                    final InetAddress inet_addr = addr.getAddress();
                    if (!(inet_addr instanceof Inet4Address)) {
                        continue;
                    }
                    if (addr.getBroadcast() != null) {
                        return inet_addr.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //FIXME only works on mac
        return "host.docker.internal";
        //throw new RuntimeException("Unable to find a candidate IP Address");
    }
}
