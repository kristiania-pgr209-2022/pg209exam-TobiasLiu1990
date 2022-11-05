package no.kristiania.pgr209.ISeekYou.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class MessageServer {

    private final Server server;
    private final Logger logger = LoggerFactory.getLogger(MessageServer.class);

    public MessageServer(int port){
        this.server = new Server(port);
        var context = createWebApp();
        server.setHandler(createApiContext(context));
    }

    private WebAppContext createWebApp() {
        var context = new WebAppContext(); // Serving dynamic webpage
        context.setContextPath("/");
        context.setBaseResource(Resource.newClassPathResource("/webapp"));
        return context;
    }

    private ServletContextHandler createApiContext(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new ServletContainer(new ResourceConfig(MessageEndPoint.class))), "/api/*");
        return context;
    }


    public URL getURL() throws MalformedURLException {
        return server.getURI().toURL();
    }

    public void start() throws Exception {
        this.server.start();
        logger.warn("Server starting at {}", getURL());
    }

    public static void main(String[] args) throws Exception {
        var server = new MessageServer(8080);
        server.start();
    }
}