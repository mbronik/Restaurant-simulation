package bin.Worker;

public abstract class Person implements People{
    private String name;
    private String surname;
    private String phoneNumber;

    public Person(String name, String surname, String phoneNumber){
        this.name=name;
        this.surname = surname;
        this.phoneNumber=phoneNumber;
    }

    public void updatePhoneNumber(String phoneNumber){
        this.phoneNumber=phoneNumber;
    }

    public String getName(){
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String toString(){
        return name+" "+ surname +" tel. "+phoneNumber;
    }
}
