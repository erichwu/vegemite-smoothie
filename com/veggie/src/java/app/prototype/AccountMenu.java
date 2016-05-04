package com.veggie.src.java.app.prototype;

import java.io.IOException;

import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;

import com.veggie.src.java.controllers.account.*;
import com.veggie.src.java.controllers.Controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class AccountMenu implements HttpHandler {
    private static final String HANDLE_PATH = "/account/";

    public void handle(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().toString();
        if (query.length() == 0 || query.charAt(query.length() - 1) != '/') query += "/";
        query = query.substring(HANDLE_PATH.length());

        if (query.length() == 0) {
            Server.sendToLogin(httpExchange);
        } else {
            int slashLoc = query.indexOf("/");
            if (slashLoc < 0) slashLoc = query.length();
            int sessionId = Integer.parseInt(query.substring(0, slashLoc));
            String controller = query.substring(slashLoc + 1);
            if (controller.indexOf("/") > 0) controller = controller.substring(0, controller.indexOf("/"));
            handleMenu(httpExchange, sessionId, controller);
        }

    }

    public void handleMenu(HttpExchange httpExchange, int sessionId, String controller) throws IOException {

        Map<String, Controller> controllerMap = new TreeMap<>();
        controllerMap.put("addaccount", new AddAccountController());
        controllerMap.put("addfee", new AddFeeController());
        controllerMap.put("deleteaccount", new DeleteAccountController());
        controllerMap.put("editaccount", new EditAccountController());
        controllerMap.put("lookupuser", new LookUpUserController());
        controllerMap.put("payfee", new PayFeeController());
        controllerMap.put("report", new ReportController());
        controllerMap.put("searchuser", new SearchUserController());
        controllerMap.put("suspendaccount", new SuspendAccountController());

        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("addaccount", "Add Account");
        nameMap.put("addfee", "Add Fee");
        nameMap.put("deleteaccount", "Delete Account");
        nameMap.put("editaccount", "Edit Account");
        nameMap.put("lookupuser", "Look Up User");
        nameMap.put("payfee", "Pay Fee");
        nameMap.put("report", "Generate Report");
        nameMap.put("searchuser", "Search Users");
        nameMap.put("suspendaccount", "Suspend Account");

        if (controller.length() == 0) {
            StringBuilder response = new StringBuilder();
            response.append("<html><body>");
            response.append("<h1>Accounts</h1><br/>");
            String linkStart = "<a href=\"/account/" + sessionId + "/";
            String linkEnd = "\">";
            String lineEnd = "</a><br/>";
            for (String s : controllerMap.keySet()) {
                response.append(linkStart + s + linkEnd + nameMap.get(s) + lineEnd);
            }
            response.append("<a href=\"/" + sessionId + "\">Back</a><br/>");
            response.append("</body></html>");
            Server.writeResponse(httpExchange, response.toString());
        } else if (controllerMap.containsKey(controller)) {
            Controller c = controllerMap.get(controller);
            StringBuilder response = new StringBuilder();
            response.append(c.activate().getFieldNames().toString());
            Server.writeResponse(httpExchange, response.toString());
        } else {
            StringBuilder response = new StringBuilder();
            response.append("<html><body>");
            response.append("<h3>Invalid Controller Reference</h3><br/>");
            response.append("<a href=\"/" + sessionId + "\">Back</a><br/>");
            response.append("</body></html>");
            Server.writeResponse(httpExchange, response.toString());
        }
    }
}
