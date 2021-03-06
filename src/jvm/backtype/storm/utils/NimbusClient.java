package backtype.storm.utils;

import backtype.storm.Config;
import backtype.storm.generated.Nimbus;
import java.util.Map;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;


public class NimbusClient {
    public static NimbusClient getConfiguredClient(Map conf) {
        String nimbusHost = (String) conf.get(Config.NIMBUS_HOST);
        int nimbusPort = ((Long) conf.get(Config.NIMBUS_THRIFT_PORT)).intValue();
        return new NimbusClient(nimbusHost, nimbusPort);
    }

    private TTransport conn;
    private Nimbus.Client client;

    public NimbusClient(String host) {
        this(host, 6627);
    }

    public NimbusClient(String host, int port) {
        try {
            conn = new TFramedTransport(new TSocket(host, port));
            client = new Nimbus.Client(new TBinaryProtocol(conn));
            conn.open();
        } catch(TException e) {
            throw new RuntimeException(e);
        }
    }

    public Nimbus.Client getClient() {
        return client;
    }

    public void close() {
        conn.close();
    }
}
