package com.dress.shop.services;



import com.dress.shop.domain.*;
import com.dress.shop.exceptions.AuthException;
import com.dress.shop.exceptions.AuthorizationException;
import com.dress.shop.exceptions.UserDoseNotExistException;
import com.dress.shop.repositories.CartRepository;
import com.dress.shop.repositories.UserRepository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.jboss.logging.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.OIDCAuthenticationError;
import org.keycloak.adapters.OIDCHttpFacade;
import org.keycloak.adapters.rotation.AdapterTokenVerifier;
import org.keycloak.adapters.spi.AuthChallenge;
import org.keycloak.adapters.spi.AuthOutcome;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.common.VerificationException;
import org.keycloak.jose.jws.JWSInput;
import org.keycloak.jose.jws.JWSInputException;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.MappingsRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.security.cert.X509Certificate;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

@Component
public class KeyCloakService {

    @Value("${keycloak.credentials.secret}")
    private String SECRETKEY;

    @Value("${keycloak.resource}")
    private String CLIENTID;

    @Value("${keycloak.auth-server-url}")
    private String AUTHURL;

    @Value("${keycloak.realm}")
    private String REALM;

    @Autowired
    protected KeycloakDeployment deployment;

    @Autowired
    private ApplicationContext environment;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    protected Logger log = Logger.getLogger(com.dress.shop.services.KeyCloakService.class);
    protected String tokenString;
    protected AccessToken token;
    protected String surrogate;
    protected AuthChallenge challenge;

    public LoginResponse getToken(UserCredentials userCredentials) {


       User u= userRepository.findByEmailAddress(userCredentials.getEmail());

       if(u ==null){
           throw new UserDoseNotExistException("you are not registered to the system!");
       }

        UsersResource userRessource = getKeycloakUserResource();

        MappingsRepresentation r = userRessource.get(u.getUserIdentifier()).roles().getAll();

        List<RoleRepresentation> l=r.getRealmMappings();

        String role="";
        for(int i =0; i<l.size(); i++){


            if(l.get(i).getName().toLowerCase().equals("user") || l.get(i).getName().toLowerCase().equals("admin") ){

                role=l.get(i).getName().toString();
            }

        }

        LoginResponse responseToken = null;
        try {

            String username = u.getUserName();



            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("grant_type", "password"));
            urlParameters.add(new BasicNameValuePair("client_id", CLIENTID));
            urlParameters.add(new BasicNameValuePair("username", username));
            urlParameters.add(new BasicNameValuePair("password", userCredentials.getPassword()));
            urlParameters.add(new BasicNameValuePair("client_secret", SECRETKEY));

            responseToken = sendPost(urlParameters,role);

        } catch (Exception e) {
            System.out.println("catch called");

            e.printStackTrace();
        }

        return responseToken;

    }

    public LoginResponse getByRefreshToken(String refreshToken) {

        LoginResponse responseToken = null;
        try {

            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("grant_type", "refresh_token"));
            urlParameters.add(new BasicNameValuePair("client_id", CLIENTID));
            urlParameters.add(new BasicNameValuePair("refresh_token", refreshToken));
            urlParameters.add(new BasicNameValuePair("client_secret", SECRETKEY));

            responseToken = sendPost(urlParameters,"");

        } catch (Exception e) {
            e.printStackTrace();

        }

        return responseToken;
    }

    public User updateUserInKeyCloak(UpdatedUser updatedUser,String uemail){

            User u=userRepository.findByEmailAddress(uemail);
            u.setFirstName(updatedUser.getName());
            u.setLastName(updatedUser.getLastname());
            u.setUserName(updatedUser.getName()+ updatedUser.getLastname());
            u.setEmailAddress(updatedUser.getEmail());
            u.setPassword("1234");
            u.setConfirmPassword("1234");

        try{


            UsersResource userRessource = getKeycloakUserResource();

            UserRepresentation ur = new UserRepresentation();
//            ur.setUsername(u.getUserName());
            ur.setEmail(updatedUser.getEmail());
            ur.setFirstName(updatedUser.getName());
            ur.setLastName(updatedUser.getLastname());
            ur.setEnabled(true);

         


            userRessource.get(u.getUserIdentifier()).update(ur);


        }catch (Exception e) {

            e.printStackTrace();

        }
            return userRepository.save(u);
    }

    public ResponseEntity createUserInKeyCloak(User user) {

        int statusId = 0;
        try {
            user.setUserName(user.getFirstName()+user.getLastName());


            UsersResource userRessource = getKeycloakUserResource();


            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(false);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(user.getPassword());

            UserRepresentation u = new UserRepresentation();
            u.setUsername(user.getUserName());
            u.setEmail(user.getEmailAddress());
            u.setFirstName(user.getFirstName());
            u.setLastName(user.getLastName());
            u.setEnabled(true);


//            u.setCredentials(Collections.singletonList(passwordCred));


            // Create user
			Response result = userRessource.create(u);



            statusId = result.getStatus();



			if (statusId == 201) {


                String userId = result.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");



				// set role
                RealmResource realmResource = getRealmResource();

//                 Get realm role "user" (requires view-realm role)
                RoleRepresentation userRealmRole = realmResource.roles()
                        .get("user").toRepresentation();

				userRessource.get(userId).roles().realmLevel().add(Arrays.asList(userRealmRole));



				userRessource.get(userId).resetPassword(passwordCred);


				user.setUserIdentifier(userId);
				User u1=userRepository.save(user);
				Cart cart=new Cart();
				cart.setUser(u1);
				cartRepository.save(cart);




                KeycloakResponse keycloakResponse=new KeycloakResponse(true,user.getUserName() + " created in system successfully");
                return new ResponseEntity<>(keycloakResponse, HttpStatus.CREATED);


            } else if (statusId == 409) {

                KeycloakResponse keycloakResponse=new KeycloakResponse(false,user.getUserName() + " already present in system");

                return new ResponseEntity<>(keycloakResponse, HttpStatus.CONFLICT);


            } else {
                KeycloakResponse keycloakResponse=new KeycloakResponse(false, user.getUserName() + " could not be created in system");

                return new ResponseEntity<>(keycloakResponse, HttpStatus.BAD_REQUEST);


            }

        } catch (Exception e) {

            e.printStackTrace();

        }
        KeycloakResponse keycloakResponse=new KeycloakResponse(false, user.getUserName() + " could not be created in system");

        return new ResponseEntity<>(keycloakResponse, HttpStatus.BAD_REQUEST);



    }

    // after logout user from the keycloak system. No new access token will be
    // issued.
    public void logoutUser(String userId) {

        UsersResource userRessource = getKeycloakUserResource();

        userRessource.get(userId).logout();

    }

    // Reset passowrd
    public void resetPassword(String newPassword, String userId) {

        UsersResource userResource = getKeycloakUserResource();

        // Define password credential
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(newPassword.toString().trim());

        // Set password credential
        userResource.get(userId).resetPassword(passwordCred);

    }

    private UsersResource getKeycloakUserResource() {

        Keycloak kc = KeycloakBuilder.builder().serverUrl(AUTHURL).realm("master").username("adminn").password("Pa55w0rd")
                .clientId("admin-cli").resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();


        RealmResource realmResource = kc.realm(REALM);
        UsersResource userRessource = realmResource.users();

        return userRessource;
    }

    private RealmResource getRealmResource() {

        Keycloak kc = KeycloakBuilder.builder().serverUrl(AUTHURL).realm("master").username("adminn").password("Pa55w0rd")
                .clientId("admin-cli").resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();

        RealmResource realmResource = kc.realm(REALM);

        return realmResource;

    }

    private LoginResponse sendPost(List<NameValuePair> urlParameters, String role) throws Exception {

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(AUTHURL + "/realms/" + REALM + "/protocol/openid-connect/token");

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";


        String r=",\"role\":\""+role+"\"";
        String isAuth=",\"isAuth\":\""+true+"\"";

        while ((line = rd.readLine()) != null) {

            if(!line.contains("error")){

         line=   line.substring(0,line.lastIndexOf("}"))+r+isAuth+"}";
            }
            result.append(line);
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(result.toString());
        LoginResponse loginResponse=new LoginResponse();
        long unixTime = System.currentTimeMillis() / 1000L;

        loginResponse.setExpires_in((Long.parseLong(node.get("expires_in").asText())+unixTime));
        loginResponse.setRefresh_expires_in(Long.parseLong(node.get("refresh_expires_in").asText())+unixTime);
        loginResponse.setAccess_token(node.get("access_token").asText());
        loginResponse.setRefresh_token(node.get("refresh_token").asText());
        loginResponse.setRole(node.get("role").asText());
        loginResponse.setIsAuth(node.get("isAuth").asText());
        loginResponse.setToken_type(node.get("token_type").asText());



        return loginResponse;

    }


    public AuthChallenge getChallenge() {
        return challenge;
    }

    public String getTokenString() {
        return tokenString;
    }

    public AccessToken getToken() {
        return token;
    }

    public String getSurrogate() {
        return surrogate;
    }

    public AuthOutcome authenticate(HttpServletRequest exchange,String role) {

        String header_authorization = exchange.getHeader("Authorization");


        if(StringUtils.isBlank(header_authorization)){
            throw new AuthException("login first");
        }
        List<String> authHeaders = (StringUtils.isBlank(header_authorization)) ? null : Arrays.asList(header_authorization);
        if (authHeaders == null || authHeaders.isEmpty()) {
            challenge = challengeResponse(exchange, OIDCAuthenticationError.Reason.NO_BEARER_TOKEN, null, null);
            return AuthOutcome.NOT_ATTEMPTED;
        }


        tokenString = null;
        for (String authHeader : authHeaders) {
            String[] split = authHeader.trim().split("\\s+");
            if (split.length != 2) continue;
            if (split[0].equalsIgnoreCase("Bearer")) {

                tokenString = split[1].trim();

                log.debugf("Found [%d] values in authorization header, selecting the first value for Bearer.", (Integer) authHeaders.size());
                break;
            }
        }

        if (tokenString == null) {

            challenge = challengeResponse(exchange, OIDCAuthenticationError.Reason.NO_BEARER_TOKEN, null, null);
            return AuthOutcome.NOT_ATTEMPTED;
        }

        return (authenticateToken(exchange, tokenString,role));
    }

//    public TokenPayload authenticate(HttpServletRequest exchange) {
//
//        String header_authorization = exchange.getHeader("Authorization");
//
//
//        if(StringUtils.isBlank(header_authorization)){
//            throw new AuthException("login first");
//        }
//        List<String> authHeaders = (StringUtils.isBlank(header_authorization)) ? null : Arrays.asList(header_authorization);
//        if (authHeaders == null || authHeaders.isEmpty()) {
//            challenge = challengeResponse(exchange, OIDCAuthenticationError.Reason.NO_BEARER_TOKEN, null, null);
//            return null;
//        }
//
//
//        tokenString = null;
//        for (String authHeader : authHeaders) {
//            String[] split = authHeader.trim().split("\\s+");
//            if (split.length != 2) continue;
//            if (split[0].equalsIgnoreCase("Bearer")) {
//
//                tokenString = split[1];
//
//                log.debugf("Found [%d] values in authorization header, selecting the first value for Bearer.", (Integer) authHeaders.size());
//                break;
//            }
//        }
//
//        if (tokenString == null) {
//
//            challenge = challengeResponse(exchange, OIDCAuthenticationError.Reason.NO_BEARER_TOKEN, null, null);
//            return null;
//        }
//
//        return decodeToken(exchange, tokenString);
//    }




    private void init() {
        AdapterConfig cfg = new AdapterConfig();
        cfg.setRealm(environment.getEnvironment().getProperty("keycloak.realm"));
        cfg.setAuthServerUrl(environment.getEnvironment().getProperty("keycloak.auth-server-url"));
        cfg.setResource(environment.getEnvironment().getProperty("keycloak.resource"));
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("secret", environment.getEnvironment().getProperty("keycloak.credentials-secret"));
        cfg.setCredentials(credentials);

        deployment = KeycloakDeploymentBuilder.build(cfg);

    }

//    protected TokenPayload decodeToken(HttpServletRequest exchange, String tokenString) {
//
//        init();
//        TokenPayload tokenPayload=new TokenPayload();
//
//        log.debug("Verifying access_token");
////		if (log.isTraceEnabled()) {
//
//        try {
//            JWSInput jwsInput = new JWSInput(tokenString);
//            String wireString = jwsInput.getWireString();
//            log.tracef("\taccess_token: %s", wireString.substring(0, wireString.lastIndexOf(".")) + ".signature");
//
//
//        } catch (JWSInputException e) {
//            System.out.println("Failed to parse access_toke");
//            return null;
//        }
////		}
//        try {
//            token = AdapterTokenVerifier.verifyToken(tokenString, deployment);
//
//
//
//            tokenPayload.setEmail(token.getEmail());
//            tokenPayload.setLastname(token.getFamilyName());
//            tokenPayload.setName(token.getGivenName());
//
//
//
//        } catch (VerificationException e) {
//            log.debug("Failed to verify token");
//            challenge = challengeResponse(exchange, OIDCAuthenticationError.Reason.INVALID_TOKEN, "invalid_token", e.getMessage());
//            throw new AuthorizationException("token is not valid");
////            return AuthOutcome.FAILED;
//        }
//        if (token.getIssuedAt() < deployment.getNotBefore()) {
//            log.debug("Stale token");
//            challenge = challengeResponse(exchange, OIDCAuthenticationError.Reason.STALE_TOKEN, "invalid_token", "Stale token");
//            System.out.println("token.getIssuedAt() < deployment.getNotBefore() ");
//
//            return null;
//        }
//        boolean verifyCaller = false;
//        if (deployment.isUseResourceRoleMappings()) {
//            verifyCaller = token.isVerifyCaller(deployment.getResourceName());
//        } else {
//            verifyCaller = token.isVerifyCaller();
//        }
//        surrogate = null;
//        if (verifyCaller) {
//
//            if (token.getTrustedCertificates() == null || token.getTrustedCertificates().isEmpty()) {
//                log.warn("No trusted certificates in token");
//                challenge = clientCertChallenge();
//
//                return null;
//            }
//
//            // for now, we just make sure Undertow did two-way SSL
//            // assume JBoss Web verifies the client cert
//            X509Certificate[] chain = new X509Certificate[0];
//            try {
//
//                chain = (X509Certificate[]) exchange
//                        .getAttribute("javax.servlet.request.X509Certificate");
//
////				chain = exchange.getCertificateChain();
//            } catch (Exception ignore) {
//
//            }
//            if (chain == null || chain.length == 0) {
//
//                log.warn("No certificates provided by undertow to verify the caller");
//                challenge = clientCertChallenge();
//                return null;
//            }
//            surrogate = chain[0].getSubjectDN().getName();
//        }
//        log.debug("successful authorized");
//        return tokenPayload;
//    }


    protected AuthOutcome authenticateToken(HttpServletRequest exchange, String tokenString,String role) {

        init();

        String wireString="";
        try {
            JWSInput jwsInput = new JWSInput(tokenString);
             wireString = jwsInput.getWireString();
            log.tracef("\taccess_token: %s", wireString.substring(0, wireString.lastIndexOf(".")) + ".signature");


        } catch (JWSInputException e) {
            System.out.println("Failed to parse access_toke");

            log.errorf(e, "Failed to parse access_token: %s", tokenString);
        }
        try {
            token = AdapterTokenVerifier.verifyToken(wireString, deployment);

            System.out.println(token);
            Set<String> roles= token.getRealmAccess().getRoles();

            boolean authorized =false;
            for(String r: roles){
                if(r.toLowerCase().equals(role))
                {
                    authorized=true;
                    System.out.println(r);


                }

            }

            if(!authorized)
                throw new AuthorizationException("you are not authorized to do this action!");


        } catch (VerificationException e) {
            log.debug("Failed to verify token");
            challenge = challengeResponse(exchange, OIDCAuthenticationError.Reason.INVALID_TOKEN, "invalid_token", e.getMessage());
            throw new AuthException("Failed to verify token");
//            return AuthOutcome.FAILED;
        }
        if (token.getIssuedAt() < deployment.getNotBefore()) {
            log.debug("Stale token");
            challenge = challengeResponse(exchange, OIDCAuthenticationError.Reason.STALE_TOKEN, "invalid_token", "Stale token");
            System.out.println("token.getIssuedAt() < deployment.getNotBefore() ");

            return AuthOutcome.FAILED;
        }
        boolean verifyCaller = false;
        if (deployment.isUseResourceRoleMappings()) {
            verifyCaller = token.isVerifyCaller(deployment.getResourceName());
        } else {
            verifyCaller = token.isVerifyCaller();
        }
        surrogate = null;
        if (verifyCaller) {

            if (token.getTrustedCertificates() == null || token.getTrustedCertificates().isEmpty()) {
                log.warn("No trusted certificates in token");
                challenge = clientCertChallenge();

                return AuthOutcome.FAILED;
            }

            // for now, we just make sure Undertow did two-way SSL
            // assume JBoss Web verifies the client cert
            X509Certificate[] chain = new X509Certificate[0];
            try {

                chain = (X509Certificate[]) exchange
                        .getAttribute("javax.servlet.request.X509Certificate");

//				chain = exchange.getCertificateChain();
            } catch (Exception ignore) {

            }
            if (chain == null || chain.length == 0) {

                log.warn("No certificates provided by undertow to verify the caller");
                challenge = clientCertChallenge();
                return AuthOutcome.FAILED;
            }
            surrogate = chain[0].getSubjectDN().getName();
        }
        log.debug("successful authorized");
        return AuthOutcome.AUTHENTICATED;
    }



    protected AuthChallenge clientCertChallenge() {
        return new AuthChallenge() {
            @Override
            public int getResponseCode() {
                return 0;
            }

            @Override
            public boolean challenge(HttpFacade exchange) {
                // do the same thing as client cert auth
                return false;
            }
        };
    }


    protected AuthChallenge challengeResponse(HttpServletRequest facade, final OIDCAuthenticationError.Reason reason, final String error, final String description) {
        StringBuilder header = new StringBuilder("Bearer realm=\"");
        header.append(deployment.getRealm()).append("\"");
        if (error != null) {
            header.append(", error=\"").append(error).append("\"");
        }
        if (description != null) {
            header.append(", error_description=\"").append(description).append("\"");
        }
        final String challenge = header.toString();
        return new AuthChallenge() {
            @Override
            public int getResponseCode() {
                return 401;
            }

            @Override
            public boolean challenge(HttpFacade facade) {
                if (deployment.getPolicyEnforcer() != null) {
                    deployment.getPolicyEnforcer().enforce(OIDCHttpFacade.class.cast(facade));
                    return true;
                }
                OIDCAuthenticationError error = new OIDCAuthenticationError(reason, description);
                facade.getRequest().setError(error);
                facade.getResponse().addHeader("WWW-Authenticate", challenge);
                if (deployment.isDelegateBearerErrorResponseSending()) {
                    facade.getResponse().setStatus(401);
                } else {
                    facade.getResponse().sendError(401);
                }
                return true;
            }
        };
    }

}
