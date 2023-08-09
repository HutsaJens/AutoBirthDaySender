package org.example;

import com.google.api.services.people.v1.model.ContactGroup;
import com.google.api.services.people.v1.model.Person;
import org.example.google.GoogleApiHelper;
import org.example.model.ContactPerson;
import org.example.util.BirthdayService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final Integer BATCH_SIZE = 300;
    private static final String LABEL_NAME = "AutoCard";


    public static void main(String... args) throws IOException, GeneralSecurityException {
        GoogleApiHelper googleApiHelper = new GoogleApiHelper();

        List<Person> connections = googleApiHelper.fetchConnections(BATCH_SIZE);
        List<ContactPerson> contactPersonList = new ArrayList<>();
        for (Person person : connections) {
            ContactPerson contactPerson = ContactPerson.Companion.fromPerson(person);
            if (contactPerson != null) {
                contactPersonList.add(contactPerson);
            }
        }


        // Fetch specific contact group
        ContactGroup familyGroup = googleApiHelper.fetchContactGroup(LABEL_NAME, BATCH_SIZE);
        List<String> memberResourceNames = familyGroup.getMemberResourceNames();

        List<ContactPerson> familyContacts = new ArrayList<>();
        for (String memberResourceName : memberResourceNames) {
            Person contactPerson = googleApiHelper.fetchPerson(memberResourceName);

            ContactPerson contactPersonTwo = ContactPerson.Companion.fromPerson(contactPerson);
            if (contactPersonTwo != null) {
                familyContacts.add(contactPersonTwo);
            }
            System.out.println("Contact Person: " + contactPersonTwo);
            System.out.println();
            // Process contactPerson and extract desired information
        }

        System.out.println(familyContacts);

        System.out.println("Size: " + contactPersonList.size());
        for (ContactPerson person : contactPersonList) {
            BirthdayService birthdayService = new BirthdayService(person.getBirthday());
            boolean isToday = birthdayService.isTodayBirthday();
            boolean isWithin7Days = birthdayService.isBirthdayWithin7Days();
            boolean isThisMonth = birthdayService.isBirthdayThisMonth();
            String name = person.getName();

            if (isToday) {
                System.out.println("Happy Birthday! " + name);
            } else if (isWithin7Days) {
                System.out.println("Birthday of " + name + " is within the next 7 days.");
            } else if (isThisMonth) {
                System.out.println("Birthday of " + name + " is this month.");
            }
        }

    }
}
