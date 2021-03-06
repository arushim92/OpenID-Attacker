/*
 * OpenID Attacker
 * (C) 2015 Christian Mainka & Christian Koßmann
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package wsattacker.sso.openid.attacker.controller;

import java.util.List;
import wsattacker.sso.openid.attacker.composition.AbstractBean;
import wsattacker.sso.openid.attacker.config.OpenIdServerConfiguration;
import wsattacker.sso.openid.attacker.evaluation.EvaluationResult;
import wsattacker.sso.openid.attacker.evaluation.EvaluationResultStore;
import wsattacker.sso.openid.attacker.log.RequestLogEntry;
import wsattacker.sso.openid.attacker.log.RequestLogger;
import wsattacker.sso.openid.attacker.server.IdpType;
import wsattacker.sso.openid.attacker.server.OpenIdServer;

public class ServerController extends AbstractBean {

    private IdpType idpType;
    
    private static final OpenIdServer attackerServer = new OpenIdServer();
    private static final OpenIdServerConfiguration attackerConfig = OpenIdServerConfiguration.getAttackerInstance();
    
    private static OpenIdServer analyzerServer;
    private static final OpenIdServerConfiguration analyzerConfig = OpenIdServerConfiguration.getAnalyzerInstance();
    
    public ServerController() {
        // by default set config and server to the attacker IdP
        idpType = IdpType.ATTACKER;
    }
    
    public OpenIdServer getServer() {
        if (idpType.equals(IdpType.ATTACKER)) {
            return attackerServer;
        }
        
        return getAnalyzerServer();
    }

    public OpenIdServerConfiguration getConfig() {
        if (idpType.equals(IdpType.ATTACKER)) {
            return attackerConfig;
        }
        
        return analyzerConfig;
    }
    
    
    
    // set the server AND config to the attacker or analyzer IdP
    public void setIdp(IdpType idp) {
        switch (idp) {
            case ATTACKER:
                idpType = IdpType.ATTACKER;
                break;
            case ANALYZER:
                idpType = IdpType.ANALYZER;
                break;
        }
    }
    
    public OpenIdServer getAnalyzerServer() {
        if (analyzerServer == null) {
            analyzerServer = new OpenIdServer(IdpType.ANALYZER);
        }
        
        return analyzerServer;
    }
    
    public OpenIdServerConfiguration getAnalyzerConfig() {
        return analyzerConfig;
    }

    public OpenIdServer getAttackerServer() {
        return attackerServer;
    }

    public OpenIdServerConfiguration getAttackerConfig() {
        return attackerConfig;
    }
    public List<RequestLogEntry> getRequestLog() {
        return RequestLogger.getInstance().getEntryList();
    }
    
    public List<EvaluationResult> getEvaluationResults() {
        return EvaluationResultStore.getEvaluationResultStore().getEvaluationResults();
    }
    
    /*public List<RequestLogEntry> getFilteredRequestLog() {
        List<RequestLogEntry> requestLog = RequestLogger.getInstance().getEntryList();
        
        List<RequestLogEntry> filteredRequestLog =
                requestLog.
                stream().
                filter(entry -> entry.getIdpType().equals(idpType)).
                collect(Collectors.toList());
        
        return filteredRequestLog;
    }*/
    /*
    public List<RequestLogEntry> getAttackerRequestLog() {
        List<RequestLogEntry> requestLog = RequestLogger.getInstance().getEntryList();
        
        return requestLog.
                parallelStream().
                filter(entry -> entry.getIdpType().equals(IdpType.ATTACKER)).
                collect(Collectors.toList());
    }
    
    public List<RequestLogEntry> getAnalyzerRequestLog() {
        List<RequestLogEntry> requestLog = RequestLogger.getInstance().getEntryList();
        
        return requestLog.
                parallelStream().
                filter(entry -> entry.getIdpType().equals(IdpType.ANALYZER)).
                collect(Collectors.toList());
    }*/
}