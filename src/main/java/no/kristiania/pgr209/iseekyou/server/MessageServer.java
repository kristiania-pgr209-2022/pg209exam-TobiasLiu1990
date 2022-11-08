package no.kristiania.pgr209.iseekyou.server;

import jakarta.servlet.DispatcherType;
import no.kristiania.pgr209.iseekyou.database.Database;
import org.eclipse.jetty.server.CustomRequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumSet;

public class MessageServer {

    private final Server server;
    private final Logger logger = LoggerFactory.getLogger(MessageServer.class);

    //Receive dataSource depending on caller (prod vs testing)
    public MessageServer(int port, DataSource dataSource) throws IOException {
        this.server = new Server(port);
        server.setHandler(new HandlerList(createApiContext(dataSource), createWebApp()));
        server.setRequestLog(new CustomRequestLog());
    }

    private WebAppContext createWebApp() throws IOException {
        WebAppContext webContext = new WebAppContext();
        webContext.setContextPath("/");

        //Set source directory
        setSourceDirectory(webContext);

        //Where to locate files
        webContext.setInitParameter("jersey.config.server.provider.packages", "no.kristiania.pgr209.iseekyou");

        //Filter
        webContext.addFilter(new FilterHolder(new MessageServerFilter()), "/", EnumSet.of(DispatcherType.REQUEST));

        return webContext;
    }

    private ServletContextHandler createApiContext(DataSource dataSource) {
        var context = new ServletContextHandler(server, "/api");
        context.addServlet(new ServletHolder(new ServletContainer(new MessagingConfig(dataSource))), "/*");
        return context;
    }

    private void setSourceDirectory(WebAppContext webContext) throws IOException {
        var resources = Resource.newClassPathResource("/webapp");
        var sourceDirectory = getSourceDirectory(resources);

        if (sourceDirectory != null) {
            webContext.setBaseResource(Resource.newResource(sourceDirectory));
            webContext.setInitParameter(DefaultServlet.CONTEXT_INIT + "useFileMappedBuffer", "false");      //Remove lock on file
        } else {
            webContext.setBaseResource(resources);
        }
    }

    private File getSourceDirectory(Resource resources) throws IOException {
        if (resources.getFile() == null) {
            return null;
        } else {
            var sourceDirectory = new File(resources.getFile()
                    .getAbsolutePath()
                    .replace("target\\classes", "src\\main\\resources"));
            return sourceDirectory.exists() ? sourceDirectory : null;
        }
    }

    public URL getURL() throws MalformedURLException {
        return server.getURI().toURL();
    }

    public void start() throws Exception {
        this.server.start();
        logger.warn("Server starting at {}", getURL());
    }

    public static void main(String[] args) throws Exception {
        //Forgot to handle azure PORT. Add later
        var server = new MessageServer(8080, Database.getDatasource());
        server.start();
    }
}