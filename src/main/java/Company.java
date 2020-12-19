import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Company {
    public static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID"); // set environment path
    public static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN"); // set environment path
    public static final String ABSOLUTE_PATH = "/home/boss/Programming/Java/Projects/twilioSendSMS/news/"; // set news folder path
    public static final String CURRENT_NEWS_PATH = "/currentNews.txt"; // set news folder path
    public static final String OLD_NEWS_PATH = "/oldNews.txt"; // set news folder path
    public static final String NEWEST_NEWS_PATH = "/newestNews.txt"; // set news folder path
    public String name;
    public String url;
    public ArrayList<Person> persons;

    public Company(String name, String url) throws IOException {
        this.name = name;
        this.url = url;
        this.persons = new ArrayList<>();

        // create company news directory and text files
        File fDirectory = new File(ABSOLUTE_PATH + name + "/");
        if(!(fDirectory.exists())) {
            fDirectory.mkdir();
            File fCurrentNews = new File(ABSOLUTE_PATH + name + CURRENT_NEWS_PATH);
            fCurrentNews.createNewFile();
            File fOldNews = new File(ABSOLUTE_PATH + name + OLD_NEWS_PATH);
            fOldNews.createNewFile();
            File fNewestNews = new File(ABSOLUTE_PATH + name + NEWEST_NEWS_PATH);
            fNewestNews.createNewFile();
            System.out.println(name + " news directory and text files created.");
        }
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void addRecipient(Person person) {
        this.persons.add(person);
        System.out.println(person.getName() + " was added to " + this.name);
    }

    public void removeRecipient(Person person) {
        this.persons.remove(person);
        System.out.println(person.getName() + " was removed from " + this.name);
    }

    public Runnable run() {
        System.out.println("Scanning for news...");
        try {
            scrape();
        } catch (IOException e) {
            e.printStackTrace();
        }
//
        String message = compare();

        // if message.length() is 0, then it is empty, and no need to send message
        assert message != null;
        if(message.length() != 0) {
            sendSMS(message);
        } else {
            System.out.println("Nothing new.");
            System.out.println("--------------------------");
        }
        return null;
    }

    private void scrape() throws IOException {
        PrintWriter out = new PrintWriter(ABSOLUTE_PATH + this.name + CURRENT_NEWS_PATH); // set absolute path
        ScrapeIt scrape = new ScrapeIt();
        ArrayList<String> hyperlinks = scrape.doItAll(url); // replace with custom stock search in rss feed (xml)

        //  iterate through arraylist and print it out (i++)
        for (String s : hyperlinks) {
            out.println(s);
        }

        out.close();
        System.out.println("Scraper finished.");
    }

    private String compare() {
        try {
            // Assigns currentNews.txt and oldNews.txt into an ArrayList<String>
            String currentNews = ABSOLUTE_PATH + this.name + CURRENT_NEWS_PATH; // specify absolute paths
            String oldNews = ABSOLUTE_PATH + this.name + OLD_NEWS_PATH;
            String newestNews = ABSOLUTE_PATH + this.name + NEWEST_NEWS_PATH;
            BufferedReader br1 = null;
            BufferedReader br2 = null;
            String sCurrentLine;
            List<String> list1 = new ArrayList<String>();
            List<String> list2 = new ArrayList<String>();
            br1 = new BufferedReader(new FileReader(currentNews));
            br2 = new BufferedReader(new FileReader(oldNews));

            while ((sCurrentLine = br1.readLine()) != null) {
                list1.add(sCurrentLine);
            }
            while ((sCurrentLine = br2.readLine()) != null) {
                list2.add(sCurrentLine);
            }

            // removes oldNews from currentNews, assigning the newest news to tempList
            List<String> tempList = new ArrayList<String>(list1);
            tempList.removeAll(list2);

            System.out.println("--------------------------");
            System.out.println("Newest Release: ");

            for (int i = 0; i < tempList.size(); i++) {
                System.out.println(tempList.get(i)); //content from currentNews.txt which is not there in test2.txt
            }

            // write tempList to newestNews.txt
            PrintWriter rewriteNewestNews = new PrintWriter(newestNews);
            if (list1.size() != list2.size()) {
                rewriteNewestNews.print("");

                for (String s : tempList) {
                    rewriteNewestNews.println(s);
                }
            }
            rewriteNewestNews.close();

            // erases the content in currentNews.txt
            PrintWriter rewrite = new PrintWriter(currentNews);
            rewrite.print("");
            rewrite.close();

            // make oldNews.txt = currentNews.txt
            PrintWriter rewriteOldNews = new PrintWriter(oldNews);
            for (String s : list1) {
                rewriteOldNews.println(s);
            }
            rewriteOldNews.close();

            // turn templist into String to be sent through SMS
            StringBuilder sb = new StringBuilder();
            for (String s : tempList) {
                sb.append(s);
            }

            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendSMS(String text) {
        // multiple recipients
        System.out.println("--------------------------");
        System.out.println("Recipients: ");

        Iterator<Person> person = this.persons.iterator();
        while(person.hasNext()) {
            String number = person.next().getNumber();
            System.out.print(number + ";");
            try {
                    // comment out this inner block to not send text message
                    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

                    Message message = Message.creator(new PhoneNumber(number),
                            new PhoneNumber(System.getenv("TWILIO_NUMBER")),
                            text).create();

                    System.out.println();
                    System.out.println("Sid = "+ message.getSid());
                System.out.println();
                System.out.println("--------------------------");
                // comment out to here
            } catch (final ApiException e) {
                System.err.println(e);
            }
        }
    }
}
