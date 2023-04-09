import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.PasswordAuthentication;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
// mailBilgi sınıfı, kullanıcının girdiği e-posta bilgilerini depolar
class mailBilgi{
    String elitAd="",elitSoyad="", elitMail="", genelAd="", genelSoyad="", genelMail="";
    Scanner scan1=new Scanner(System.in);
}
// uyeEkleme sınıfı, yeni üyeleri kaydeder, mevcut üyeleri okur ve e-posta gönderir
class uyeEkleme extends mailBilgi {
    // elitUye() yöntemi, elit bir üyenin bilgilerini alır ve kaydeder
    public void elitUye() {
        System.out.print("Lütfen elit üyenin adını giriniz: ");
        elitAd = scan1.next();
        System.out.print("Lütfen elit üyenin soyadını giriniz: ");
        elitSoyad = scan1.next();
        System.out.print("Lütfen elit üyenin mail adresini giriniz: ");
        elitMail = scan1.next();
        System.out.println("isim: " + elitAd + " " + elitSoyad + ", mail: " + elitMail);
        kaydet(elitAd, elitSoyad, elitMail, "Elit");
    }
    // genelUye() yöntemi, genel bir üyenin bilgilerini alır ve kaydeder
    public void genelUye() {
        System.out.print("Lütfen genel üyenin adını giriniz: ");
        genelAd = scan1.next();
        System.out.print("Lütfen genel üyenin soyadını giriniz: ");
        genelSoyad = scan1.next();
        System.out.print("Lütfen genel üyenin mail adresini giriniz: ");
        genelMail = scan1.next();
        System.out.println("isim: " + genelAd + " " + genelSoyad + ", mail: " + genelMail);
        kaydet(genelAd, genelSoyad, genelMail, "Genel");
    }
    // kaydet() yöntemi, yeni bir üye bilgisini uye.txt dosyasına kaydeder
    private void kaydet(String ad, String soyad, String email, String tip) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("uye.txt", true));
            writer.append(ad + "    " + soyad + " ,   " + email + "#" + tip + "Üye#\n");
            writer.close();
            System.out.println("Üye bilgileri başarıyla kaydedildi.");
        } catch (IOException e) {
            System.out.println("Üye bilgileri kaydedilemedi: " + e.getMessage());
        }
    }
    // statik blok, mevcut üye bilgilerini okur ve konsola yazar
    static {
        try {
            // Dosya varsa oku
            List<String> lines = Files.readAllLines(Paths.get("uye.txt"));
            List<String> elitler = new ArrayList<>();
            List<String> geneller = new ArrayList<>();
            for (String line : lines) {
                String[] tokens = line.split("#");
                if (tokens[1].equals("ELİT ÜYESI")) {
                    elitler.add(tokens[0]);
                } else if (tokens[1].equals("GENEL ÜYESI")) {
                    geneller.add(tokens[0]);
                }
            }
            // Elit üyeleri yazdır
            System.out.println("ELİT ÜYELER:");
            System.out.println("------------");
            for (String line : elitler) {
                String[] tokens = line.split(",");
                System.out.println(tokens[0] + "    " + tokens[1] + " (" + tokens[2] + ")");
            }
            // Genel üyeleri yazdır
            System.out.println("\nGENEL ÜYELER:");
            System.out.println("-------------");
            for (String line : geneller) {
                String[] tokens = line.split(",");
                System.out.println(tokens[0] + "    " + tokens[1] + " (" + tokens[2] + ")");
            }
        } catch (IOException e) {
            System.out.println("Dosya okunamadı: " + e.getMessage());
        }
    }
    public void mailGonder() {
        String gonderici = "yusufuveyskaplan1@gmail.com";
        char[] sifre = {'q','m','q','x','m','n','u','m','e','m','w','j','t','g','m','n'};
        PasswordAuthentication passwordAuthentication = new PasswordAuthentication(gonderici, sifre);
        String alici;
        String konu;
        String icerik;
        Scanner scan = new Scanner(System.in);

        System.out.print("Lütfen mail göndermek istediğiniz üyenin mail adresini giriniz: ");
        alici = scan.nextLine();
        System.out.print("Lütfen mail konusunu giriniz: ");
        konu = scan.nextLine();
        System.out.print("Lütfen mail içeriğini giriniz: ");
        icerik = scan.nextLine();

        String host = "smtp.gmail.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(gonderici, String.valueOf(sifre));
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(gonderici));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(alici));
            message.setSubject(konu);
            message.setText(icerik);

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(icerik);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            String filename = "uye.txt";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Mail başarıyla gönderildi.");

        } catch (MessagingException e) {
            System.out.println("Mail gönderilemedi: " + e.getMessage());
        }
    }
}

public class Main {
    public static void main(String[] args) {
        int islem;
        uyeEkleme yeni=new uyeEkleme();
        mailBilgi mail=new mailBilgi();
        Scanner scan=new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.println("1. Elit Üye Ekleme.");
            System.out.println("2. Genel Üye Ekleme.");
            System.out.println("3. Mail Gönderme.");
            System.out.print("Lütfen yapmak istedğiniz işlemi seçiniz: ");
            islem = scan.nextInt();
            System.out.println();
            switch (islem) {
                case (1) -> {
                    yeni.elitUye();
                }
                case (2) -> {
                    yeni.genelUye();
                }
                case (3) -> {
                    yeni.mailGonder();
                }
                default -> System.out.println("geçersiz işlem");
            }
        }
    }
}