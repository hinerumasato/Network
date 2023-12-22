package Teach;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Teach.Student.Student;

public class App {

    private static List<Student> students = new ArrayList<Student>(Arrays.asList(
            new Student("Nguyễn Văn A", 20, 7.5),
            new Student("Nguyễn Văn B", 19, 8.5),
            new Student("Nguyễn Văn C", 21, 9.5)));

    public static void copyFile(String sourceFile, String destFile) throws IOException {
        File file = new File(sourceFile);
        File copyFile = new File(destFile);
        copyFile.createNewFile();
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(copyFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        int offset;
        byte[] buffer = new byte[1024];

        while ((offset = bis.read(buffer)) != -1) {
            bos.write(buffer, 0, offset);
        }

        bis.close();
        bos.close();
    }

    public static void writeStudents(String sourceFile) throws IOException {
        File file = new File(sourceFile);
        FileOutputStream fos = new FileOutputStream(file);
        DataOutputStream dos = new DataOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        if (!file.exists())
            file.createNewFile();
        for (Student student : students) {
            dos.writeUTF(student.getName());
            dos.writeInt(student.getAge());
            dos.writeDouble(student.getAge());
            // oos.writeObject(student);
        }

        dos.close();

    }

    public static void writeStudentRAF(String sourceFile) throws IOException {
        File file = new File(sourceFile);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        List<Long> pointers = new ArrayList<Long>();

        if (!file.exists())
            file.createNewFile();

        // Write header
        raf.writeInt(students.size());
        for (int i = 0; i < students.size(); i++) {
            long currentPointer = raf.getFilePointer();
            pointers.add(currentPointer);
            raf.writeLong(0);
        }

        // Write Body
        for (int i = 0; i < students.size(); i++) {
            long currentPosition = raf.getFilePointer();
            Student student = students.get(i);
            raf.seek(pointers.get(i));
            raf.writeLong(currentPosition);
            raf.seek(currentPosition);
            raf.writeUTF(student.getName());
            raf.writeInt(student.getAge());
            raf.writeDouble(student.getScore());
        }
        raf.close();
    }

    public static Student readStudentRAF(String sourceFile, int index) throws IOException {
        File file = new File(sourceFile);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");

        int size = raf.readInt();
        List<Long> pointers = new ArrayList<Long>();
        for (int i = 0; i < size; i++) {
            long position = raf.readLong();
            pointers.add(position);
        }

        long pointer = pointers.get(index);
        raf.seek(pointer);

        String name = raf.readUTF();
        int age = raf.readInt();
        double score = raf.readDouble();

        Student student = new Student(name, age, score);
        raf.close();
        return student;
    }

    public static void packFolder(String sourceDir, String packFileName) throws IOException {
        File folder = new File(sourceDir);
        File packFile = new File(packFileName);
        packFile.createNewFile();
        RandomAccessFile raf = new RandomAccessFile(packFile, "rw");

        int size = folder.listFiles().length;
        List<Long> pointers = new ArrayList<Long>();
        // Ghi header
        raf.writeInt(size);
        for(int i = 0; i < size; i++) {
            long currentPosition = raf.getFilePointer();
            pointers.add(currentPosition);
            raf.writeLong(0);
        }

        // Ghi body
        for(int i = 0; i < size; i++) {
            long currentPosition = raf.getFilePointer();
            File file = folder.listFiles()[i];
            FileInputStream fis = new FileInputStream(file);

            raf.seek(pointers.get(i));
            raf.writeLong(currentPosition);
            raf.seek(currentPosition);
            raf.writeUTF(file.getName());
            raf.writeLong(file.length());
            raf.write(fis.readAllBytes());
            fis.close();
        }

        raf.close();
    }

    public static void unpackFile(String sourceFile, String destDir) throws IOException {
        File file = new File(sourceFile);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        // Đọc dữ liệu
        int size = raf.readInt();
        List<Long> pointers = new ArrayList<Long>();
        for(int i = 0; i < size; i++) {
            long pointer = raf.readLong();
            pointers.add(pointer);
        } //Đọc xong header

        // Đọc body
        for(int i = 0; i < size; i++) {
            String fileName = raf.readUTF();
            long fileSize = raf.readLong();
            File newFile = new File(destDir + File.separator + fileName);
            FileOutputStream fos = new FileOutputStream(newFile);
            byte[] readBytes = new byte[(int) fileSize];
            raf.read(readBytes);
            fos.write(readBytes);
            fos.close();
        }
        // Sau khi đọc dữ liệu xong
        raf.close();
    }

    public static void unpackFileWithIndex(String sourceFile, String destDir, int index) throws IOException {
        File file = new File(sourceFile);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        // Đọc dữ liệu
        int size = raf.readInt();
        List<Long> pointers = new ArrayList<Long>();
        for(int i = 0; i < size; i++) {
            long pointer = raf.readLong();
            pointers.add(pointer);
        } //Đọc xong header

        // Nhảy con trỏ tới vị trí bắt đầu của cái file đó
        raf.seek(pointers.get(index));
        // Đọc body
        String fileName = raf.readUTF();
        long fileSize = raf.readLong();
        File newFile = new File(destDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(newFile);
        byte[] readBytes = new byte[(int) fileSize];
        raf.read(readBytes);
        fos.write(readBytes);
        fos.close();
        // Sau khi đọc dữ liệu xong
        raf.close();
    }

    public static void main(String[] args) throws IOException {
        // packFolder("resources/pack", "resources/pack.txt");
        // unpackFile("resources/pack.txt", "resources/unpack");
        unpackFileWithIndex("resources/pack.txt", "resources/unpack", 4);
    }
}
