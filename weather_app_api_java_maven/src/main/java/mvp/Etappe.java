package mvp;

import java.time.LocalDate;

public class Etappe {
    Integer timeStart;
    Integer timeEnd;
    float temp = 0;
    float low = 9999;
    float high = -9999;
    Integer count = 0;
    float regn = 0;
    float vind = 0;

    public Integer getTimeStart() { return this.timeStart; }

    public void setTimeStart(Integer timeStart)
    { 
        this.timeStart = timeStart;
    }

    public Integer getTimeEnd() { return this.timeEnd; }

    public void setTimeEnd(Integer timeEnd)
    { 
        this.timeEnd = timeEnd;
    }


    public float getTemp() { return this.temp; }

    public void setTemp(float temp)
    { 
        this.temp += temp;
    }
    public float getLow() { return this.low; }
    public float getHigh() { return this.high; }

    public void setHighLow(float temperatur)
    { 
        if (temperatur < this.low){

            this.low = temperatur;
        }
        if ( temperatur > this.high) {
            this.high = temperatur;
        }
    }

    public float getRegn() { return this.regn; }

    public void setRegn(float regn)
    { 
        this.regn += regn;
    }
    public float getVind() { return this.vind; }

    public void setVind(float vind)
    { 
        this.vind += vind;
    }
    
    public Integer getCount() { return count; }

    public void setCount()
    { 
        this.count += 1;
    }

        public void display(String[]brukerVariablerArray){ 
       
        String displayMessage = String.format("%d-%d: ",this.timeStart, this.timeEnd);
        
        for (Integer i = 0; i < brukerVariablerArray.length; i++){
            // System.out.println(this.brukerVariablerArray[i]);
            // System.out.println(uke[LocalDate.parse(this.dato).getDayOfWeek().getValue()-1]);
            if (brukerVariablerArray[i].toLowerCase().equals("temperatur")) {
                displayMessage += String.format(" fra %.2f til %.2f grader (snittemperatur %.2f grader)  ", this.low, this.high, (this.temp / this.count));
            }
            if (brukerVariablerArray[i].toLowerCase().equals("regn")) {
                displayMessage += String.format(" %.2fmm regn ", this.regn);
            }
            if (brukerVariablerArray[i].toLowerCase().equals("vind")) {
                displayMessage += String.format("  %.2f m/s", (this.vind / this.count));
            }
        }

        System.out.println(String.format("%s: \n", displayMessage));
    }


}
