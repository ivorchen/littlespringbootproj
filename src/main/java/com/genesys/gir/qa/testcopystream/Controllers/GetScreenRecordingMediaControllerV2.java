package com.genesys.gir.qa.testcopystream.Controllers;

import com.genesys.gir.qa.testcopystream.Models.WebDAVClient;
import com.genesys.gir.qa.testcopystream.Models.WebDavConfigComponent;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;

@RestController
@Configuration
@EnableConfigurationProperties(WebDavConfigComponent.class)
@RequestMapping(value = "/internal-api/screen-recordings",method = RequestMethod.GET)
public class GetScreenRecordingMediaControllerV2 {
    @Autowired
    private WebDavConfigComponent webDavConfigComponent;

    private Logger logger= LogManager.getLogger(GetScreenRecordingMediaControllerV2.class);

    @lombok.Setter
    private HttpServletResponse response;

    @Inject
    private WebDAVClient webDAVClient;

    @GetMapping(value = "/{mediaID}")
    public void GetScreenRecordingMediaFile(@PathVariable("mediaID") String mediID) throws IOException, URISyntaxException {
        String url= webDavConfigComponent.getHost()+":"+
                    webDavConfigComponent.getPort()+
                    webDavConfigComponent.getUrl();
        URI uri=new URIBuilder().setScheme("http")
                .setHost(webDavConfigComponent.getHost()+":"+
                        webDavConfigComponent.getPort())
                .setPath(webDavConfigComponent.getUrl())
                .build();
        logger.info("Fetching media file from url :"+url);
        InputStream ins = webDAVClient.get(
                                            uri,
                                            webDavConfigComponent.getUsername(),
                                            webDavConfigComponent.getPassword()
        );
        copyStream(ins,response.getOutputStream(),mediID);
    }

    private void copyStream(InputStream inputStream, OutputStream outputStream, String mediaID) throws IOException
    {
        byte[] buffer = new byte[8192];
        int bytesRead;

        try
        {
            while ((bytesRead = inputStream.read(buffer)) > 0)
            {
                try
                {
                    outputStream.write(buffer, 0, bytesRead);
                }
                catch (IOException e)
                {
                    logger.warn(mediaID + " Output Stream is closed, streaming is canceled", e);
                    break;
                }
            }
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
    }
}
