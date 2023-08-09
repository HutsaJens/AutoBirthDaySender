package org.example.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.google.api.services.people.v1.model.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

public class GoogleApiHelper {

    private static final String APPLICATION_NAME = "Google People API Java";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Arrays.asList(PeopleServiceScopes.CONTACTS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    public List<Person> fetchConnections(Integer batchSize) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        PeopleService service = buildPeopleService(HTTP_TRANSPORT);

        ListConnectionsResponse response = service.people().connections()
                .list("people/me")
                .setPageSize(batchSize)
                .setPersonFields("names,birthdays")
                .execute();

        return response.getConnections();
    }

    private PeopleService buildPeopleService(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        GoogleClientSecrets clientSecrets = loadClientSecrets();

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        return new PeopleService.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(GoogleApiHelper.APPLICATION_NAME)
                .build();
    }

    private GoogleClientSecrets loadClientSecrets() throws IOException {
        InputStream in = GoogleApiHelper.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        return GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
    }

    public static boolean hasLabel(Person person, String labelName) {
        if (person.getRelations() != null) {
            for (Relation relation : person.getRelations()) {
                System.out.println(person.getRelations());
                if (relation.getType().equals(labelName)) {
                    System.out.println("Found family label for: " + person.getNames().get(0).getDisplayName());
                    return true;
                }
            }
        }
        return false;
    }

    public ContactGroup fetchContactGroup(String labelName, Integer batchSize) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        PeopleService service = buildPeopleService(HTTP_TRANSPORT);

        // Fetch contact group
        ContactGroup contactGroup = service.contactGroups().get("contactGroups/" + labelName)
                .setMaxMembers(batchSize) // Set a non-zero value for maxMembers to retrieve memberResourceNames
                .execute();

        return contactGroup;
    }

    public Person fetchPerson(String memberResourceName) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        PeopleService service = buildPeopleService(HTTP_TRANSPORT);

        return service.people().get(memberResourceName)
                .setPersonFields("names,birthdays") // Customize the fields you want to retrieve
                .execute();
    }
}
