package Hadoop;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeMap;
 
public class MyTMCompUserDefine {
 
    public static void main(String a[]){
        //By using name comparator (String comparison)
        TreeMap<Empl,String> tm = new TreeMap<Empl, String>(new MyNameComp());
        tm.put(new Empl("Ram",3000), "RAM");
        tm.put(new Empl("John",6000), "JOHN");
        tm.put(new Empl("Crish",2000), "CRISH");
        tm.put(new Empl("Tom",2400), "TOM");
        Set<Empl> keys = tm.keySet();
        for(Empl key:keys){
            System.out.println(key+" ==> "+tm.get(key));
        }
        System.out.println("===================================");
        //By using salary comparator (int comparison)
        TreeMap<Empl,String> trmap = new TreeMap<Empl, String>(new MySalaryComp());
        trmap.put(new Empl("Ram",3000), "RAM");
        trmap.put(new Empl("John",6000), "JOHN");
        trmap.put(new Empl("Crish",2000), "CRISH");
        trmap.put(new Empl("Tom",2400), "TOM");
        Set<Empl> ks = trmap.keySet();
        for(Empl key:ks){
            System.out.println(key+" ==> "+trmap.get(key));
        }
    }
}
 
class MyNameComp implements Comparator<Empl>{
 
    @Override
    public int compare(Empl e1, Empl e2) {
        return e1.getName().compareTo(e2.getName());
    }
}
 
class MySalaryComp implements Comparator<Empl>{
 
    @Override
    public int compare(Empl e1, Empl e2) {
        if(e1.getSalary() > e2.getSalary()){
            return 1;
        } else {
            return -1;
        }
    }
}
 
class Empl{
     
    private String name;
    private int salary;
     
    public Empl(String n, int s){
        this.name = n;
        this.salary = s;
    }
     
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getSalary() {
        return salary;
    }
    public void setSalary(int salary) {
        this.salary = salary;
    }
    public String toString(){
        return "Name: "+this.name+"-- Salary: "+this.salary;
    }
}
