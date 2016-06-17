package com.kalix.framework.core.impl.web;

import org.apache.camel.ExchangePattern;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.http.common.HttpConsumer;
import org.apache.camel.http.common.HttpHelper;
import org.apache.camel.http.common.HttpMessage;
import org.apache.camel.impl.DefaultExchange;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chenyanxu
 */
public class KalixCamelHttpTransportServlet extends CamelHttpTransportServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.log.trace("Service: {}", request);
        HttpConsumer consumer = this.resolve(request);
        if (consumer == null) {
            this.log.debug("No consumer to service request {}", request);
            response.sendError(404);
        } else if (consumer.isSuspended()) {
            this.log.debug("Consumer suspended, cannot service request {}", request);
            response.sendError(503);
        } else if ("OPTIONS".equals(request.getMethod())) {
            String exchange1;
            if (consumer.getEndpoint().getHttpMethodRestrict() != null) {
                exchange1 = "OPTIONS," + consumer.getEndpoint().getHttpMethodRestrict();
            } else {
                exchange1 = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,CONNECT,PATCH";
            }

            response.addHeader("Allow", exchange1);
            response.setStatus(200);
        } else if (consumer.getEndpoint().getHttpMethodRestrict() != null && !consumer.getEndpoint().getHttpMethodRestrict().contains(request.getMethod())) {
            response.sendError(405);
        } else if ("TRACE".equals(request.getMethod()) && !consumer.isTraceEnabled()) {
            response.sendError(405);
        } else {
            DefaultExchange exchange = new DefaultExchange(consumer.getEndpoint(), ExchangePattern.InOut);
            if (consumer.getEndpoint().isBridgeEndpoint()) {
                exchange.setProperty("CamelSkipGzipEncoding", Boolean.TRUE);
                exchange.setProperty("CamelSkipWwwFormUrlEncoding", Boolean.TRUE);
            }

            if (consumer.getEndpoint().isDisableStreamCache()) {
                exchange.setProperty("CamelDisableHttpStreamCache", Boolean.TRUE);
            }

            ClassLoader oldTccl = this.overrideTccl(exchange);
            HttpHelper.setCharsetFromContentType(request.getContentType(), exchange);
            exchange.setIn(new HttpMessage(exchange, request, response));
            String contextPath = consumer.getEndpoint().getPath();
            exchange.getIn().setHeader("CamelServletContextPath", contextPath);
            String httpPath = (String) exchange.getIn().getHeader("CamelHttpPath");
            if (contextPath != null && httpPath.startsWith(contextPath)) {
                exchange.getIn().setHeader("CamelHttpPath", httpPath.substring(contextPath.length()));
            }

            try {
                consumer.createUoW(exchange);
            } catch (Exception var16) {
                this.log.error("Error processing request", var16);
                throw new ServletException(var16);
            }

            try {
                if (this.log.isTraceEnabled()) {
                    this.log.trace("Processing request for exchangeId: {}", exchange.getExchangeId());
                }

                consumer.getProcessor().process(exchange);
            } catch (Exception var15) {
                exchange.setException(var15);
            }

            try {
                if (this.log.isTraceEnabled()) {
                    this.log.trace("Writing response for exchangeId: {}", exchange.getExchangeId());
                }

                Integer e = consumer.getEndpoint().getResponseBufferSize();
                if (e != null) {
                    this.log.trace("Using response buffer size: {}", e);
                    response.setBufferSize(e.intValue());
                }
                //consumer.getBinding().writeResponse(exchange, response);
                //===CODE_START===
                /**
                 * change code to deal with all runtime exception.
                 * avoid the origin error back to client side.
                 **/
                if (exchange.getException() == null) {
                    consumer.getBinding().writeResponse(exchange, response);
                } else {
                    response.setHeader("Content-Type", " text/html;charset=utf-8");
                    response.getWriter().write("{success:false,msg:'" + exchange.getException().getMessage() + "'}");
                }
                //===CODE_END===
            } catch (IOException var17) {
                this.log.error("Error processing request", var17);
                throw var17;
            } catch (Exception var18) {
                this.log.error("Error processing request", var18);
                throw new ServletException(var18);
            } finally {
                consumer.doneUoW(exchange);
                this.restoreTccl(exchange, oldTccl);
            }
        }
    }
}
