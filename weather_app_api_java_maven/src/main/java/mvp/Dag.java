package mvp;

import java.time.LocalDate;

public class Dag {
    float totalTemp = 0;
    String dato;
    Integer count = 0;
    float totalRegn = 0;
    float totalVind = 0;
    String[] brukerVariablerArray;
    Etappe[] etapper = { new Etappe(), new Etappe(), new Etappe(), new Etappe()};

    public float getTotalTemp() { return totalTemp; }

    public void setTotalTemp(float temp)
    { 
        this.totalTemp += temp;
    }
    public String getDato() { return dato; }

    public void setDato(String date)
    { 
        this.dato = date;
    }


    public Integer getCount() { return count; }

    public void setCount()
    { 
        this.count += 1;
    }


    public float getTotalRegn() { return totalRegn; }

    public void setTotalRegn(float regn)
    { 
        this.totalRegn += regn;
    }


    public float getTotalVind() { return totalVind; }

    public void setTotalVind(float vind)
    { 
        this.totalVind += vind;
    }



    public String[] getBrukerVariabler() { return brukerVariablerArray; }

    public void setBrukerVariabler(String brukerVariabler)
    { 
        this.brukerVariablerArray = brukerVariabler.split(", ");
    }


    public void display()
    { 
        String[] uke = { "Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lørdag", "Søndag"};
        String[] months = {
            "Januar",
            "Februar",
            "Mars",
            "April",
            "Mai",
            "Juni",
            "Juli",
            "August",
            "September",
            "Oktober",
            "November",
            "Desember",
        };

        String displayMessage = String.format("\n%s %s.%s %s", uke[LocalDate.parse(this.dato).getDayOfWeek().getValue()-1], this.dato.substring(8, 10), months[Integer.parseInt(this.dato.substring(5, 7))-1], this.dato.substring(0, 4));
        
        for (Integer i = 0; i < this.brukerVariablerArray.length; i++){
            // System.out.println(this.brukerVariablerArray[i]);
            // System.out.println(uke[LocalDate.parse(this.dato).getDayOfWeek().getValue()-1]);
            if (this.brukerVariablerArray[i].toLowerCase().equals("temperatur")) {
                displayMessage += String.format(" (snittemperatur %.2f grader) ", (this.totalTemp / this.count));
            }
            if (this.brukerVariablerArray[i].toLowerCase().equals("regn")) {
                displayMessage += String.format(" %.2fmm regn ", this.totalRegn);
            }
            if (this.brukerVariablerArray[i].toLowerCase().equals("vind")) {
                displayMessage += String.format(" snittvind %.2f m/s ", (this.totalVind / this.count));
            }
        }

        System.out.println(String.format("%s: \n", displayMessage));
        
        for (Etappe etappe : this.etapper) {
            etappe.display(this.brukerVariablerArray);
        }

    }



 //   public void Dag(String newDate, String brukerVariabler) {
 //       brukerVariablerArray = brukerVariabler.split(", ");
 //       dato = newDate;
 //   }


}
