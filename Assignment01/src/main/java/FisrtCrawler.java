import com.github.crab2died.ExcelUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FisrtCrawler {
    public static void main(String[] args) {
        //直接调用对应类方法开始爬虫
        WhuPageProcessor.run();
        TsuPageProcessor.run();
        TjuPageProcessor.run();

        //开始处理爬取的结果
        //生成表头与存放所有教师信息的表
        List<String> header=new ArrayList<>();
        header.add("名字");
        header.add("职称");
        header.add("研究方向");
        header.add("所属大学");
        List<List<String>> allTeachers = new ArrayList<>();

        //开始处理武汉大学的信息
        File dir = new File("./result/cs.whu.edu.cn");
        if(!dir.exists()){
            System.out.println("Whu Crawler error!");
        }
        else{
            for(File file:dir.listFiles()){
                List<String>teacher=new ArrayList<>();
                String jsonString =new String();
                try {
                    jsonString = new String(Files.readAllBytes(Paths.get(file.getPath())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int l=0,r=0;
                String name ="";
                String title ="";
                String field ="";
                String university ="武汉大学";
                l=jsonString.indexOf("姓名： ");
                r=jsonString.indexOf(" ",l+4);
                if(l!=-1&&r!=-1){
                    if(r!=1){
                        name=jsonString.substring(l+4,r);
                    }
                    else{
                        name=jsonString.substring(l+4);
                    }
                }
                l=jsonString.indexOf("职称： ");
                r=jsonString.indexOf("学历学位",l+4);
                if(l!=-1&&r!=-1){
                    if(r!=1){
                        title=jsonString.substring(l+4,r);
                    }
                    else{
                        title=jsonString.substring(l+4);
                    }
                }
                if(title.indexOf("()")!=-1){
                    title=title.substring(0,title.indexOf("()"));
                }
                l=jsonString.indexOf("领域： ");
                r=jsonString.indexOf("招生信息",l+4);
                if(l!=-1&&r!=-1){
                    if(r!=1){
                        field=jsonString.substring(l+4,r);
                    }
                    else{
                        field=jsonString.substring(l+4);
                    }
                }
                teacher.add(name);
                teacher.add(title);
                teacher.add(field);
                teacher.add(university);
                allTeachers.add(teacher);
            }
        }

        //开始处理清华大学的信息
        dir = new File("./result/www.cs.tsinghua.edu.cn");
        if(!dir.exists()){
            System.out.println("Tsu Crawler error!");
        }
        else{
            for(File file:dir.listFiles()){
                List<String>teacher=new ArrayList<>();
                String jsonString =new String();
                try {
                    jsonString = new String(Files.readAllBytes(Paths.get(file.getPath())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int l=0,r=0;
                String name ="";
                String title ="";
                String field ="";
                String university ="清华大学";
                l=jsonString.indexOf("姓名：");
                r=jsonString.indexOf(" ",l+3);
                if(l!=-1&&r!=-1){
                    if(r!=1){
                        name=jsonString.substring(l+3,r);
                    }
                    else{
                        name=jsonString.substring(l+3);
                    }
                }
                l=jsonString.indexOf("职称：");
                r=jsonString.indexOf(" ",l+3);
                if(l!=-1&&r!=-1){
                    if(r!=1){
                        title=jsonString.substring(l+3,r);
                    }
                    else{
                        title=jsonString.substring(l+3);
                    }
                }
                l=jsonString.indexOf("研究领域 ");
                r=jsonString.indexOf(" ",l+5);
                if(l!=-1&&r!=-1){
                    if(r!=1){
                        field=jsonString.substring(l+5,r);
                    }
                    else{
                        field=jsonString.substring(l+5);
                    }
                }
                teacher.add(name);
                teacher.add(title);
                teacher.add(field);
                teacher.add(university);
                allTeachers.add(teacher);
            }
        }

        //开始处理同济大学的信息
        dir =new File("./result/see.tongji.edu.cn");
        if(!dir.exists()){
            System.out.println("Tju Crawler error!");
        }
        else{
            for(File file:dir.listFiles()){
                List<String>teacher=new ArrayList<>();
                String jsonString =new String();
                try {
                    jsonString = new String(Files.readAllBytes(Paths.get(file.getPath())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int l=0,r=0;
                String name ="";
                String title ="";
                String field ="";
                String university ="同济大学";
                l=jsonString.indexOf("姓名 ");
                r=jsonString.indexOf(" ",l+3);
                if(l!=-1&&r!=-1){
                    if(r!=1){
                        name=jsonString.substring(l+3,r);
                    }
                    else{
                        name=jsonString.substring(l+3);
                    }
                }
                l=jsonString.indexOf("职称 ");
                r=jsonString.indexOf("学科",l+3);
                if(l!=-1&&r!=-1){
                    if(r!=1){
                        title=jsonString.substring(l+3,r);
                    }
                    else{
                        title=jsonString.substring(l+3);
                    }
                }
                l=jsonString.indexOf("研究方向 ");
                r=jsonString.indexOf(" ",l+5);
                if(l!=-1&&r!=-1){
                    if(r!=1){
                        field=jsonString.substring(l+5,r);
                    }
                    else{
                        field=jsonString.substring(l+5);
                    }
                }
                teacher.add(name);
                teacher.add(title);
                teacher.add(field);
                teacher.add(university);
                allTeachers.add(teacher);
            }
        }

        //生成Excel表
        try {
            ExcelUtils.getInstance().exportObjects2Excel(allTeachers, header, "./Answer.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}