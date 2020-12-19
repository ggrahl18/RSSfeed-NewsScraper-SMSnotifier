import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Companies {
    public String name;
    public List<Company> companiesList;

    public Companies(String name) {
        this.name = name;
        this.companiesList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company createCompany(String name, String url) throws IOException {
        Company company = new Company(name, url);
        this.companiesList.add(company);
        System.out.println(name + " has been added to " + this.name);
        return company;

    }

    private int findCompany(Company company) { return this.companiesList.indexOf(company); }

    private int findCompany(String name) {
        for(int i=0; i<this.companiesList.size(); i++) {
            Company company = this.companiesList.get(i);
            if(company.getName().equals(name)) return i;
        }

        return -1;
    }

    public String queryCompany(Company company) {
        if(findCompany(company) >= 0) {
            return company.getName();
        }

        return null;
    }

    public Company queryCompany(String name) {
        int position = findCompany(name);
        if(position >= 0) {
            return this.companiesList.get(position);
        }

        return null;
    }

    public void deleteCompany(String name) {
        Company company = queryCompany(name);
        if(company != null) {
            System.out.println("Company has been removed");
            this.companiesList.remove(company);
        } else System.out.println("Company could not be removed.");
    }

    // modifyCompany or maybe just remove the company and create another in its stead
}
