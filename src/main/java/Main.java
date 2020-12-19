import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        runAll();

        System.out.println("--------------------------");
    }

    public static Runnable process() throws IOException {
        People investors = People.getInstance("investors");
        Person personName = new Person("personName", "+15395882563"); // example name & phone number
        investors.addPerson(personName);
        System.out.println("--------------------------");

        Companies companies = new Companies("pink");

        // replace url with RSS feed
        Company decisionDiagnostics = companies.createCompany("DECN", "https://rss.app/feeds/1QtbF7Q6zMa8E17k.xml");
        decisionDiagnostics.addRecipient(personName);
        decisionDiagnostics.run();

        // replace url with RSS feed
        Company enzolytics = companies.createCompany("ENZC", "https://rss.app/feeds/Ttlmq05MWM18AuiO.xml");
        enzolytics.addRecipient(personName);
        enzolytics.run();

        return null;
    }

    public static void runAll() throws IOException {

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture < ? > handle =
                scheduler.scheduleWithFixedDelay( new Runnable() {
                    public void run () {
                        try {
                            // Do business logic.
                            System.out.println("Running...");
                            process();
                        } catch ( Exception e ) {
                            // … handle exception/error. Trap any unexpected exception here rather to stop it reaching and shutting-down the scheduled executor service.
                            // logger.error( "Caught exception in ScheduledExecutorService. StackTrace:\n" + e.getStackTrace() );
                        }
                    }
                } , 0 , 15 , TimeUnit.SECONDS );
    }
}