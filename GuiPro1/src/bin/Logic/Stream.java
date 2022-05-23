package bin.Logic;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class Stream {
    String fileName;
    private FileWriter out;
    private FileReader in;

    public Stream(String fileName){
        this.fileName=fileName;
        try {
            out = new FileWriter(fileName, true);
            writeToFile("");
            in = new FileReader(fileName);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void writeToFile(String a){
        try {
            out.write(a);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void save(){
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readLine(){
        StringBuilder line = new StringBuilder();
        try {
            int temp;
            do{
                temp = in.read();
                line.append((char)temp);
            }while(temp!='\n' && temp!=-1);
            line.deleteCharAt(line.length()-1);
            if(line.length()==0){
                return "-1";
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return line.toString();
    }
    public String readSegment(){
        return readSegment('|');
    }
    public String readSegment(char separator){
        StringBuilder word = new StringBuilder();
        try {
            int temp;
            do{
                temp = in.read();
                word.append((char)temp);
            }while(temp!=separator && temp!='\n' && temp!=-1);
            word.deleteCharAt(word.length()-1);
            if(word.length()==0){
                return "-1";
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return word.toString();
    }
    public void clear(){
        try {
            FileWriter temp = new FileWriter(fileName);
            temp.write("");
            temp.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        save();
    }
    public void closeFile(){
        try{
            in.close();
            out.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
